package flightoptimizer.score;

import flightoptimizer.domain.Plane;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import static org.optaplanner.core.api.score.stream.Joiners.*;

public class LandingPlanConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                // HARD constraints
                planeMustLand(constraintFactory),
                planesCantLandAtTheSameTime(constraintFactory),
                planeTimeBoundMiss(constraintFactory),
                notEnoughPlaneSeparationTime(constraintFactory),
                // SOFT constraints
                planeUnderTargetDeviation(constraintFactory),
                planeOverTargetDeviation(constraintFactory)
        };
    }

    // HARD constraints

    // Every plane must land
    private Constraint planeMustLand(ConstraintFactory constraintFactory) {
        return constraintFactory.forEachIncludingNullVars(Plane.class)
                .filter(plane -> plane.getRunway() == null || plane.getLandingTime() == null)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Plane must land");
    }

    // Planes cant land at the same time
    private Constraint planesCantLandAtTheSameTime(ConstraintFactory constraintFactory) {
        return constraintFactory.forEachUniquePair(Plane.class, equal(Plane::getLandingTime))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Planes cant land at the same time");
    }

    // A plane must land between its earliest and latest landing time
    private Constraint planeTimeBoundMiss(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Plane.class)
                .filter(plane -> !isBetween(plane.getLandingTime(), plane.getEarliestLandingTime(), plane.getLatestLandingTime()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Plane time bound miss");
    }

    // A plane must have enough separation time before the next plane lands
    private Constraint notEnoughPlaneSeparationTime(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Plane.class)
                .join(Plane.class,
                        equal(Plane::getRunway),
                        lessThan(Plane::getLandingTime))
                .ifNotExists(Plane.class,
                        equal((plane1, plane2) -> plane1.getRunway(), Plane::getRunway),
                        lessThan((plane1, plane2) -> plane1.getLandingTime(), Plane::getLandingTime),
                        greaterThan((plane1, plane2) -> plane2.getLandingTime(), Plane::getLandingTime))
                .penalize(HardSoftScore.ONE_HARD, (plane1, plane2) -> {
                    int requiredSeparationTime = plane1.getSeparationTimes().get(plane2.getId() - 1);
                    int actualSeparationTime = plane2.getLandingTime() - plane1.getLandingTime();

                    return actualSeparationTime < requiredSeparationTime
                            ? requiredSeparationTime - actualSeparationTime : 0;
                })
                .asConstraint("Planes must have enough separation time");
    }

    // SOFT constraints

    // A plane must land close to the target landing time (if landed within the earliest and latest times)
    private Constraint planeUnderTargetDeviation(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Plane.class)
                .filter(plane -> plane.getLandingTime() >= plane.getEarliestLandingTime() &&
                        plane.getLandingTime() < plane.getTargetLandingTime())
                .penalize(HardSoftScore.ONE_SOFT, plane -> plane.getTargetLandingTime() - plane.getLandingTime())
                .asConstraint("Plane landing under target deviation");
    }

    // A plane must land close to the target landing time (if landed within the earliest and latest times)
    private Constraint planeOverTargetDeviation(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Plane.class)
                .filter(plane -> plane.getLandingTime() <= plane.getLatestLandingTime() &&
                        plane.getLandingTime() > plane.getTargetLandingTime())
                .penalize(HardSoftScore.ONE_SOFT, plane -> plane.getLandingTime() - plane.getTargetLandingTime())
                .asConstraint("Plane landing over target deviation");
    }

    private static boolean isBetween(int value, int lowerBound, int upperBound) {
        return value >= lowerBound && value <= upperBound;
    }
}

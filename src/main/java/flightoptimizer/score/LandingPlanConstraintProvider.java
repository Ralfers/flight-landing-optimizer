package flightoptimizer.score;

import flightoptimizer.domain.Landing;
import flightoptimizer.domain.Plane;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import java.util.function.Function;

import static org.optaplanner.core.api.score.stream.Joiners.*;

public class LandingPlanConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                // HARD constraints
                planeMustLand(constraintFactory),
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
        return constraintFactory.forEach(Plane.class)
                .ifNotExists(Landing.class, equal(Function.identity(), Landing::getPlane))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Plane must land");
    }

    // A plane must land between its earliest and latest landing time
    private Constraint planeTimeBoundMiss(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Landing.class)
                .filter(landing -> {
                    Plane plane = landing.getPlane();
                    return !isBetween(landing.getLandingTime(), plane.getEarliestLandingTime(), plane.getLatestLandingTime());
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Plane time bound miss");
    }

    // A plane must have enough separation time before the next plane lands
    private Constraint notEnoughPlaneSeparationTime(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Landing.class)
                .join(Landing.class,
                        equal(Landing::getRunway),
                        lessThan(Landing::getLandingTime))
                .ifNotExists(Landing.class,
                        equal((a, b) -> a.getRunway(), Landing::getRunway),
                        lessThan((a, b) -> a.getLandingTime(), Landing::getLandingTime),
                        greaterThan((a, b) -> b.getLandingTime(), Landing::getLandingTime))
                .penalize(HardSoftScore.ONE_HARD, (a, b) -> {
                    Plane firstPlane = a.getPlane();
                    Plane secondPlane = b.getPlane();

                    int requiredSeparationTime = firstPlane.getSeparationTimes().get(secondPlane.getId() - 1);
                    int actualSeparationTime = b.getLandingTime() - a.getLandingTime();

                    return actualSeparationTime < requiredSeparationTime
                            ? requiredSeparationTime - actualSeparationTime : 0;
                })
                .asConstraint("Planes must have enough separation time");
    }

    // SOFT constraints

    // A plane must land close to the target landing time (if landed within the earliest and latest times)
    private Constraint planeUnderTargetDeviation(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Landing.class)
                .filter(landing -> {
                    Plane plane = landing.getPlane();
                    return landing.getLandingTime() >= plane.getEarliestLandingTime() &&
                            landing.getLandingTime() < plane.getTargetLandingTime();
                })
                .penalize(HardSoftScore.ONE_SOFT, landing -> {
                    Plane plane = landing.getPlane();
                    return plane.getTargetLandingTime() - landing.getLandingTime();
                })
                .asConstraint("Plane landing under target deviation");
    }

    // A plane must land close to the target landing time (if landed within the earliest and latest times)
    private Constraint planeOverTargetDeviation(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Landing.class)
                .filter(landing -> {
                    Plane plane = landing.getPlane();
                    return landing.getLandingTime() <= plane.getLatestLandingTime() &&
                            landing.getLandingTime() > plane.getTargetLandingTime();
                })
                .penalize(HardSoftScore.ONE_SOFT, landing -> {
                    Plane plane = landing.getPlane();
                    return landing.getLandingTime() - plane.getTargetLandingTime();
                })
                .asConstraint("Plane landing over target deviation");
    }

    private static boolean isBetween(int value, int lowerBound, int upperBound) {
        return value >= lowerBound && value <= upperBound;
    }
}

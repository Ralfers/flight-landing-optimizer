package flightoptimizer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Plane {

    private int id;

    private int arrivalTime;

    private int earliestLandingTime;

    private int targetLandingTime;

    private int latestLandingTime;

    private int penaltyForUnderTarget;

    private int penaltyForOverTarget;

    List<Integer> separationTimes = new ArrayList<>();

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}

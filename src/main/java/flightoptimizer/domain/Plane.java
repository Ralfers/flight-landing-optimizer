package flightoptimizer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.ArrayList;
import java.util.List;

@PlanningEntity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Plane {

    @PlanningId
    private int id;

    private int arrivalTime;

    private int earliestLandingTime;

    private int targetLandingTime;

    private int latestLandingTime;

    private int penaltyForUnderTarget;

    private int penaltyForOverTarget;

    List<Integer> separationTimes = new ArrayList<>();

    @PlanningVariable(valueRangeProviderRefs = "landingTimeRange")
    private Integer landingTime;

    @PlanningVariable(valueRangeProviderRefs = "runwayRange")
    private Runway runway;

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}

package flightoptimizer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.CountableValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
@NoArgsConstructor
@Getter
@Setter
public class LandingPlan {

    @ValueRangeProvider(id = "runwayRange")
    @ProblemFactCollectionProperty
    private List<Runway> runwayList;

    @PlanningEntityCollectionProperty
    private List<Plane> planeList;

    @PlanningScore
    private HardSoftScore score;

    public LandingPlan(List<Plane> planeList, List<Runway> runwayList) {
        this.planeList = planeList;
        this.runwayList = runwayList;
    }

    @ValueRangeProvider(id = "landingTimeRange")
    public CountableValueRange<Integer> getLandingTimeRange(){
        return ValueRangeFactory.createIntValueRange(1, 100000);
    }
}

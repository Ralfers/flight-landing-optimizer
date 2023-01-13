package flightoptimizer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Landing {

    @PlanningId
    private Long id;

    private Integer landingTime;

    @PlanningVariable(valueRangeProviderRefs = "planeRange")
    private Plane plane;
    @PlanningVariable(valueRangeProviderRefs = "runwayRange")
    private Runway runway;
}

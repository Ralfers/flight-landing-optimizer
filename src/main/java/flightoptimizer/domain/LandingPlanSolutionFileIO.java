package flightoptimizer.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.optaplanner.persistence.jackson.impl.domain.solution.JacksonSolutionFileIO;

public class LandingPlanSolutionFileIO extends JacksonSolutionFileIO<LandingPlan> {

    public LandingPlanSolutionFileIO() {
        super(LandingPlan.class, new ObjectMapper().findAndRegisterModules());
    }
}

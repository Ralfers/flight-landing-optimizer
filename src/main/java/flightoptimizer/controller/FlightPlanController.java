package flightoptimizer.controller;

import flightoptimizer.domain.Landing;
import flightoptimizer.domain.LandingPlan;
import flightoptimizer.domain.Plane;
import flightoptimizer.domain.Runway;
import lombok.extern.slf4j.Slf4j;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/landing-plan")
public class FlightPlanController {

    @Autowired
    private SolverManager<LandingPlan, UUID> solverManager;

    @PostMapping("/solve")
    @ResponseBody
    public LandingPlan solve(@RequestBody LandingPlan problem) throws Exception {
        UUID id = UUID.randomUUID();

        printLandingPlan(problem);

        return solverManager.solve(id, problem).getFinalBestSolution();
    }

    public void printLandingPlan(LandingPlan landingPlan) {
        log.info("Runway count: {}", landingPlan.getRunwayList().size());
        log.info("Plane count: {}", landingPlan.getPlaneList().size());
        for (Runway runway : landingPlan.getRunwayList()) {
            printRunway(runway);
        }
        for (Plane plane : landingPlan.getPlaneList()) {
            printPlane(plane);
        }
    }

    public void printPlane(Plane plane) {
        log.info("Plane: {} {} {} {} {} {} {} {}", plane.getId(), plane.getArrivalTime(), plane.getEarliestLandingTime(),
                plane.getLatestLandingTime(), plane.getTargetLandingTime(), plane.getPenaltyForUnderTarget(),
                plane.getPenaltyForOverTarget(), plane.getSeparationTimes());
    }

    public void printRunway(Runway runway) {
        log.info("Runway: {}", runway.getId());
    }
}

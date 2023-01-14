package flightoptimizer.controller;

import flightoptimizer.domain.LandingPlan;
import flightoptimizer.domain.Plane;
import flightoptimizer.domain.Runway;
import lombok.extern.slf4j.Slf4j;
import org.optaplanner.core.api.score.ScoreManager;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.constraint.Indictment;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/landing-plan")
public class LandingPlanController {

    @Autowired
    private SolverManager<LandingPlan, UUID> solverManager;
    @Autowired
    private ScoreManager<LandingPlan, HardSoftScore> scoreManager;

    private final Map<UUID, LandingPlan> solutionMap = new HashMap<>();

    @PostMapping("/solve")
    @ResponseBody
    public UUID solve(@RequestBody LandingPlan problem) throws Exception {
        UUID uuid = UUID.randomUUID();
        solverManager.solveAndListen(uuid, id -> problem, landingPlan -> {
            log.info("Found a new best solution for problem {} with score: {}", uuid, landingPlan.getScore());
            solutionMap.put(uuid, landingPlan);
        });

        return uuid;
    }

    @GetMapping("/solution/{uuid}")
    @ResponseBody
    public LandingPlan getSolution(@PathVariable(value = "uuid") UUID uuid) throws Exception {
        return solutionMap.get(uuid);
    }

    @GetMapping("/solution/{uuid}/explanation")
    @ResponseBody
    public Map<Object, Indictment<HardSoftScore>> getSolutionExplanation(@PathVariable(value = "uuid") UUID uuid) throws Exception {
        LandingPlan landingPlan = solutionMap.get(uuid);
        if (landingPlan == null) {
            return null;
        }
        return scoreManager.explainScore(landingPlan).getIndictmentMap();
    }
}

package flightoptimizer;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BenchmarkApplication {

    private static final Logger log = LoggerFactory.getLogger(BenchmarkApplication.class);

    public static void main(String[] args) {
        log.debug("Benchmark starting");

        PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromXmlResource("benchmark-config.xml");
        PlannerBenchmark benchmark = benchmarkFactory.buildPlannerBenchmark();
        benchmark.benchmarkAndShowReportInBrowser();
    }
}

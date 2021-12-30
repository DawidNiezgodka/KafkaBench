import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import kafka.KafkaService;
import workload.WorkloadConfig;
import workload.WorkloadGenerator;

import java.io.File;
import java.io.IOException;

public class Benchmark {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    public static class Args {
        @Parameter(
                names = {"--kafka_bench_setting", "-kbs"},
                description = "a file that contains the description of setting of various kafka components",
                required = true)
        public String kafkaBenchSetting;

        @Parameter(
                names = { "--workload", "-w" },
                description = "a file with the description of a workload for stressing the SUT",
                required = true)
        public String workload;
    }


    public static void main(String[] args) {
        Args arguments = new Args();
        JCommander jCommander = new JCommander(arguments);

        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            e.printStackTrace();
        }

        /*
        First step: initialize kafka benchmark service that provides functionalities
        to create topics, consumers, producers according to the properties specified
        in the file, which was provided as an argument to the programm.
         */
        File kafkaBenchSetting = new File(arguments.kafkaBenchSetting);
        KafkaService kafkaService = new KafkaService(kafkaBenchSetting);

        /*
        Second step: Get workload from the file
         */

        try {
            File workloadFile = new File(arguments.workload);
            WorkloadConfig workloadConfig = MAPPER.readValue(workloadFile, WorkloadConfig.class);
            WorkloadGenerator workloadGenerator = new WorkloadGenerator(
                    workloadConfig,
                    kafkaService
            );
            workloadGenerator.runBenchmark();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

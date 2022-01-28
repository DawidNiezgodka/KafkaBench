package util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BenchConsts {

    private BenchConsts() {}

    public static List<String> RELEVANT_PRODUCER_METRICS = new ArrayList<>();
    static {
        //RELEVANT_PRODUCER_METRICS.add("incoming-byte-total");
        //RELEVANT_PRODUCER_METRICS.add("incoming-byte-rate");
        //RELEVANT_PRODUCER_METRICS.add("outgoing-byte-rate");
        RELEVANT_PRODUCER_METRICS.add("request-latency-avg");
        RELEVANT_PRODUCER_METRICS.add("request-latency-max");
        //RELEVANT_PRODUCER_METRICS.add("request-size-max");
        //RELEVANT_PRODUCER_METRICS.add("request-size-avg");
        //RELEVANT_PRODUCER_METRICS.add("record-size-avg");
        //RELEVANT_PRODUCER_METRICS.add("record-size-max");
        RELEVANT_PRODUCER_METRICS.add("batch-size-avg");
        //RELEVANT_PRODUCER_METRICS.add("record-retry-total");
        //RELEVANT_PRODUCER_METRICS.add("batch-size-max");
        //RELEVANT_PRODUCER_METRICS.add("record-error-total");
        //RELEVANT_PRODUCER_METRICS.add("record-error-rate");
        //RELEVANT_PRODUCER_METRICS.add("record-retry-rate");
        RELEVANT_PRODUCER_METRICS.add("record-send-total");
        //RELEVANT_PRODUCER_METRICS.add("records-per-request-avg");
        RELEVANT_PRODUCER_METRICS.add("record-send-rate");
    }

    public static final String PRODUCER_METRICS = "producer-metrics";
    public static final String STRING_1024 = "hksgdcxvelqglcrqdtqceyllecwydrrbxvylcxckitghkmpqqapgghujlsvwykhyofqekgdlnauhishsufcjamdbqbyehhhlctyetkrcvmdxatkovxtajauowvwgfhvobltsvkgunkifeowtlrccrpgyrxwunvefyduenjdvivcfofqifogbdexbcmrlgugfwqjfsorjdflpysvvkmlmupajqatrjiylgujsushngybpbpyfjrcvcijltseygyxohrcsuemlipbvjpsbearwwyhwapclfmmnihdvieeusbkmcddpwaorqahienfgjlvsiivxfqeasndyhpeaagrsxvfxxoqsyrsfgobkagiexxwalpfblqranjikebkmorasugnubwwvvdjfitchhynfoilomkomawpupcvvnvqaskkonbhymstdwcpltwnnxlgyfgyuaanalhpxwiqdgrvmvmcevreqxsfqapbnxmuvdlvoqmvjxueqbjlncwignkmbsdrhiwqieuyimhcgprysypcveexgtqyietglighqpcuyhmuagunvykgimipyfudprlpsajivjdiajwlxsjerfxcolpmvdjqxogufooxacnbslquhtaoqbaqghlmbhcaapqoplnpdmnpgevyblmkjtcpbfqccerhleecmwiyncdtnrsxwyellhxrwgssueqhekjmejjqoilrasthrfiagaupvagsgllessjxfmewhnwsefkfbanqxqiwlafbuydonqvfruqlepbiyssiljvwixkhcvsgbkjmrfnntvimyedhvnnsxkoscqoehnexjgmmloxlelolkwlkdpehdquvhdmucduyxyhxtfqimpdrokwoufpvjyohymfppnqdcnrmvjyohcsulciqpiexrspsqrenbstifdwudahncbkvjwcmassqgmksdllaowfulxfgqxcdewufvixvepjbnxknvknpqgyrscmjnijpiakmonlgdjbywkkbmiahujharofii";
    public static final short SEED = 42;
    public static final short DEFAULT_TOPIC_LEN = 25;
    public static final short DEFAULT_INITIAL_DELAY_OF_PRODUCERS = 0;
    public static final TimeUnit BENCH_DURATION_UNIT = TimeUnit.MINUTES;
    public static final TimeUnit PRODUCER_RATE_UNIT = TimeUnit.SECONDS;
    public static int NANOS_IN_SECOND = 1000000;
    public static final long EXTRA_SHUTDOWN_TIME = 2;

    public static final String RESULTS_FOLDER_RELATIVE = "./testFolder";
    public static final String RESULTS_FILE = "/results.csv";

}

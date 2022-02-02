package util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BenchConsts {

    private BenchConsts() {}

    public static List<String> RELEVANT_PRODUCER_METRICS = new ArrayList<>();
    public static List<String> SUMMED_PRODUCER_METRICS = new ArrayList<>();
    public static List<String> AVERAGED_PRODUCER_METRICS = new ArrayList<>();
    static {
        // all relevant metrics
        RELEVANT_PRODUCER_METRICS.add("request-latency-avg");
        RELEVANT_PRODUCER_METRICS.add("request-latency-max");
        RELEVANT_PRODUCER_METRICS.add("record-queue-time-avg");
        RELEVANT_PRODUCER_METRICS.add("record-queue-time-max");
        RELEVANT_PRODUCER_METRICS.add("record-size-avg");
        RELEVANT_PRODUCER_METRICS.add("record-size-max");
        RELEVANT_PRODUCER_METRICS.add("batch-size-avg");
        RELEVANT_PRODUCER_METRICS.add("batch-size-max");
        RELEVANT_PRODUCER_METRICS.add("record-send-total");
        RELEVANT_PRODUCER_METRICS.add("records-per-request-avg");
        RELEVANT_PRODUCER_METRICS.add("record-send-rate");

        // the following part of the relevant metrics should be summed
        SUMMED_PRODUCER_METRICS.add("record-send-rate");
        SUMMED_PRODUCER_METRICS.add("record-send-total");

        // and these should be averaged
        AVERAGED_PRODUCER_METRICS.add("request-latency-avg");
        AVERAGED_PRODUCER_METRICS.add("request-latency-max");
        AVERAGED_PRODUCER_METRICS.add("record-size-avg");
        AVERAGED_PRODUCER_METRICS.add("record-size-max");
        AVERAGED_PRODUCER_METRICS.add("batch-size-avg");
        AVERAGED_PRODUCER_METRICS.add("batch-size-max");
        AVERAGED_PRODUCER_METRICS.add("records-per-request-avg");
        AVERAGED_PRODUCER_METRICS.add("record-queue-time-avg");
        AVERAGED_PRODUCER_METRICS.add("record-queue-time-max");
    }



    public static final String PRODUCER_METRICS = "producer-metrics";
    public static final String STRING_1024 = "hksgdcxvelqglcrqdtqceyllecwydrrbxvylcxckitghkmpqqapgghujlsvwykhyofqekgdlna" +
            "uhishsufcjamdbqbyehhhlctyetkrcvmdxatkovxtajauowvwgfhvobltsvkgunkifeowtlrccrpgyrxwunvefyduenjdvivcfofqifogbd" +
            "exbcmrlgugfwqjfsorjdflpysvvkmlmupajqatrjiylgujsushngybpbpyfjrcvcijltseygyxohrcsuemlipbvjpsbearwwyhwapclfmmni" +
            "hdvieeusbkmcddpwaorqahienfgjlvsiivxfqeasndyhpeaagrsxvfxxoqsyrsfgobkagiexxwalpfblqranjikebkmorasugnubwwvvdjfi" +
            "tchhynfoilomkomawpupcvvnvqaskkonbhymstdwcpltwnnxlgyfgyuaanalhpxwiqdgrvmvmcevreqxsfqapbnxmuvdlvoqmvjxueqbjlncw" +
            "ignkmbsdrhiwqieuyimhcgprysypcveexgtqyietglighqpcuyhmuagunvykgimipyfudprlpsajivjdiajwlxsjerfxcolpmvdjqxogufo" +
            "oxacnbslquhtaoqbaqghlmbhcaapqoplnpdmnpgevyblmkjtcpbfqccerhleecmwiyncdtnrsxwyellhxrwgssueqhekjmejjqoilrasthr" +
            "fiagaupvagsgllessjxfmewhnwsefkfbanqxqiwlafbuydonqvfruqlepbiyssiljvwixkhcvsgbkjmrfnntvimyedhvnnsxkoscqoehnexjg" +
            "mmloxlelolkwlkdpehdquvhdmucduyxyhxtfqimpdrokwoufpvjyohymfppnqdcnrmvjyohcsulciqpiexrspsqrenbstifdwudahncbkvjwc" +
            "massqgmksdllaowfulxfgqxcdewufvixvepjbnxknvknpqgyrscmjnijpiakmonlgdjbywkkbmiahujharofii";

    public static final short DEFAULT_TOPIC_LEN = 25;
    public static final short DEFAULT_INITIAL_DELAY_OF_PRODUCERS = 0;
    public static final TimeUnit BENCH_DURATION_UNIT = TimeUnit.MINUTES;
    public static int NANOS_IN_SECOND = 1000000;

    public static final String RESULTS_FOLDER_RELATIVE = "./testFolder";
    public static final String RESULTS_FILE = "/results.csv";

}

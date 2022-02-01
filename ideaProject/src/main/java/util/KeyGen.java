package util;

import java.util.Base64;
import java.util.Random;

public class KeyGen {

    private static final int NUMBER_OF_UNIQUE_KEYS = 100;
    private static final int KEY_SIZE = 5;
    private static final Random RAND = new Random();
    private static final String[] KEYS = new String[NUMBER_OF_UNIQUE_KEYS];
    private static final Base64.Encoder ENCODER = Base64.getEncoder().withoutPadding();

    private static int currentIndex = 0;

    static {
        byte[] buffer = new byte[KEY_SIZE];
        for (int i = 0; i < KEYS.length; i++) {
            RAND.nextBytes(buffer);
            KEYS[i] = ENCODER.encodeToString(buffer);
        }
    }

    // round robin
    public static String get() {
        if (++currentIndex >= NUMBER_OF_UNIQUE_KEYS) {
            currentIndex = 0;
        }
        return KEYS[currentIndex];
    }
}

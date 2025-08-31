package mission.utility;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class UUIDv7 {
    private UUIDv7() {}

    /** Draft/RFC 규격에 맞춘 간단 v7 생성기 (48b epoch ms + 12b rand + 62b rand) */
    public static UUID random() {
        long ms = System.currentTimeMillis();              // 48비트 timestamp (ms)
        long randA = ThreadLocalRandom.current().nextLong(1L << 12); // 12비트

        // MSB: [48b ts][4b ver=0111][12b randA]
        long msb = (ms & 0x0000FFFFFFFFFFFFL) << 16;       // 상위 48비트에 ts
        msb |= 0x7000L;                                    // version 7
        msb |= (randA & 0xFFFL);                           // 12비트 rand

        // LSB: variant(10xx...) + 62b randB
        long randB = ThreadLocalRandom.current().nextLong();
        long lsb = (randB & 0x3FFFFFFFFFFFFFFFL) | 0x8000000000000000L; // variant=RFC 4122

        return new UUID(msb, lsb);
    }
}

package fun.LSDog.CMIAFK;

public class CMIAFKUtils {

    public static long now() {
        return System.currentTimeMillis();
    }

    public static long nextSec(long timeStamp) {
        long rem = timeStamp % 1000;
        return (rem != 0) ? (timeStamp - rem + 1000) : (timeStamp);
    }

    public static String getHms(long timePeriod) {
        long h = timePeriod/3600000;
        timePeriod %= 3600000;
        int m = (int) (timePeriod/60000);
        timePeriod %= 60000;
        int s = (int) (timePeriod/1000);
        return h + "h " + m + "m " + s + "s";
    }

}

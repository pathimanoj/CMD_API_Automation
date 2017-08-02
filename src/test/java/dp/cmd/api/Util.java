package dp.cmd.api;
import java.util.Random;

public class Util {

    static Random random = new Random();

    public static int getRandomInt(int lengthToBuild) {
        int value = 0;
        try {
            value = random.nextInt(lengthToBuild);
        } catch (Exception ee) {
        }
        return value;
    }
}

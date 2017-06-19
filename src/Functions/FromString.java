package Functions;

/**
 * Created by anka on 24.05.17.
 */
public class FromString {
    public static int[] fromStringToInt(String string){
        String[] origin = string.split(";");
        double first = Double.parseDouble(origin[0]);
        double second = Double.parseDouble(origin[1]);
        int tab[] = {(int) first, (int) second};
        return tab;
    }

    public static String[] fromStringToString(String string){
        String[] origin = string.split(";");
        return origin;
    }
}

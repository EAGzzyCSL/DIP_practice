package my;


import java.util.Arrays;

public class MyUtils {
    public static int toRGBInt(int r, int g, int b) {
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;
        return 0xFF000000 | r | g | b;
    }
    public static int toRGBInt(int g){
        return toRGBInt(g,g,g);
    }

    public static double getAverage(int[] arr) {
        return getSum(arr) / (float) arr.length;
    }

    public static int getSum(int[] arr) {
        int sum = 0;
        for (int a : arr) {
            sum += a;
        }
        return sum;
    }

    public static double getStandardDeviation(int[] arr) {
        double avg = getAverage(arr);
        double variance = 0;
        for (int a : arr) {
            variance += square(a - avg);
        }
        return Math.sqrt(variance/arr.length);
    }

    public static int getMedian(int[] arr) {
        int[] arrCopy = arr.clone();
        Arrays.sort(arrCopy);
        if (arrCopy.length % 2 == 0) {
            return (arrCopy[arrCopy.length / 2] +
                    arrCopy[arrCopy.length / 2 - 1]) / 2;
        } else {
            return arrCopy[arrCopy.length / 2];
        }
    }

    private static double square(double a) {
        return a * a;
    }

    public static double degreeToRadian(double degree){
        return degree/180*Math.PI;
    }
}
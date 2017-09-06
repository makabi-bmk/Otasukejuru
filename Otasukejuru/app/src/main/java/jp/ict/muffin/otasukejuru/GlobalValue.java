package jp.ict.muffin.otasukejuru;

/**
 * Created by mito on 2017/08/26.
 */

public class GlobalValue {
    private static int displayWidth = 0;
    private static int displayHeight = 0;
    private static boolean timerFlag = false;

    public static int getDisplayWidth() {
        return displayWidth;
    }
    public static int getDisplayHeight() {
        return displayHeight;
    }
    public static boolean getTimerFlag() {
        return timerFlag;
    }

    public static void setDisplayWidth(int width) {
        displayWidth = width;
    }
    public static void setDisplayHeight(int height) {
        displayHeight = height;
    }
    public static void setTimerFlag(Boolean flag) {
        timerFlag = flag;
    }
}

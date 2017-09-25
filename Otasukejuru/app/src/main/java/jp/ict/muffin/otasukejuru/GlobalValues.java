package jp.ict.muffin.otasukejuru;


public class GlobalValues {
    private static int displayWidth = 0;
    private static int DisplayHeight = 0;
    private static boolean timerFlag = false;


    static void setDisplayWidth(int displayWidth) {
        GlobalValues.displayWidth = displayWidth;
    }

    static void setDisplayHeight(int displayHeight) {
        DisplayHeight = displayHeight;
    }

    public static void setTimerFlag(boolean timerFlag) {
        GlobalValues.timerFlag = timerFlag;
    }

    public static int getDisplayWidth() {

        return displayWidth;
    }

    public static int getDisplayHeight() {
        return DisplayHeight;
    }

    public static boolean isTimerFlag() {
        return timerFlag;
    }
}

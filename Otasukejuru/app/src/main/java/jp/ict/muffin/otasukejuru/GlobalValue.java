package jp.ict.muffin.otasukejuru;

/**
 * Created by mito on 2017/08/26.
 */

public class GlobalValue {
    private static int displayWidth;
    private static int displayHeight;

    public int getDisplayWidth() {
        return displayWidth;
    }
    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayWidth(int width) {
        displayWidth = width;
    }
    public void setDisplayHeight(int height) {
        displayHeight = height;
    }
}

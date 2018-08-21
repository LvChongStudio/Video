package com.lvstudio.video.utils;

/**
 * @author gaozhaoya
 * @date 2018/3/7
 */

public class FormatUtil {

    /**
     * format Int Time  传入秒
     *
     * @param time time in miles like 3829
     * @return 35:09
     */
    public static String formatIntTime(int time) {
        int h = (int) (time / 60f / 60);
        int m = (int) (time / 60f % 60);
        int s = (int) (time % 60f);
        String M = ((m < 10) ? ("0" + m) : String.valueOf(m));
        String S = ((s < 10) ? ("0" + s) : String.valueOf(s));
        String ft = ((h > 0) ? (h + ":" + M + ":" + S) : (M + ":" + S));
        return ft;
    }
}

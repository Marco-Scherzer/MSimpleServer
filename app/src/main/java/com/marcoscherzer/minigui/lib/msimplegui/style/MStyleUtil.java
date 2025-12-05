package com.marcoscherzer.minigui.lib.msimplegui.style;

import android.graphics.Color;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MStyleUtil {
    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private MStyleUtil() {
        // Utility class — keine Instanz erlaubt
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static int rgb(int r, int g, int b) {
        return Color.rgb(r, g, b);
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static int argb(int a, int r, int g, int b) {
        return Color.argb(a, r, g, b);
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static int rgba(int r, int g, int b, float alpha) {
        return Color.argb((int) (alpha * 255), r, g, b);
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static float clampFloat(float value) {
        return Math.max(0f, Math.min(1f, value));
    }

    /**
     * @param baseColor Ausgangsfarbe (ARGB)
     * @param percent   Prozentuale Änderung der Helligkeit (+10 = heller, -10 = dunkler)
     * @return Neue Farbe mit angepasster Brightness
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * JavaFX-ähnliche derive-Methode: verändert die Brightness im HSV-Farbraum.
     */
    public static int derive(int baseColor, float percent) {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        hsv[2] = clampFloat(hsv[2] + (percent / 100f));
        return Color.HSVToColor(hsv);
    }

    /**
     * JavaFX-ähnliche ladder-Methode:
     *
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static int ladder(int baseColor, int... thresholds) {
        if (thresholds.length == 0) return baseColor;
        int avg = 0;
        for (int t : thresholds) avg += t;
        avg /= thresholds.length;
        return derive(baseColor, avg - 50);
    }
}

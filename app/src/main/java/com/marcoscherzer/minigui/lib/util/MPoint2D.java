package com.marcoscherzer.minigui.lib.util;


/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, All rights reserved
 */
public final class MPoint2D {
    private final double x;
    private final double y;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, All rights reserved
     */
    public MPoint2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, All rights reserved
     */
    public String toString() {
        return this.getX() + ", " + this.getY();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, All rights reserved
     */
    public double getX() {
        return x;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, All rights reserved
     */
    public double getY() {
        return y;
    }

}

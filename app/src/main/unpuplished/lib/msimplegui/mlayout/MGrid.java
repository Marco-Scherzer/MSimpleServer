package com.marcoscherzer.minigui.lib.msimplegui.mlayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * MGridBuilder&MGridBuilder-API, Copyright Marco Scherzer Closed Source Development, All Rights Reserved
 * MGridBuilder-JavaFX Version, Copyright Marco Scherzer Closed Source Development, All Rights Reserved
 *
 * @version 0.0.1 preAlpha MGridBuilder-Android UI Version(Port-Try via Microsoft Copilot),
 * Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MGrid extends ConstraintLayout {
    private FrameLayout[][] cellMatrix;
    //private ConstraintLayout layout;

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    public MGrid(Context context) {
        super(context);
    }

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    public void setMatrix(FrameLayout[][] matrix) {
        this.cellMatrix = matrix;
    }


    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    public <T extends View> T getContent(int line, int column) {
        FrameLayout wrapper = cellMatrix[line][column];
        return (T) wrapper.getChildAt(0); //FrameLayout.View
    }

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    public void setContent(int line, int column, View content) {
        FrameLayout wrapper = cellMatrix[line][column];
        wrapper.removeAllViews();
        wrapper.addView(content);
    }

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    public int getLineCnt() {
        return cellMatrix.length;
    }

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    public int getColumnCnt() {
        return cellMatrix[0].length;
    }

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    public void setCellBackground(int line, int column, MBorderDrawableBuilder style) {
        FrameLayout wrapper = cellMatrix[line][column];
        MBorderDrawableBuilder.styleViewWithBackground(wrapper, style, wrapper.getPaddingTop(), wrapper.getPaddingLeft(), wrapper.getPaddingBottom(), wrapper.getPaddingRight());
    }

    public void setCellBackground(int line, int column, Drawable background) {
        cellMatrix[line][column].setBackground(background);
    }

    public void setCellBackgroundColor(int line, int column, int color) {
        cellMatrix[line][column].setBackgroundColor(color);
    }

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    public void setLineBackgrounds(int line, MBorderDrawableBuilder style) {
        for (int column = 0; column < getColumnCnt(); column++) {
            FrameLayout wrapper = cellMatrix[line][column];
            MBorderDrawableBuilder.styleViewWithBackground(
                    wrapper,
                    style,
                    wrapper.getPaddingTop(),
                    wrapper.getPaddingLeft(),
                    wrapper.getPaddingBottom(),
                    wrapper.getPaddingRight()
            );
        }
    }

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    public void setColumnBackgrounds(int column, MBorderDrawableBuilder style) {
        for (int line = 0; line < getLineCnt(); line++) {
            FrameLayout wrapper = cellMatrix[line][column];
            MBorderDrawableBuilder.styleViewWithBackground(
                    wrapper,
                    style,
                    wrapper.getPaddingTop(),
                    wrapper.getPaddingLeft(),
                    wrapper.getPaddingBottom(),
                    wrapper.getPaddingRight()
            );
        }
    }


    public void setLineBackgroundColors(int line, int color) {
        for (int column = 0; column < getColumnCnt(); column++) {
            cellMatrix[line][column].setBackgroundColor(color);
        }
    }

    public void setColumnBackgroundColors(int column, int color) {
        for (int line = 0; line < getLineCnt(); line++) {
            cellMatrix[line][column].setBackgroundColor(color);
        }
    }


    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    public void setAllCellBackgrounds(MBorderDrawableBuilder style) {
        for (int line = 0; line < cellMatrix.length; line++) {
            for (int column = 0; column < cellMatrix[line].length; column++) {
                FrameLayout wrapper = cellMatrix[line][column];
                MBorderDrawableBuilder.styleViewWithBackground(wrapper, style, wrapper.getPaddingTop(), wrapper.getPaddingLeft(), wrapper.getPaddingBottom(), wrapper.getPaddingRight());
            }
        }
    }


    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    //@Override
    public void setBackgroundColor(int color) {
        //System.out.println("MGrid.setBackgroundColor");
        super.setBackgroundColor(color);
    }

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    //@Override
    public void setBackground(Drawable background) {
        //System.out.println("MGrid.setBackground");
        super.setBackground(background);
    }

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    public void setAllCellBackgroundColors(int color) {
        for (int line = 0; line < cellMatrix.length; line++) {
            for (int column = 0; column < cellMatrix[line].length; column++) {
                cellMatrix[0][0].setBackgroundColor(color);
            }
        }
    }

}





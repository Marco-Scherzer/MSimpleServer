package com.marcoscherzer.minigui.appstyle;

import static com.marcoscherzer.minigui.lib.msimplegui.style.MStyleUtil.rgba;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.RippleDrawable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.marcoscherzer.minigui.MMenuBar;
import com.marcoscherzer.minigui.lib.msimplegui.mlayout.MBorderDrawableBuilder;
import com.marcoscherzer.minigui.lib.msimplegui.mlayout.MBorderDrawableBuilder.BorderEdge;
import com.marcoscherzer.minigui.lib.msimplegui.mlayout.MGrid;
import com.marcoscherzer.minigui.lib.msimplegui.style.MComponentStyler;
import com.marcoscherzer.msimpleserver.R;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MMenuBarStyler implements MComponentStyler<MMenuBar, MStyleRoot> {

    @Override
    public void styleComponent(MMenuBar entry, MStyleRoot m) {
        entry.setBackgroundColor(rgba(0, 50, 120, 1.0f));
        entry.styleUseOnly_getRootGrid().setBackgroundColor(rgba(0, 50, 120, 1.0f));
        MGrid grid = entry.styleUseOnly_getRootGrid();

        int orange = rgba(255, 100, 0, 1.0f);
        int base = rgba(0, 50, 120, 1.0f);

        MBorderDrawableBuilder cellStyle = new MBorderDrawableBuilder()
                .setFillColor(base)
                //.setCornerRadius(5f)
                //.setStroke(orange, 1f, BorderEdge.BOTTOM);
                .setStroke(rgba(0, 122, 255, 1.0f), 1f, BorderEdge.BOTTOM);
        // Setze gesamte Menüzeile (erste Zeile) mit zellspezifischem Hintergrund
        grid.setLineBackgrounds(0, cellStyle);


        styleButton(entry.styleUseOnly_leftMenuButton(), R.drawable.baseline_menu_24);
        styleButton(entry.styleUseOnly_newAppointmentButton(), R.drawable.baseline_calendar_month_24);
        styleButton(entry.styleUseOnly_newAppointmentButton2(), R.drawable.baseline_punch_clock_24);
        styleButton(entry.styleUseOnly_getVoiceNoteButton(), R.drawable.baseline_mic_24);

    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void styleButton(MaterialButton button, int iconResource) {
        button.setIconResource(iconResource);
        button.setText("");
        button.setIconPadding(0);
        button.setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_START);
        button.setIconTint(ColorStateList.valueOf(rgba(255, 100, 0, 0.7f))); // Orange Akzent
        button.setTextColor(Color.WHITE);
        button.setPadding(0, 0, 0, 0);

        float cornerRadius = 5f;
        ColorStateList rippleColor = ColorStateList.valueOf(rgba(255, 100, 0, 0.2f));

        ShapeAppearanceModel shapeModel = new ShapeAppearanceModel.Builder()
                .setAllCornerSizes(cornerRadius)
                .build();

        MaterialShapeDrawable backgroundDrawable = new MaterialShapeDrawable(shapeModel);
        backgroundDrawable.setFillColor(new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}
                },
                new int[]{
                        rgba(0, 90, 190, 1.0f),   // dunkler Stard Gluon beim Drücken
                        rgba(0, 50, 120, 1.0f)   // Stard Gluon normal
                }
        ));
        backgroundDrawable.setShadowCompatibilityMode(MaterialShapeDrawable.SHADOW_COMPAT_MODE_NEVER);
        backgroundDrawable.setElevation(0f);
        backgroundDrawable.setTintMode(PorterDuff.Mode.SRC_OVER);

        RippleDrawable rippleDrawable = new RippleDrawable(
                rippleColor,
                backgroundDrawable,
                null
        );

        button.setBackground(rippleDrawable);

        StateListAnimator animator = new StateListAnimator();
        animator.addState(
                new int[]{android.R.attr.state_pressed},
                ObjectAnimator.ofFloat(button, "translationZ", -2f)
        );
        animator.addState(
                new int[]{},
                ObjectAnimator.ofFloat(button, "translationZ", 12f)
        );
        button.setStateListAnimator(animator);

        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN:
                    v.setScaleX(0.90f);
                    v.setScaleY(0.90f);
                    break;
                case android.view.MotionEvent.ACTION_UP:
                case android.view.MotionEvent.ACTION_CANCEL:
                    v.setScaleX(1f);
                    v.setScaleY(1f);
                    break;
            }
            return false;
        });
    }
}





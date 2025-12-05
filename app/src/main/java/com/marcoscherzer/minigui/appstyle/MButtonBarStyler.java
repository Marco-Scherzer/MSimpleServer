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
import com.marcoscherzer.minigui.MButtonBar;
import com.marcoscherzer.minigui.lib.msimplegui.style.MComponentStyler;
import com.marcoscherzer.msimpleserver.R;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MButtonBarStyler implements MComponentStyler<MButtonBar, MStyleRoot> {
    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public void styleComponent(MButtonBar entry, MStyleRoot m) {
        //entry.setBackgroundColor(entry.styleUseOnly_styleRuleForUseInsideList()?MSECTIONSGRID_BACKGROUND_COLOR_EVEN:MSECTIONSGRID_BACKGROUND_COLOR_ODD);
        entry.setBackgroundColor(Color.TRANSPARENT);
        styleButton(entry.styleUseOnly_getStartButton(), R.drawable.baseline_play_arrow_24);
        styleButton(entry.styleUseOnly_getPauseButton(), R.drawable.baseline_pause_24);
        styleButton(entry.styleUseOnly_getDoneButton(), R.drawable.baseline_check_24);
        styleButton(entry.styleUseOnly_getCallButton(), R.drawable.baseline_call_24);
        styleButton(entry.styleUseOnly_getInfoButton(), R.drawable.baseline_info_24);
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void styleButton(MaterialButton button, int iconResource) {
        button.setIconResource(iconResource);
        button.setText("");
        button.setIconPadding(0);
        button.setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_START);
        button.setIconTint(ColorStateList.valueOf(rgba(255, 100, 0, 0.7f))); // Orange Akzent
        button.setTextColor(Color.WHITE);
        button.setPadding(0, 0, 0, 0);

        int strokeWidth = 4;
        float cornerRadius = 5f;

        // Blauer Rand (Stard Gluon) im Normalzustand, Orange beim Drücken
        ColorStateList strokeColor = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}
                },
                new int[]{
                        rgba(255, 100, 0, 1.0f), // Orange beim Drücken
                        rgba(0, 122, 255, 1.0f)  // Stard Gluon Blau als Rand
                }
        );

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
                        //rgba(0, 122, 255, 1.0f)
                        rgba(0, 50, 120, 1.0f)
                        //TEXT_MID_COLOR
                        // Stard Gluon normal
                }
        ));
        backgroundDrawable.setStrokeColor(strokeColor);
        backgroundDrawable.setStrokeWidth(strokeWidth);
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




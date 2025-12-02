package com.marcoscherzer.minigui;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.marcoscherzer.minigui.lib.msimplegui.mlayout.MGrid;
import com.marcoscherzer.minigui.lib.msimplegui.mlayout.MGridBuilder;
import com.marcoscherzer.minigui.lib.msimplegui.style.MStyleable;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MButtonBar extends LinearLayout implements MStyleable {

    private final MaterialButton startButton;
    private final MaterialButton pauseButton;
    private final MaterialButton doneButton;
    private final MaterialButton callButton;
    private final MaterialButton infoButton;


    private final MGrid rootGrid;
    private boolean styleRuleForUseInsideList_colorEven = true;
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Runnable doneButtonPressedHandler;
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Runnable startButtonPressedHandler;
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Runnable pauseButtonPressedHandler;
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Runnable callButtonPressedHandler;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MButtonBar(Context context) {
        super(context);
        startButton = new MaterialButton(context);//new Icon(MaterialDesignIcon.PLAY_ARROW)
        pauseButton = new MaterialButton(context);//new Icon(MaterialDesignIcon.PAUSE)
        doneButton = new MaterialButton(context);//new Icon(MaterialDesignIcon.DONE)
        callButton = new MaterialButton(context);//new Icon(MaterialDesignIcon.PHONE)
        infoButton = new MaterialButton(context);// new Icon(MaterialDesignIcon.INFO));


        MGridBuilder rootG = new MGridBuilder(context);
        rootG.setColumnWidths(0.2f, 0.2f, 0.2f, 0.2f, 0.2f);
        rootG.addLine(1.0f)
                .addWithPaddings(startButton, 3, 6, 7, 3)
                .addWithPaddings(pauseButton, 3, 3, 7, 3)
                .addWithPaddings(doneButton, 3, 3, 7, 3)
                .addWithPaddings(callButton, 3, 3, 7, 3)
                .addWithPaddings(infoButton, 3, 3, 7, 6);
        this.rootGrid = rootG.create();

        setAsMainComponent();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void styleUseOnly_styleRuleForUseInsideList(boolean colorEven) {
        this.styleRuleForUseInsideList_colorEven = colorEven;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public boolean styleUseOnly_styleRuleForUseInsideList() {
        return this.styleRuleForUseInsideList_colorEven;
    }

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    private void setAsMainComponent() {
        this.setOrientation(VERTICAL);

        View innerLayout = this.rootGrid;
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        this.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        this.addView(innerLayout);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MGrid styleUseOnly_getRootGrid() {
        return this.rootGrid;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MaterialButton styleUseOnly_getStartButton() {
        return this.startButton;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MaterialButton styleUseOnly_getPauseButton() {
        return this.pauseButton;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MaterialButton styleUseOnly_getDoneButton() {
        return this.doneButton;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MaterialButton styleUseOnly_getCallButton() {
        return this.callButton;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MaterialButton styleUseOnly_getInfoButton() {
        return this.infoButton;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void setOnDoneButtonPressed(Runnable handler) {
        doneButtonPressedHandler = handler;
        doneButton.setOnClickListener(v -> doneButtonPressedHandler.run());
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void setOnStartButtonPressedHandler(Runnable handler) {
        startButtonPressedHandler = handler;
        startButton.setOnClickListener(v -> startButtonPressedHandler.run());
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void setOnPauseButtonPressed(Runnable handler) {
        pauseButtonPressedHandler = handler;
        pauseButton.setOnClickListener(v -> pauseButtonPressedHandler.run());
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void setOnCallButtonPressed(Runnable handler) {
        callButtonPressedHandler = handler;
        callButton.setOnClickListener(v -> callButtonPressedHandler.run());
    }


}

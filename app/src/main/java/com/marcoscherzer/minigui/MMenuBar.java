package com.marcoscherzer.minigui;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.marcoscherzer.minigui.lib.msimplegui.mlayout.MGrid;
import com.marcoscherzer.minigui.lib.msimplegui.mlayout.MGridBuilder;
import com.marcoscherzer.minigui.lib.msimplegui.style.MStyleable;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MMenuBar extends LinearLayout implements MStyleable {

    private final MaterialButton leftMenuButton;
    private final MaterialButton newAppointmentButton2;
    private final MaterialButton newAppointmentButton;
    private final MaterialButton voiceNoteButton;
    private final TextView text;
    //private final MaterialButton infoButton;


    private final MGrid rootGrid;
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Runnable doneButtonPressedHandler;
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Runnable startButtonPressedHandler;
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Runnable pauseButtonPressedHandler;
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Runnable callButtonPressedHandler;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MMenuBar(Context context) {
        super(context);
        leftMenuButton = new MaterialButton(context);//new Icon(MaterialDesignIcon.PLAY_ARROW)

        text = new TextView(context);

        newAppointmentButton2 = new MaterialButton(context);//new Icon(MaterialDesignIcon.PAUSE)
        newAppointmentButton = new MaterialButton(context);//new Icon(MaterialDesignIcon.DONE)
        voiceNoteButton = new MaterialButton(context);//new Icon(MaterialDesignIcon.PHONE)


        MGridBuilder rootG = new MGridBuilder(context);
        rootG.setColumnWidths(0.2f, 0.2f, 0.2f, 0.2f, 0.2f);
        rootG.addLine(1.0f)
                .addWithPaddings(leftMenuButton, 1)
                .addWithPaddings(text, 1)
                .addWithPaddings(newAppointmentButton2, 1)
                .addWithPaddings(newAppointmentButton, 1)
                .addWithPaddings(voiceNoteButton, 1);
        //.addWithMargins(infoButton,1);

        this.rootGrid = rootG.create();

        setAsMainComponent();
    }

    /*** author Marco Scherzer, Copyright Marco Scherzer , All rights reserved */
    private final void setAsMainComponent() {
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
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MGrid styleUseOnly_getRootGrid() {
        return this.rootGrid;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MaterialButton styleUseOnly_leftMenuButton() {
        return this.leftMenuButton;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final TextView styleUseOnly_TextView() {
        return this.text;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MaterialButton styleUseOnly_newAppointmentButton2() {
        return this.newAppointmentButton2;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MaterialButton styleUseOnly_newAppointmentButton() {
        return this.newAppointmentButton;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MaterialButton styleUseOnly_getVoiceNoteButton() {
        return this.voiceNoteButton;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void setOnDoneButtonPressed(Runnable handler) {
        doneButtonPressedHandler = handler;
        newAppointmentButton.setOnClickListener(v -> doneButtonPressedHandler.run());
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void setOnStartButtonPressedHandler(Runnable handler) {
        startButtonPressedHandler = handler;
        leftMenuButton.setOnClickListener(v -> startButtonPressedHandler.run());
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void setOnPauseButtonPressed(Runnable handler) {
        pauseButtonPressedHandler = handler;
        newAppointmentButton2.setOnClickListener(v -> pauseButtonPressedHandler.run());
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void setOnCallButtonPressed(Runnable handler) {
        callButtonPressedHandler = handler;
        voiceNoteButton.setOnClickListener(v -> callButtonPressedHandler.run());
    }


}

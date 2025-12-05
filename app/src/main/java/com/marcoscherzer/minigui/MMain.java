package com.marcoscherzer.minigui;

import static com.marcoscherzer.minigui.appstyle.MStyleRoot.BASE;
import static com.marcoscherzer.minigui.lib.msimplegui.style.MStyleUtil.derive;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;
import com.marcoscherzer.minigui.appstyle.MButtonBarStyler;
import com.marcoscherzer.minigui.appstyle.MMenuBarStyler;
import com.marcoscherzer.minigui.appstyle.MStyleRoot;
import com.marcoscherzer.minigui.lib.msimplegui.mlayout.MGrid;
import com.marcoscherzer.minigui.lib.msimplegui.mlayout.MGridBuilder;
import com.marcoscherzer.minigui.lib.msimplegui.style.MStyler;
import com.marcoscherzer.msimpleserver.MSimpleMiniServer;


/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MMain extends Activity {

    private MStyleRoot m;
    private MaterialCardView viewCard;
    private MaterialCardView menuBarCard;
    private MSimpleMiniServer server;

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static MaterialCardView createMaterialCardOverlay(Context context, View view, float elevation) {
        MaterialCardView card = new MaterialCardView(context);
        card.setCardElevation(elevation);
        card.setRadius(0f);
        card.setUseCompatPadding(false);
        card.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        card.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        card.addView(view);
        return card;
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.setErr(System.out);
        System.out.println("onCreate");

        getWindow().setStatusBarColor(derive(BASE, 10));
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        m = new MStyleRoot();

        try {
            server = MySimpleServerConfig.createAndStartServerOnAndroid(this);


            MMenuBar menuBar = new MMenuBar(this);

            MGridBuilder G1 = new MGridBuilder(this);
            G1.setColumnWidths(1.0f);
            G1.addLine(0.1f).add(menuBar);
            G1.addLine(0.9f).add(new View(this));
            MGrid overlayGrid1 = G1.create();

            menuBarCard = createMaterialCardOverlay(this, overlayGrid1, 3f);

            // Container für beide Layer
          /*  FrameLayout layeredRoot = new FrameLayout(this);
            layeredRoot.setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            ));

            layeredRoot.addView(overlayGrid1);

            setContentView(layeredRoot);
*/
            // Styles
            MStyler.addUserStyle(MMenuBar.class, new MMenuBarStyler());
            MStyler.addUserStyle(MButtonBar.class, new MButtonBarStyler());
            //  MStyler.applyUserStyles(layeredRoot, m);


        } catch (Exception exc) {
            exc.printStackTrace();
        }

        //System.gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


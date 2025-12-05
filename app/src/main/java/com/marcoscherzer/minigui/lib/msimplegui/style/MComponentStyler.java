package com.marcoscherzer.minigui.lib.msimplegui.style;

import android.view.View;

import com.marcoscherzer.minigui.appstyle.MStyleRoot;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public interface MComponentStyler<ViewT extends View, MT extends MStyleRoot> {
    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    void styleComponent(ViewT view, MT m);
}


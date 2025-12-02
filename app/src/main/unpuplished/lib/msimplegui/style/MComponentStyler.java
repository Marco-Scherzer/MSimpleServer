package com.marcoscherzer.minigui.lib.msimplegui.style;

import android.view.View;

import com.marcoscherzer.minigui.appstyle.MStyleRoot;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public interface MComponentStyler<ViewT extends View, MT extends MStyleRoot> {

    void styleComponent(ViewT view, MT m);
}


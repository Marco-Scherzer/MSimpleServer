package com.marcoscherzer.minigui.lib.msimplegui.style;

import android.view.View;
import android.view.ViewGroup;

import com.marcoscherzer.minigui.appstyle.MStyleRoot;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MStyler {

    private static final Map<Class<? extends MStyleable>, MComponentStyler> userClass2StylerMap = new HashMap<>();
    private static Map<Class<? extends MStyleable>, MComponentStyler> defaultClass2StylerMap;
    private static Map<Class<? extends MStyleable>, MComponentStyler> currentMap;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private MStyler() {
        // Utility class — keine Instanz erlaubt
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Bisher leer
     */

    public static <MT extends MStyleRoot> void initializeDefaultComponentStyles() {

    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Bisher funktionslos
     */
    public static <MT extends MStyleRoot> void applyDefaultStyles(View root, MT m) {
        defaultClass2StylerMap = new HashMap<>();
        currentMap = defaultClass2StylerMap;
        initializeDefaultComponentStyles();
        applyStylesRecursive(root, m, 0, true);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void addUserStyle(Class<? extends MStyleable> theClass, MComponentStyler styler) {
        userClass2StylerMap.put(theClass, styler);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static <MT extends MStyleRoot> void applyUserStyles(View root, MT m) {
        currentMap = userClass2StylerMap;
        applyStylesRecursive(root, m, 0, true);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static <MT extends MStyleRoot> void applyStylesRecursive(View root, MT m, int depth, boolean isLast) {
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            prefix.append(i == depth - 1 ? (isLast ? "    " : "|   ") : "|   ");
        }
        prefix.append(isLast ? "└── " : "├── ");

        Class<?> rootClass = root.getClass();
        System.out.println(prefix + rootClass.getSimpleName());

        boolean styled = false;
        for (Map.Entry<Class<? extends MStyleable>, MComponentStyler> entry : currentMap.entrySet()) {
            Class<?> targetClass = entry.getKey();
            System.out.println(prefix + "searching for " + targetClass.getSimpleName() + " equals " + rootClass);

            boolean match = rootClass.equals(targetClass); // exakte Übereinstimmung

            if (match) {
                System.out.println(prefix + "→ styling with " + targetClass.getSimpleName());
                try {
                    entry.getValue().styleComponent(root, m);
                    styled = true;
                    break;
                } catch (ClassCastException e) {
                    System.out.println(prefix + "⚠ Styler cast failed for " + rootClass.getSimpleName());
                }
            } else {
                System.out.println(prefix + "not matched: " + targetClass.getSimpleName());
            }
        }


        if (!styled) {
            System.out.println(prefix + "↪  No matching styler");
        }

        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                boolean childIsLast = (i == count - 1);
                applyStylesRecursive(group.getChildAt(i), m, depth + 1, childIsLast);
            }
        }
    }
}







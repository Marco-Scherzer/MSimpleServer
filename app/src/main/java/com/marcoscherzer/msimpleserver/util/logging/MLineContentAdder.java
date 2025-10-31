package com.marcoscherzer.msimpleserver.util.logging;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved 2003,2024. All Rights Reserved.
 * History: siehe MStringBuilder
 */
public class MLineContentAdder {

    boolean lastWasNewLine = true;

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public String prefix(String s, String preFix) {
        char[] chars = s.toCharArray();
        StringBuilder sb_ = new StringBuilder();
        for (char c : chars) {
            if (c == '\n') lastWasNewLine = true;
            else if (lastWasNewLine) {
                sb_.append(preFix);
                lastWasNewLine = false;
            }
            sb_.append(c);
        }
        return sb_.toString();
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public String prefix(char c, String preFix) {
        return prefix(Character.toString(c), preFix);
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public String prefix(int i, String preFix) {
        return prefix(Integer.toString(i), preFix);
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public String prefix(long l, String preFix) {
        return prefix(Long.toString(l), preFix);
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public String prefix(float f, String preFix) {
        return prefix(Float.toString(f), preFix);
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public String prefix(double d, String preFix) {
        return prefix(Double.toString(d), preFix);
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public String prefix(boolean b, String preFix) {
        return prefix(Boolean.toString(b), preFix);
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public String prefix(Object obj, String preFix) {
        return prefix(obj.toString(), preFix);
    }
}

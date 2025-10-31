package com.marcoscherzer.msimpleserver.mpool;


import java.util.ArrayList;

/**
 * Author: Marco Scherzer
 *
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
 */
public class GCArrayList {
    private final ArrayList<Object> list;
    private int gcMax;

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public GCArrayList() {
        this.list = new ArrayList<>();
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public void setGCMax(int gcMax) {
        this.gcMax = gcMax;
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public void add(Object element) {
        if (list.size() > gcMax) {
            list.clear();
            System.gc();
        }
        list.add(element);
    }

    /**
     * Author: Marco Scherzer
     *
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
     */
    public int size() {
        return list.size();
    }
}
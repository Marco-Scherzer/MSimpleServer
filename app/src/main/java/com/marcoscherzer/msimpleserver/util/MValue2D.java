package com.marcoscherzer.msimpleserver.util;

/**
 * author Marco Scherzer, Copyright Marco Scherzer, All rights reserved 2003,2024. All Rights Reserved.,All rights reserved
 */
public final class MValue2D<T1, T2> {
    private final T1 val1;
    private final T2 val2;

    public MValue2D(T1 val1, T2 val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    public T1 get1() {
        return val1;
    }

    public T2 get2() {
        return val2;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return "(" + val1 + "," + val2 + ")";
    }
}

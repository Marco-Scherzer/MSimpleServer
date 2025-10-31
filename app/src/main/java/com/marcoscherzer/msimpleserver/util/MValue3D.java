package com.marcoscherzer.msimpleserver.util;

/**
 * author Marco Scherzer, Copyright Marco Scherzer, All rights reserved 2003,2024. All Rights Reserved.,All rights reserved
 */
public final class MValue3D<T1, T2, T3> {
    private final T1 val1;
    private final T2 val2;
    private final T3 val3;

    public MValue3D(T1 val1, T2 val2, T3 val3) {
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
    }

    public T1 get1() {
        return val1;
    }

    public T2 get2() {
        return val2;
    }

    public T3 get3() {
        return val3;
    }
}
 
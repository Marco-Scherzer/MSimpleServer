package com.marcoscherzer.msimpleserver.util.logging;

import com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.MLogHeader;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved 2003,2024. All Rights Reserved.
 */
public final class MStringBuilder {
    private final StringBuffer sb = new StringBuffer();
    private final MLineContentAdder prefixAdder = new MLineContentAdder();
    private MLogHeader logHeader;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved 2003,2024. All Rights Reserved.
     */
    public MStringBuilder append(String txt) {
        if (logHeader != null) {
            sb.append(prefixAdder.prefix(txt, logHeader.create()));
        } else {
            sb.append(txt);
        }
        return this;
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public MStringBuilder append(char c) {
        if (logHeader != null) {
            sb.append(prefixAdder.prefix(c, logHeader.create()));
        } else {
            sb.append(c);
        }
        return this;
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public MStringBuilder append(int i) {
        if (logHeader != null) {
            sb.append(prefixAdder.prefix(i, logHeader.create()));
        } else {
            sb.append(i);
        }
        return this;
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public MStringBuilder append(long l) {
        if (logHeader != null) {
            sb.append(prefixAdder.prefix(l, logHeader.create()));
        } else {
            sb.append(l);
        }
        return this;
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public MStringBuilder append(float f) {
        if (logHeader != null) {
            sb.append(prefixAdder.prefix(f, logHeader.create()));
        } else {
            sb.append(f);
        }
        return this;
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public MStringBuilder append(double d) {
        if (logHeader != null) {
            sb.append(prefixAdder.prefix(d, logHeader.create()));
        } else {
            sb.append(d);
        }
        return this;
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public MStringBuilder append(boolean b) {
        if (logHeader != null) {
            sb.append(prefixAdder.prefix(b, logHeader.create()));
        } else {
            sb.append(b);
        }
        return this;
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public MStringBuilder append(Object obj) {
        if (logHeader != null) {
            sb.append(prefixAdder.prefix(obj, logHeader.create()));
        } else {
            sb.append(obj);
        }
        return this;
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public MLogHeader getLogHeader() {
        return this.logHeader;
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    public void setLogHeader(MLogHeader logHeader) {
        this.logHeader = logHeader;
    }

    /**
     * Copyright Marco Scherzer, All rights reserved, Author Marco Scherzer
     */
    @Override
    public String toString() {
        return sb.toString();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved 2003,2024. All Rights Reserved.
     */
    public void setLength(int newLength) {
        sb.setLength(newLength);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved 2003,2024. All Rights Reserved.
     */
   /* public static void main(String[] args) {
        MStringBuilder ms = new MStringBuilder();
        ms.setLogHeader(new MLogHeader().addStringConstant("\t\t@"));
        
        ms.append("testtext0\n");
        ms.append("testtext1\n");
        ms.append('a');
        ms.append(123);
        ms.append(123456789L);
        ms.append(3.14f);
        ms.append(2.71828);
        ms.append(true);
        ms.append(new Object());

        System.out.println(ms.toString());
    }*/
}

package com.cao.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtil {
    private ExceptionUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * get the exception stack trace
     */
    public static String getStackTrace(Throwable t) {
        var sw = new StringWriter();
        var pw = new PrintWriter(sw);
        try (pw) {
            t.printStackTrace(pw);
            return sw.toString();
        }
    }
}

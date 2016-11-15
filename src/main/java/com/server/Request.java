package com.server;

/**
 * <p>
 * Request
 * </p>
 *
 * @author zhen.li
 * @date 2016-07-28
 */
public abstract class Request {

    public abstract String parse();

    protected String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }
}

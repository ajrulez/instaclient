package com.alifesoftware.instaclient.utils;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This class provides commonly used utilities that
 * are used at various places in this app
 */
public class Utils {
    /**
     * Method to check whether a String is valid
     * where a valid String means it is non-Null and not-empty
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        if(str == null ||
                str.isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * Method to convert InputStream (retrieved from
     * a HTTP connection) to String
     *
     * @param inputStream
     * @return
     */
    public static String streamToString(InputStream inputStream) {
        String response = "";

        if (inputStream != null) {
            Scanner scanner = new Scanner(inputStream);
            response = scanner.useDelimiter("\\A").next();
            scanner.close();
        }

        return response;
    }
}

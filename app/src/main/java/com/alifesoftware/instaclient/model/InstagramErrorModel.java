package com.alifesoftware.instaclient.model;

/**
 * Created by anujsaluja on 6/12/15.
 *
 * This class represents the data model for Instagram
 * error messages which is in JSON format
 *
 */
public class InstagramErrorModel {
    // Error Type
    private String errorType;

    // Error Message
    private String errorMessage;

    // Error Code
    private int errorCode;

    /** Getters and Setters for the above fields **/
    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * Converts InstagramErrorModel object to String
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("\"Like\" Error (");
        bldr.append(errorCode);
        bldr.append(") : ");
        bldr.append(errorType);
        bldr.append(" - ");
        bldr.append(errorMessage);

        return bldr.toString();
    }
}

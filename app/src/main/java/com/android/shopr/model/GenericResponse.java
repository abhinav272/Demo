package com.android.shopr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by abhinav.sharma on 1/10/2017.
 */

public class GenericResponse {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("errorCode")
    @Expose
    private String errorCode;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

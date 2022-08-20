package com.forthreal.crypto.dto;

public class RequestDto {
    private String requestString;
    private String signatureText;

    public RequestDto(String requestString, String signatureText) {
        this.requestString = requestString;
        this.signatureText = signatureText;
    }

    public String getRequestString() {
        return requestString;
    }
    public void setRequestString(String requestString) {
        this.requestString = requestString;
    }

    public String getSignatureText() {
        return signatureText;
    }
    public void setSignatureText(String signatureText) {
        this.signatureText = signatureText;
    }
}

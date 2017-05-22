package com.vpage.vpos.pojos.sale;

public class UpdateSaleResponse {

    private String status;

    private String status_code;

    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UpdateSaleResponse{" +
                "status='" + status + '\'' +
                ", status_code='" + status_code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

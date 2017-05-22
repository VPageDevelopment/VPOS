package com.vpage.vpos.pojos.supplier;

public class UpdateSuppliersResponse {

    private String status;

    private String status_code;

    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UpdateSuppliersResponse{" +
                "status='" + status + '\'' +
                ", status_code='" + status_code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

package com.vpage.vpos.pojos;

public class SignInResponse {

    private String username;

    private String status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SignInResponse{" +
                "username='" + username + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

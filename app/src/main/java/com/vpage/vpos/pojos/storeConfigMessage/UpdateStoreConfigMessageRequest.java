package com.vpage.vpos.pojos.storeConfigMessage;


public class UpdateStoreConfigMessageRequest {

    private String smpt_timeout;

    private String smpt_encryption;

    private String protocol;

    private String smpt_port;

    private String smpt_username;

    private String smpt_password;

    private String path_to_sendmail;

    private String smpt_server;

    public String getSmpt_timeout() {
        return smpt_timeout;
    }

    public void setSmpt_timeout(String smpt_timeout) {
        this.smpt_timeout = smpt_timeout;
    }

    public String getSmpt_encryption() {
        return smpt_encryption;
    }

    public void setSmpt_encryption(String smpt_encryption) {
        this.smpt_encryption = smpt_encryption;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSmpt_port() {
        return smpt_port;
    }

    public void setSmpt_port(String smpt_port) {
        this.smpt_port = smpt_port;
    }

    public String getSmpt_username() {
        return smpt_username;
    }

    public void setSmpt_username(String smpt_username) {
        this.smpt_username = smpt_username;
    }

    public String getSmpt_password() {
        return smpt_password;
    }

    public void setSmpt_password(String smpt_password) {
        this.smpt_password = smpt_password;
    }

    public String getPath_to_sendmail() {
        return path_to_sendmail;
    }

    public void setPath_to_sendmail(String path_to_sendmail) {
        this.path_to_sendmail = path_to_sendmail;
    }

    public String getSmpt_server() {
        return smpt_server;
    }

    public void setSmpt_server(String smpt_server) {
        this.smpt_server = smpt_server;
    }

    @Override
    public String toString() {
        return "UpdateStoreConfigMessageRequest{" +
                "smpt_timeout='" + smpt_timeout + '\'' +
                ", smpt_encryption='" + smpt_encryption + '\'' +
                ", protocol='" + protocol + '\'' +
                ", smpt_port='" + smpt_port + '\'' +
                ", smpt_username='" + smpt_username + '\'' +
                ", smpt_password='" + smpt_password + '\'' +
                ", path_to_sendmail='" + path_to_sendmail + '\'' +
                ", smpt_server='" + smpt_server + '\'' +
                '}';
    }
}

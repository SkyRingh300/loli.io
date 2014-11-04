package io.loli.sc.server.util;

public class StatusBean {
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_ERROR = "error";
    private String status;
    private String message;

    public StatusBean(String status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

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

}

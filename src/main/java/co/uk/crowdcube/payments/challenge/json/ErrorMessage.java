package co.uk.crowdcube.payments.challenge.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"status", "code", "message"})
public class ErrorMessage {

    private String status;
    private String message;
    private Integer code;

    public ErrorMessage status(final String status) {
        this.status = status;
        return this;
    }

    public ErrorMessage message(final String message) {
        this.message = message;
        return this;
    }

    public ErrorMessage code(final Integer code) {
        this.code = code;
        return this;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "status='" + this.status + '\'' +
                ", message='" + this.message + '\'' +
                ", code=" + this.code +
                '}';
    }
}
package co.uk.crowdcube.payments.challenge.exception;

public class InvalidInformationException extends Exception {

    public InvalidInformationException() {
        super();
    }

    public InvalidInformationException(String message) {
        super(message);
    }

    public InvalidInformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInformationException(Throwable cause) {
        super(cause);
    }
}

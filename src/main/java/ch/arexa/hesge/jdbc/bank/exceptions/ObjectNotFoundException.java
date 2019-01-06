package ch.arexa.hesge.jdbc.bank.exceptions;

public class ObjectNotFoundException extends Throwable {
    public ObjectNotFoundException(String s) {

    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

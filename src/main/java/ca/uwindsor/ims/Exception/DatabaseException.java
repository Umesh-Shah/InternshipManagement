package ca.uwindsor.ims.exception;

public class DatabaseException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DatabaseException(Throwable cause) {
        super(cause);
    }
} 
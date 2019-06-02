package controller.exceptions;

public class MissingWinnerException extends RuntimeException {

    public MissingWinnerException(String message){
        super(message);
    }
}

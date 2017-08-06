package cf.nathanpb.Dogo.Exceptions;

/**
 * Created by nathanpb on 8/1/17.
 */
public class EvalException extends Exception{
    String error;
    String message;
    public EvalException(String error, String message){
        super();
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

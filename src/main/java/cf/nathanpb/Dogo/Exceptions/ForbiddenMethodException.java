package cf.nathanpb.Dogo.Exceptions;

/**
 * Created by nathanpb on 8/24/17.
 */
public class ForbiddenMethodException extends Exception{
    public ForbiddenMethodException(){
        super("There is a forbidden method in your code!", new RuntimeException("Forbidden method!"));
    }
}

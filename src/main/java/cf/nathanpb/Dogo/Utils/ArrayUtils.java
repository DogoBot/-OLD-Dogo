package cf.nathanpb.Dogo.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathanpb on 8/4/17.
 */
public class ArrayUtils {
    public static String toString(ArrayList<String> list){
        String s = "";
        for(String o : list){
            s+=o+", ";
        }
        return s;
    }
    public static String toString(List<Object> list){
        String s = "";
        for(Object o : list){
            s+=o+", ";
        }
        return s;
    }
}

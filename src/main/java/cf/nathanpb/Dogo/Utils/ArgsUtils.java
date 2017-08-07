package cf.nathanpb.Dogo.Utils;

import java.util.ArrayList;

/**
 * Created by nathanpb on 7/28/17.
 */
public class ArgsUtils {
    public static String merge(ArrayList<String> args, int start){
        start = start-1;
        while(start >= 0){
            args.remove(start);
            start--;
        }
        String s = "";
        for(String s2 : args){
            s+=s2+" ";
        }
        return s.substring(0, s.length() - 1);
    }
}

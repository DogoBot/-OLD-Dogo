package cf.nathanpb.Dogo.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by nathanpb on 7/27/17.
 */
public class SystemUtils {
    public static String shellCommand(String command){
        StringBuffer output = new StringBuffer();

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}

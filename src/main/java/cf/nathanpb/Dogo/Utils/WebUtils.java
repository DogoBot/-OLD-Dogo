package cf.nathanpb.Dogo.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by nathanpb on 7/28/17.
 */
public class WebUtils {
    public static Object download(URL url) throws Exception{
        InputStream is = null;
        BufferedReader br;
        String line;
        String output="";
        try {
            URLConnection c = url.openConnection();
            c.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            is = c.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                output+=line+"\n";
            }
        } finally {
            if (is != null) is.close();
        }
        return output;
    }
    public static Object download(String s) throws Exception{
        return download(new URL(s));
    }
}

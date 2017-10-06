package cf.nathanpb.Dogo.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by nathanpb on 7/28/17.
 */
public class WebUtils {
    public static Object download(URL url, Proxy proxy) throws Exception{
        InputStream is = null;
        BufferedReader br;
        String line;
        String output="";
        try {
            URLConnection c;
            if(proxy != null){
                c = url.openConnection(proxy);
            }else{
                c = url.openConnection();
            }

            c.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            c.connect();
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
    public static Object download(String s, Proxy proxy) throws Exception{
        return download(new URL(s), proxy);
    }
}

package cf.nathanpb.Dogo.Utils;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by nathanpb on 7/27/17.
 */
public class HastebinUtils {
    private  static String url = "https://hastebin.com/";
    public static String upload(String text){
        try {
            String pasteToken = Unirest.post("https://hastebin.com/documents")
                    .header("User-Agent", "PCBRecBot")
                    .header("Content-Type", "text/plain")
                    .body(text)
                    .asJson()
                    .getBody()
                    .getObject()
                    .getString("key");
            return pasteToken;
        } catch (UnirestException e) {
            return "Config threw ``" + e.getCause().getClass().getSimpleName() + "``" + " while trying to upload paste, check logs";
        }
    }
    public static synchronized String download(String ID) {
        String URLString = "https://hastebin.com/raw/" + ID + "/";
        try {
            return WebUtils.download(URLString, null).toString();
        }catch (Exception e){
            return e.getMessage();
        }
    }
    public static String getUrl(String ID, boolean raw){
        if(raw) return url+"raw/"+ID;
        return url+ID;
    }
    public static String getID(String URL){
        if(URL.contains("/raw/")){
            URL = URL.replace("raw/", "");
        }
        return URL.replace(url, "");
    }
}

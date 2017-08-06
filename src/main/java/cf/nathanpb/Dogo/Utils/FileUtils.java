package cf.nathanpb.Dogo.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Created by nathanpb on 7/28/17.
 */
public class FileUtils {
    public static void copyData(InputStream from, OutputStream to) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = from.read(buffer)) != -1) to.write(buffer, 0, read);
    }
    public static String read(File f){
        String s = "";
        try {
            for(String ss : Files.readAllLines(f.toPath())){
                s+=ss+"\n";
            }
        }catch (Exception e){
            return e.getMessage();
        }
        return s;
    }
}

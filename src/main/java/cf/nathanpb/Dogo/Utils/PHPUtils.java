package cf.nathanpb.Dogo.Utils;

import cf.nathanpb.Dogo.Config;
import me.nathanpb.ProjectMetadata.ProjectMetadataObject;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nathanpb on 7/28/17.
 */
public class PHPUtils {
    static {
       String s = "apache_child_terminate, apache_setenv, define_syslog_variables, escapeshellarg, escapeshellcmd, eval, exec, dl, fsocket, pfsockopen, fsockopen, fp, fput, ftp_connect, ftp_exec, ftp_get, ftp_login, ftp_nb_fput, ftp_put, ftp_raw, ftp_rawlist, highlight_file, ini_alter, ini_get_all, ini_restore, inject_code, mysql_pconnect, openlog, phpinfo, passthru, php_uname, phpAds_remoteInfo, phpAds_XmlRpc, phpAds_xmlrpcDecode, phpAds_xmlrpcEncode, popen, posix_getpwuid, posix_kill, posix_mkfifo, posix_setpgid, posix_setsid, posix_setuid, posix_setuid, posix_uname, proc_close, proc_get_status, proc_nice, proc_open, proc_terminate, shell_exec, syslog, system, system,proc_open, symlink, xmlrpc_entity_decode, popen, curl_exec, curl_multi_exec, parse_ini_file, show_source";
        for(String s2 : s.split(", ")){
            addForbidden(s2);
        }
    }
    public static String header = "array(\n" +
            "  'http'=>array(\n" +
            "    'method'=>\"GET\",\n" +
            "    'header'=>\"Accept-language: en\\r\\n\" .\n" +
            "              \"Cookie: foo=bar\\r\\n\" .\n" +
            "              \"User-Agent: Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.102011-10-16 20:23:10\\r\\n\"\n" +
            "  )\n" +
            ")";
    public static Object eval(String code, Object[] args) throws Exception{
            String arg = "";
            for(Object s2 : args){
                if(StringUtils.isNumeric(s2.toString())){
                    arg += Long.valueOf(s2.toString())+", ";
                }else if(s2 instanceof String && s2.toString().startsWith("array")) {
                    arg += s2.toString()+", ";
                }else
                {
                    s2 = s2.toString().replace("\"", "\\\"");
                    arg += "\""+s2.toString()+"\", ";
                }
            }
            code.replace("<?php", "").replace("?>", "");
            code = "$args = Array("+ arg+");\n"+code;
            code = "<?php \n function errorHandler($errno, $errstr){\n" +
                    "\techo '[$errno]$errstr';\n" +
                    "\texit();\n" +
                    "}\n" +
                    "set_error_handler('errorHandler');\n"+ code;
            code += "\n ?>";
            String fname = new Random().nextInt(100) + ".php";
            File file = new File(new File(Config.APACHE_HOME.get(String.class)), fname);
            file.createNewFile();
            FileUtils.copyData(new ByteArrayInputStream(code.getBytes(Charset.defaultCharset())), new FileOutputStream(file));
            System.out.println(code);
            Object o = WebUtils.download(new URL(Config.WEBSITE_URL.get(String.class)+fname), null);
            file.delete();
            return o;
    }

    public static void addForbidden(String s){
        ProjectMetadataObject forbidden = new ProjectMetadataObject("forbidden_php");
        ArrayList<String> forb = getForbideen();
        if(!forb.contains(s)){
            forb.add(s);
            forbidden.put("methods", forb);
        }
    }
    public static ArrayList<String>getForbideen(){
        ProjectMetadataObject forbidden = new ProjectMetadataObject("forbidden_php");
        if(forbidden.hasKey("methods")){
            return forbidden.getAsList("methods", String.class);
        }
        return new ArrayList<>();
    }
}

package cf.nathanpb.Dogo.Utils;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Exceptions.EvalException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by nathanpb on 7/28/17.
 */
public class JavaUtils {
    public static Object[] eval(String code, Object instance) throws EvalException{
        Object[] returns = new Object[]{System.currentTimeMillis(), System.currentTimeMillis(), "No returns"};
        try {
                 File root = new File(Config.CONTENTS_PATH.get(String.class));
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                ;
                File source = new File(root, "eval/eval.java");
                source.getParentFile().mkdirs();
                try (FileOutputStream fos = new FileOutputStream(source)) {
                    FileUtils.copyData(new ByteArrayInputStream(code.getBytes(Charset.defaultCharset())), fos);
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int errcode = compiler.run(null, out, out, source.getPath());
                returns[0] = System.currentTimeMillis() - (long) returns[0];
                if (errcode != 0) {
                    throw new EvalException(new String(out.toByteArray(), Charset.defaultCharset()), "Error Compiling");
                }
                DummyClassLoader loader = new DummyClassLoader(JavaUtils.class.getClassLoader());
                File[] files = new File(root, "eval").listFiles();
                if (files == null) files = new File[0];
                for (File f : files) {
                    if (!f.getName().endsWith(".class")) continue;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try (FileInputStream fis = new FileInputStream(f)) {
                        FileUtils.copyData(fis, baos);
                    }
                    f.delete();
                    String clname = f.getAbsolutePath().substring(root.getAbsolutePath().length() + 1).replace('/', '.').replace('\\', '.');

                    UnsafeUtils.defineClass(
                            clname.substring(0, clname.length() - 6),
                            baos.toByteArray(),
                            loader
                    );
                }
                returns[1] = System.currentTimeMillis();
                Object ret = loader.loadClass("eval.eval").getMethod("run", Command.class).invoke(null, instance);
                returns[1] = System.currentTimeMillis() - (long) returns[1];
                if (ret == null) {
                    return returns;
                }
                String v;

                if (ret instanceof Object[]) v = Arrays.toString((Object[]) ret);
                else if (ret instanceof boolean[]) v = Arrays.toString((boolean[]) ret);
                else if (ret instanceof byte[]) v = Arrays.toString((byte[]) ret);
                else if (ret instanceof short[]) v = Arrays.toString((short[]) ret);
                else if (ret instanceof char[]) v = new String((char[]) ret);
                else if (ret instanceof int[]) v = Arrays.toString((int[]) ret);
                else if (ret instanceof float[]) v = Arrays.toString((float[]) ret);
                else if (ret instanceof long[]) v = Arrays.toString((long[]) ret);
                else if (ret instanceof double[]) v = Arrays.toString((double[]) ret);
                else v = String.valueOf(ret);
                returns[2] = v;
            }catch (EvalException e) {
                throw e;
            }catch (Exception e){
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.getCause().printStackTrace(pw);
                throw new EvalException(sw.toString(), "Error Executing: "+e.getClass().getSimpleName());
            }
        return returns;
    }

    public static class DummyClassLoader extends ClassLoader {
        DummyClassLoader(ClassLoader parent) {
            super(parent);
        }
    }
}

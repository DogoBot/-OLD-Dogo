package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.annotations.Default;
import cf.nathanpb.Dogo.CommandHandler.enums.Parameters;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.*;
import cf.nathanpb.Dogo.Exceptions.EvalException;
import cf.nathanpb.Dogo.Utils.ArgsUtils;
import cf.nathanpb.Dogo.Utils.HastebinUtils;
import cf.nathanpb.Dogo.Utils.JavaUtils;
import cf.nathanpb.Dogo.Utils.PHPUtils;
import com.sun.corba.se.impl.activation.CommandHandler;
import me.nathanpb.ProjectMetadata.ProjectMetadataObject;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.GuildManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.awt.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathanpb on 7/28/17.
 */
@Cmd(
        cmd = "eval",
        usage = "eval <Source Lang paramether> ode",
        description = "Evaluates a code",
        freeArgs = true,
        allow = Permission.OWNER,
        argslengh = 1
)
public class Eval {
    public static String imports = "package eval;\nimport cf.nathanpb.Dogo.*;\n";

    static {
        addImport(Config.class);
        addImport(Core.class);
        addImport(EXP.class);
        addImport(JsonMessage.class);
        addImport(Logger.class);
        addImport(RecruiterChannelCleaner.class);
        addImport(Permission.class);
        addImport(Core.class.getPackage());

        addImport(JDA.class);
        addImport(User.class);
        addImport(Message.class);
        addImport(Channel.class);
        addImport(TextChannel.class);
        addImport(PrivateChannel.class);
        addImport(VoiceChannel.class);
        addImport(EmbedBuilder.class);
        addImport(MessageEmbed.class);
        addImport(Event.class);
        addImport(MessageReceivedEvent.class);
        addImport(JDABuilder.class);
        addImport(EventListener.class);
        addImport(AccountType.class);
        addImport(SelfUser.class);
        addImport(Member.class);
        addImport(Role.class);
        addImport(GuildController.class);
        addImport(GuildManager.class);
        addImport(MessageChannel.class);
        addImport(AccountType.class);

        addImport(File.class);
        addImport(Exception.class);
        addImport(List.class);
        addImport(ArrayList.class);
        addImport(Arrays.class);
        addImport(Calendar.class);
        addImport(SimpleDateFormat.class);
        addImport(FileReader.class);
        addImport(FileWriter.class);
        addImport(FileOutputStream.class);
        addImport(FileInputStream.class);

        addImport(TimeUnit.class);
        addImport(ManagementFactory.class);
        addImport(HashMap.class);
        addImport(TimeZone.class);


        addImport(ProjectMetadataObject.class);
        addImport(ClassLoader.class);
        addImport(URLClassLoader.class);

        addImport(URL.class);
        addImport(URLConnection.class);
        addImport(InputStreamReader.class);
        addImport(BufferedReader.class);
        addImport(InputStream.class);
        addImport(Reflections.class);

        addImport(JSONObject.class);
        addImport(JSONArray.class);
    }

    public static Set<Class<?>> getAllClasses(Package p) {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        return new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(p.getName()))))
                .getSubTypesOf(Object.class);
    }

    public static void addImport(Class c) {
        String s = "import " + c.getName().replace("$", ".") + ";\n";
        if (!imports.contains(s) && !c.equals(Config.class)) {
            imports += s;
        }
    }

    public static void addImport(Package p) {
        for (Class c : getAllClasses(p)) {
            addImport(c);
        }
    }

    @Default
    public static void Default(Command cmd) {
        EmbedBuilder s = new EmbedBuilder().setDescription("Forneça um parâmetro correspondente a linguagem!");
        s.setColor(Color.GREEN);
        if (cmd.getParameters().contains(Parameters.JAVA)) {
            try {
                Object[] objects = JavaUtils.eval(mkClass(ArgsUtils.merge(cmd.getArgs(), 0)), cmd);
                objects[2] = objects[2].toString().replace(cf.nathanpb.Dogo.Config.TOKEN.get(), "<BOT_TOKEN>");
                if(objects[2].toString().length() > 500){
                    objects[2] = HastebinUtils.getUrl(HastebinUtils.upload(objects[2].toString()), false);
                }else{
                    objects[2] = "```"+objects[2].toString()+"```";
                }
                s.setDescription(objects[2].toString());
                s.setTitle("Compiled in ``" + objects[0] + "ms``, executed in ``" + objects[1] + "ms``");
            } catch (EvalException e) {
                s.setColor(Color.RED);
                s.setTitle(e.getMessage());
                String s2 = e.getError();
                s2.replace(cf.nathanpb.Dogo.Config.TOKEN.get(), "<BOT_TOKEN>");
                if (s2.length() > 500) {
                    s2 = HastebinUtils.getUrl(HastebinUtils.upload(s2), false);
                } else {
                    s2 = "```" + s2 + "```";
                }
                s.setDescription(s2);
            }

        }
        if (cmd.getParameters().contains(Parameters.PHP)) {
            long time = System.currentTimeMillis();
            Object out;
            try {
                out = PHPUtils.eval(ArgsUtils.merge(cmd.getArgs(), 0), new Object[]{});
                s.setTitle("Executed in ``" + (System.currentTimeMillis() - time) + "ms``");
            } catch (Exception e) {
                out = e.getMessage();
                s.setTitle(e.getClass().getSimpleName());
                s.setColor(Color.RED);
            }
            if (out == null || out.toString().isEmpty()) {
                out = "No return!";
            }
            if (s.toString().length() > 500) {
                out = HastebinUtils.getUrl(HastebinUtils.upload(out.toString()), false);
            } else {
                out = "```" + out + "```";
            }
            s.setDescription(out.toString());
        }
        cmd.reply(s.build());
    }

    public static String mkClass(String code) {
        if (!code.endsWith(";")) {
            code += ";";
        }
        return
                imports +
                        "public class eval {\n" +
                        "public static Object run(Command cmd) throws Throwable {\n" +
                        "try {\n" +
                        "return null;\n" +
                        "} finally {\n" +
                        code + "\n" +
                        "}\n" +
                        "}\n" +
                        "}";
    }
}

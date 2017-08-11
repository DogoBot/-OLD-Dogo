package cf.nathanpb.Dogo.CommandHandler;

import cf.nathanpb.Dogo.CommandHandler.annotations.Arg;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.annotations.Default;
import cf.nathanpb.Dogo.CommandHandler.enums.Parameters;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Commands.QuickCommand;
import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Core;
import cf.nathanpb.Dogo.Events.CommandExecutedEvent;
import cf.nathanpb.Dogo.JsonMessage;
import cf.nathanpb.Dogo.Utils.DiscordUtils;
import cf.nathanpb.Dogo.Utils.HastebinUtils;
import net.dv8tion.jda.core.entities.*;
import org.json.JSONObject;
import org.reflections.Reflections;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * Created by nathanpb on 7/27/17.
 */
public class Command {

    private User sender;
    private String raw;
    private String cmdName;
    private Class cmdClass;
    private Cmd annotation;
    private Message msg;
    private TextChannel channel;
    private Guild guild;
    public boolean allow = true;
    public int autodeleteCooldown;

    private boolean found = false;

    private ArrayList<String> args = new ArrayList<>();
    private ArrayList<Parameters> parameters = new ArrayList<>();

    private boolean ownerPerms = false;

    private Command(Message msg, Class cmdClass, Cmd annotation) {
        this.sender = msg.getAuthor();
        this.raw = msg.getRawContent();
        this.cmdClass = cmdClass;
        this.annotation = annotation;
        this.cmdName = annotation.cmd();
        this.autodeleteCooldown = annotation.cooldown();
        this.msg = msg;
        this.channel = (TextChannel) msg.getChannel();
        this.guild = msg.getGuild();
        getArgs(getParameters(msg.getContent()));

        if (this.annotation.freeArgs()) found = true;
        if (getParameters().contains(Parameters.OWNER)) {
            ownerPerms = Permission.OWNER.has(sender);
        }
        trigger();
        if(allow) {
            if (this.parameters.contains(Parameters.DELETE)) {
                try {
                    getMessage().delete().complete();
                } catch (Exception e) {

                }finally {
                    return;
                }
            }
        }
        cf.nathanpb.Dogo.CommandHandler.QuickCommand.checkQuickCommand(msg);
    }

    private String getParameters(String raw) {
        for (Parameters p : Parameters.values()) {
            String want = " " + Config.PARAMETHER_PREFIX.get(String.class) + p.getId();
            if (raw.contains(want)) {
                parameters.add(p);
                raw = raw.replace(want, "");
            }
        }
        return raw;
    }

    private String getArgs(String raw) {
        raw = raw.replace(Config.COMMAND_PREFIX.get(String.class), "");
        args = new ArrayList<>(Arrays.asList(raw.split(" ")));
        args.remove(0);
        return raw;
    }

    public static void checkCommand(Message msg) {
        String cmdname = msg.getContent().split(" ")[0].replace(Config.COMMAND_PREFIX.get(String.class), "");
        if (msg.getAuthor().isBot() || msg.getAuthor().isFake()) {
            return;
        }
        if (msg.getContent().startsWith(Config.COMMAND_PREFIX.get(String.class))) {
            for (Class c : new Reflections("cf.nathanpb.Dogo.Commands").getTypesAnnotatedWith(Cmd.class)) {
                Cmd a = (Cmd) c.getAnnotation(Cmd.class);
                if (a.cmd().equalsIgnoreCase(cmdname)) {
                    new Command(msg, c, a);
                    return;
                }
            }
        }
    }

    public void reply(JSONObject o) {
        reply(o.toString());
    }

    public void reply(String s) {
        try {
            new JsonMessage(s).send(channel);
        } catch (Exception e) {
            channel.sendMessage(s).complete();
        }
    }

    public void reply(MessageEmbed embed) {
        channel.sendMessage(embed).queue();
    }

    public User getSender() {
        return sender;
    }

    public String getRaw() {
        return raw;
    }

    public String getCmdName() {
        return cmdName;
    }

    public Class getCmdClass() {
        return cmdClass;
    }

    public Message getMessage() {
        return msg;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public Guild getGuild() {
        return guild;
    }

    public Cmd getAnnotation() {
        return annotation;
    }

    public ArrayList<Parameters> getParameters() {
        return parameters;
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    public boolean isOwner() {
        return this.ownerPerms;
    }

    public boolean getAllow() {
        return allow;
    }

    public String getArg(int index) {
        return getArgs().get(index);
    }


    private Method getDefault() {
        for (Method m : cmdClass.getMethods()) {
            if (m.isAnnotationPresent(Default.class)) {
                return m;
            }
        }
        return null;
    }

    private Method getMethod() {
        for (Method m : cmdClass.getMethods()) {
            if (m.isAnnotationPresent(Arg.class)) {
                if ((m.getAnnotation(Arg.class)).arg().equalsIgnoreCase(getArg(0))) {
                    return m;
                }
            }
        }
        return null;
    }

    private Arg getAnnotation(Method m) {
        return m.getAnnotation(Arg.class);
    }

    private boolean checkargs(Method m) {
        return args.size() >= getAnnotation(m).argsLenght();
    }

    private boolean checkargs() {
        return args.size() >= getAnnotation().argslengh();
    }

    private void exec() throws Exception {
        Method dm = null;
        if (Permission.canExecute(this, getAnnotation())) {
            if (checkargs()) {
                allow = true;
                Method d = getDefault();
                if (d != null) {
                    dm = d;
                }
            } else {
                channel.sendMessage("Incorrect args size! Use " + Config.COMMAND_PREFIX.get(String.class) + "help to see help").queue();
                return;
            }
        } else {
            channel.sendMessage("You don't have permissions to use this command!").queue();
            allow = false;
            return;
        }
        Method m = getMethod();
        if (m != null) {
            if (Permission.canExecute(this, getAnnotation(m))) {
                if (checkargs(m)) {
                    allow = true;
                } else {
                    channel.sendMessage("Incorrect args size! Use " + Config.COMMAND_PREFIX.get(String.class) + "help to see help").queue();
                    return;
                }
            } else {
                channel.sendMessage("You don't have permissions to use this command!").queue();
                allow = false;
                return;
            }
        }
        if (m == null && !getAnnotation().freeArgs()) {
            channel.sendMessage("This arg doesn't exists! Use " + Config.COMMAND_PREFIX.get(String.class) + "help to see help").queue();
            return;
        }
        if (dm != null) dm.invoke(null, (Object) this);
        if (m != null) m.invoke(null, (Object) this);
    }

    private void trigger() {
        Executors.newSingleThreadExecutor().submit(
                () -> {
                    try {
                        exec();
                        new CommandExecutedEvent(this);
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.getCause().printStackTrace(pw);
                        DiscordUtils.sendPrivateMessage(Core.jda.getUserById(Config.OWNER_ID.get()), "An Exception occurred: ``" + e.getCause().getClass().getSimpleName() + "``, " + HastebinUtils.getUrl(HastebinUtils.upload(sw.toString()), false));
                        this.getChannel().sendMessage("An ``" + e.getCause().getClass().getSimpleName() + "`` occurred!").queue();
                    }
                }
        );
    }
}

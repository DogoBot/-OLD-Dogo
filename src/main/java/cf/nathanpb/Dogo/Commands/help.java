package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Arg;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.annotations.Default;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Utils.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import org.json.JSONObject;
import org.reflections.Reflections;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nathanpb on 8/2/17.
 */
@Cmd(
        cmd = "help",
        usage = "help <command_name>",
        description = "Show help about commands. If there is executed with no args, it would list all commands available",
        freeArgs = true
)
public class help {
    @Default
    public static void Default(Command cmd){
        EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN);
        embed.setDescription("Anything went wrong! Maybe you specified a wrong command!");
        embed.setFooter("Requested by "+cmd.getSender().getName(), DiscordUtils.getAvatarURL(cmd.getSender()));
        if(cmd.getArgs().size() == 0){
            String s = "";
            embed.setTitle("Help");
            String comandos = "Available Commands: ";
            String qq = "Available QuickCommands: ";
            for(Cmd an : getAllCommands()){
                comandos+="``"+an.cmd()+"``, ";
            }
            for(Map.Entry<String, JSONObject> entry : QuickCommand.getAll().entrySet()){
                qq += "``"+entry.getKey()+"``, ";
            }
            s = comandos+"\n"+qq+"\nUse !help ``command_name`` to get help about the specified command";
            embed.setDescription(s);
        }else if(cmd.getArgs().size() == 1){
            for(Cmd c : getAllCommands()){
                if(cmd.getArg(0).equalsIgnoreCase(c.cmd())){
                    embed.setTitle("Help: "+c.cmd().toLowerCase());
                    embed.setDescription(getHelp(c));
                }
            }
        }else if(cmd.getArgs().size() > 1){
            for(Cmd c : getAllCommands()){
                if(cmd.getArg(0).equalsIgnoreCase(c.cmd())){
                    for(Arg a : getArgs(c)){
                        if(cmd.getArg(1).equalsIgnoreCase(a.arg())) {
                            embed.setTitle("Help: "+c.cmd() + "#" + a.arg());
                            embed.setDescription(getHelp(a));
                        }
                    }
                }
            }
        }
        cmd.reply(embed.build());
    }
    public static List<Cmd> getAllCommands(){
        List<Cmd> cmds = new ArrayList<>();
        for(Class c : new Reflections("cf.nathanpb.Dogo.Commands").getTypesAnnotatedWith(Cmd.class)) {
            Cmd a = (Cmd) c.getAnnotation(Cmd.class);
            cmds.add(a);
        }
        return cmds;
    }
    public static List<Arg> getArgs(Cmd cmd){
        List<Arg> args = new ArrayList<>();
        for(Class c : new Reflections("cf.nathanpb.Dogo.Commands").getTypesAnnotatedWith(Cmd.class)) {
            if (c.getAnnotation(Cmd.class).equals(cmd)) {
                for (Method m : c.getMethods()) {
                    if (m.isAnnotationPresent(Arg.class)) args.add(m.getAnnotation(Arg.class));
                }
            }
        }
        return args;
    }

    public static String getHelp(Cmd c){
        String perms = "**Who can use this command?** Who has ";
        for(Permission p : c.allow()){
            perms+="``"+p.name()+"``, ";
        }
        perms+=" permission";
        String noperms = "**Who can't use this command?** Who has ";
        for(Permission p : c.disallow()){
            noperms+="``"+p.name()+"``, ";
        }
        if(noperms.equals("**Who can't use this command?** Who has ")) {;
            noperms = "**Who can't use this command? **No one is forbidden";
        }else{
            noperms += " permissiosn";
        }

        String usage = "**How to use this command?** Just type "+ Config.COMMAND_PREFIX.get()+c.usage();
        String description = "**What does this command do?** "+c.description();
        String args = "**What are the args available for this command? **";
        String a = "";
        for(Arg p : getArgs(c)){
            a +="``"+p.arg().toUpperCase()+"``, ";
        }
        if(a.equals("")){
            a = "This command has not default args!";
        }
        args += a;
        return description+"\n"+usage+"\n"+perms+"\n"+noperms+"\n"+args;
    }
    public static String getHelp(Arg c){
        String perms = "**Who can use this arg?** Who has ";
        for(Permission p : c.allow()){
            perms+="``"+p.name()+"``, ";
        }
        perms+=" permissions";
        String noperms = "**Who can't use this arg?** Who has ";
        for(Permission p : c.disallow()){
            noperms+="``"+p.name()+"``, ";
        }
        if(noperms.equals("**Who can't use this arg?** Who has ")) {;
            noperms = "**Who can't use this arg? **No one is forbidden";
        }else{
            noperms += " permissiosn";
        }

        String usage = "**How to use this arg?** Just type "+ Config.COMMAND_PREFIX.get()+c.usage();
        String description = "**What does this arg do?** "+c.description();

        return description+"\n"+usage+"\n"+perms+"\n"+noperms;
    }
}

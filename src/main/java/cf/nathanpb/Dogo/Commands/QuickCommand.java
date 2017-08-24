package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Arg;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.enums.Parameters;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Core;
import cf.nathanpb.Dogo.Events.QQExecutedEvent;
import cf.nathanpb.Dogo.Utils.DiscordUtils;
import cf.nathanpb.Dogo.Utils.HastebinUtils;
import cf.nathanpb.Dogo.Utils.PHPUtils;
import me.nathanpb.ProjectMetadata.ProjectMetadataObject;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONObject;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by nathanpb on 7/28/17.
 */
@Cmd(
        cmd = "qq",
        usage = "qq <arg>",
        description = "Manages QuickCommands",
        argslengh = 1
)
public class QuickCommand extends ListenerAdapter{
    private static ProjectMetadataObject profile = new ProjectMetadataObject("QuickCommands");
    public static HashMap<String, JSONObject> getAll(){
        HashMap<String, JSONObject> hashMap = new HashMap<>();
        for(String o : profile.getJSONObject().keySet()){
            if(profile.get(o, Object.class) instanceof JSONObject){
                hashMap.put(o, profile.get(o, JSONObject.class));
            }
        }
        return hashMap;
    }
    @Arg(
        arg = "list",
        usage = "qq list",
        description = "List all the available QuickCommands",
        argsLenght = 1
    )
    public static void list(Command cmd){
        String s = "";
        for(Map.Entry<String, JSONObject> entry : getAll().entrySet()){
            s += entry.getKey()+" - "+Core.jda.getUserById(entry.getValue().getString("owner")).getName()+"\n";
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.yellow);
        embed.setFooter("Requisitado por "+cmd.getSender().getName(), DiscordUtils.getAvatarURL(cmd.getSender()));
        embed.setDescription(s);
        cmd.getChannel().sendMessage(embed.build()).queue();
    }

    @Arg(
            arg = "create",
            usage = "qq create QQ_NAME HASTEBIN_LINK LANG_PARAMETER",
            description = "Add a QuickCommand",
            argsLenght = 3,
            allow = Permission.DEVELOPER
    )
    public static void create(Command cmd){
        String time = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance(TimeZone.getTimeZone("UTC+0")).getTime());
        Parameters p = null;
        if(cmd.getParameters().contains(Parameters.PHP)) {
            p = Parameters.PHP;
        }
        if(p == null){
            cmd.getChannel().sendMessage("Specify a lang parameter!").queue();
            return;
        }
        if (getAll().containsKey(cmd.getArg(1))) {
            cmd.getChannel().sendMessage("This name isn't available!").queue();
            return;
        } else {
            JSONObject object = new JSONObject();
            object.put("name", cmd.getArg(1))
                    .put("lang", p.name())
                    .put("owner", cmd.getSender().getId())
                    .put("date", time)
                    .put("src", cmd.getArg(2));
            String s = HastebinUtils.download(HastebinUtils.getID(cmd.getArg(2)));
            for(String s2 : PHPUtils.getForbideen()){
                if(s.contains(s2+"(") && !cmd.isOwner()){
                    cmd.getChannel().sendMessage("Opps, your QQ has a forbidden method: ``"+s2+"``").queue();
                    cmd.allow = false;
                    return;
                }
            }
            if(test(object, cmd.getChannel(), cmd.getSender(), cmd.getMessage().getContent())){
                object.put("source", HastebinUtils.download(object.getString("src")));
                object.remove("src");
                profile.put(object.getString("name"), object);
                cmd.getChannel().sendMessage("QuickCommand created successfully!").queue();
            }else{
                cmd.getChannel().sendMessage("Errors! I can1t execute this :/").queue();
                cmd.getChannel().sendMessage("```"+exec(object, cmd.getChannel(), cmd.getSender(), cmd.getMessage().getContent())+"```").queue();
                return;
            }
        }
    }

    @Arg(
            arg = "remove",
            usage = "quickcommand remove QQ_NAME",
            description = "Remove um QuickCommand",
            argsLenght = 2,
            allow = Permission.DEVELOPER
    )
    public static void remove(Command cmd){
        if(getAll().containsKey(cmd.getArg(1))){
            if(!cmd.isOwner()) {
                if (!Permission.ADMIN.has(cmd.getSender())) {
                    if (!getAll().get(cmd.getArg(1)).getString("owner").equals(cmd.getSender().getId())) {
                        cmd.getChannel().sendMessage("Este comando não te pertence!").queue();
                        cmd.allow = false;
                        return;
                    }
                }
            }
            profile.put(cmd.getArg(1), "deleted by owner");
            cmd.getChannel().sendMessage("O QuickCommand "+cmd.getArg(1)+" foi removido!").queue();
        }else{
            cmd.getChannel().sendMessage("Este QuickCommand não existe!").queue();
        }
    }

    @Arg(
            arg = "raw",
            usage = "quickcommand raw QQ_NAME",
            description = "Mostra o JSON 'puro' do QQ",
            argsLenght = 2,
            allow = Permission.DEVELOPER
    )
    public static void raw(Command cmd){
        if(getAll().containsKey(cmd.getArg(1))){
            if(!cmd.isOwner()) {
                if (!Permission.ADMIN.has(cmd.getSender())) {
                    if (!getAll().get(cmd.getArg(1)).getString("owner").equals(cmd.getSender().getId())) {
                        cmd.getChannel().sendMessage("This QuickCommand doesn't belongs you!").queue();
                        cmd.allow = false;
                        return;
                    }
                }
            }
            cmd.getChannel().sendMessage("```"+profile.get(cmd.getArg(1), JSONObject.class).toString()+"```").queue();
        }else{
            cmd.getChannel().sendMessage("This QuickCommand doesn't exists!").queue();
        }
    }

    public static boolean test(JSONObject o, MessageChannel channel,User user ,String raw){
        return !exec(o, channel, user, raw).startsWith("Error: ");
    }

    public static String exec(JSONObject o, MessageChannel channel, User u, String raw){
        Object s = "";
        boolean error = false;
        try {
            String code = o.getString("source");
            Object[] args = new Object[]{o, channel.getId(), u.getId(), raw, PHPUtils.header};
             s = PHPUtils.eval(code, args);
        }catch (Exception e){
            s = "```"+e.getMessage()+"```";
        }
        if(s.toString().isEmpty()){
            error = true;
        }
        if(s == null || s.toString().contains("java.io.IOException") || s.toString().isEmpty()){
            if(s == null) s = "";
            error = true;
        }
        if(error){
            s = "Error: "+s;
        }
        return s.toString();
    }
}

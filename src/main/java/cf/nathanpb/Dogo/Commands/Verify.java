package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.annotations.Default;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Core;
import cf.nathanpb.Dogo.Events.TriedToEnlistEvent;
import cf.nathanpb.Dogo.FormManager.Form;
import cf.nathanpb.Dogo.Logger;
import cf.nathanpb.Dogo.Utils.DiscordUtils;
import me.nathanpb.ProjectMetadata.ProjectMetadataObject;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

/**
 * Created by nathanpb on 8/2/17.
 */
@Cmd(
        cmd = "Verify",
        usage = "verify @Member @Role",
        description = "Send the verificaiton questions (if used without args, send to yourself)",
        allow = Permission.COMMON,
        freeArgs = true
)
public class Verify {
    @Default
    public static void Default(Command cmd){
        if(!cmd.getGuild().getId().equals(Config.GUILD_ID.get())){
            if(!cmd.isOwner()){
                cmd.allow = false;
                return;
            }
        }
        List<User> ment = DiscordUtils.getMentionedUsers(cmd.getMessage());
        if(ment.size() == 0){
            enlist(cmd.getSender());
        }else{
            ment.forEach((User u) -> enlist(u));
        }
    }
    public static void enlist(User u){
        Logger.log(new TriedToEnlistEvent(u));
        if(!Permission.VOID_APPRENDICE.has(u)) {
            ProjectMetadataObject profile = new ProjectMetadataObject(u.getId());
            if (!profile.hasKey("alistado")) {
                profile.put("alistado", false);
            }
            if (!profile.get("alistado", Boolean.class)) {
                Core.jda.addEventListener(new Form(u));
            }
        }else{
            DiscordUtils.sendPrivateMessage(u, "Your profile was already sent! Just wait. If you get promoted, you will be notified.");
        }
    }
}

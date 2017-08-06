package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.annotations.Default;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Utils.DiscordUtils;
import me.nathanpb.ProjectMetadata.ProjectMetadataObject;
import net.dv8tion.jda.core.entities.User;

/**
 * Created by nathanpb on 8/3/17.
 */
@Cmd(
        cmd = "reset",
        usage = "reset @Member @Role",
        description = "Reset member's profile",
        freeArgs = true,
        allow = Permission.ADMIN
)
public class reset {
    @Default
    public static void Default(Command cmd){
        for(User u : DiscordUtils.getMentionedUsers(cmd.getMessage())){
            new ProjectMetadataObject(u.getId()).reset();
            cmd.reply(u.getName()+"'s profile reseted!");
        }
    }
}

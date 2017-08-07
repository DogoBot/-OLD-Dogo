package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.annotations.Default;
import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Core;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

/**
 * Created by nathanpb on 8/3/17.
 */
@Cmd(
        cmd = "br",
        usage = "br",
        description = "Give or remove you the 'BR' role",
        freeArgs = true
)
public class br {
    @Default
    public static void Default(Command cmd){
        if(!cmd.getGuild().getId().equals(Config.GUILD_ID.get())){
            if(!cmd.isOwner()){
                cmd.allow = false;
                return;
            }
        }
        Guild g = Core.jda.getGuildById(Config.GUILD_ID.get());
        Role r = g.getRoleById(Config.BRAZIL_ROLE_ID.get());
        Member m = g.getMember(cmd.getSender());
        if(m.getRoles().contains(r)){
            g.getController().removeSingleRoleFromMember(m, r).queue();
        }else{
            g.getController().addSingleRoleToMember(m, r).queue();
        }
        cmd.reply("Your roles had been changed successfully!");
    }

}

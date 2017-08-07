package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Arg;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Core;
import net.dv8tion.jda.core.entities.Member;

import java.util.ArrayList;

/**
 * Created by nathanpb on 8/2/17.
 */
@Cmd(
        cmd = "voidlings",
        usage = "voidlings <arg>",
        description = "Manages all about registered Voidlings",
        allow = {Permission.MOD, Permission.ADMIN}
)
public class Voidlings {
    @Arg(
            arg = "list",
            allow = Permission.ADMIN,
            usage = "voidlings list",
            description = "List all registered Voidlings"
    )
    public static void list(Command cmd){
        String s = "";
        ArrayList<String> ids = new ArrayList<>();
        for(Member m : Core.jda.getGuildById(Config.GUILD_ID.get()).getMembers()){
            if(Permission.VOID_APPRENDICE.has(m.getUser())){
                ids.add(m.getUser().getId());
            }
        }
        if(ids.size() > 50){
            for(String s2 : ids){
                s += Core.jda.getUserById(s2).getName()+", ";
            }

        }else{
            for(String s2 : ids) {
                s += Core.jda.getUserById(s2).getAsMention()+", ";
            }
        }
        cmd.getChannel().sendMessage("Voidlings: "+s).queue();
    }
    /*
    @Arg(
            arg = "kick",
            usage = "voidlings kick @Roles @Members",
            description = "Kick a voidling",
            allow = Permission.ADMIN
    )
    public static void desalistar(Command cmd){
        List<User> list = DiscordUtils.getMentionedUsers(cmd.getMessage());
        for(User u : list){
            Permissions.removePerm(u, Permission.VOID_APPRENDICE);
            GuildController gc = new GuildController(Core.jda.getRoleById(Config.APPRENTICE_ROLE_ID.get()).getGuild());
            gc.removeRolesFromMember(gc.getGuild().getMember(u), Core.jda.getRoleById(Config.APPRENTICE_ROLE_ID.get())).queue();
            ProjectMetadataObject profile = new ProjectMetadataObject(u.getId());
            profile.removeKey("listing_info");
            profile.removeKey("alistado");
            profile.removeKey("recruiter");
            cmd.getChannel().sendMessage("O soldado " + u.getAsMention() + " foi demitido!").queue();
        }
    }
    */
    @Arg(
            arg = "size",
            description = "Shows the registered Voidlings size",
            usage = "voidlings size"
    )
    public static void size(Command cmd){
        int i = 0;
        for(Member m : Core.jda.getGuildById(Config.GUILD_ID.get()).getMembers()){
            if(Permission.getPerms(m.getUser()).contains(Permission.VOID_APPRENDICE)){
                i++;
            }
        }
        cmd.getChannel().sendMessage("Voidlings: "+i).queue();
    }
}

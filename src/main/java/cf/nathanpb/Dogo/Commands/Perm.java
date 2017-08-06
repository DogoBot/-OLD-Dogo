package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Arg;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Core;
import cf.nathanpb.Dogo.Permissions;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathanpb on 7/27/17.
 */
@Cmd(
        cmd = "Perm",
        usage = "perm <arg>",
        description = "Manange the permission system",
        allow = {Permission.ADMIN},
        argslengh = 1
)
public class Perm {
    public static List<User> listAllUsers(Message msg){
        List<User> list = new ArrayList<>();
        list.addAll(msg.getMentionedUsers());
        for (Role r : msg.getMentionedRoles()) {
            for (Member m : msg.getGuild().getMembersWithRoles(r)) {
                list.add(m.getUser());
            }
        }
        return list;
    }

    @Arg(
            arg = "add",
            usage = "perm add PERMISSION_NAME @Members @Roles",
            description = "Add a permission to members",
            allow = Permission.ADMIN
    )
    public static void add(Command c){
        for (User u : listAllUsers(c.getMessage())) {
            for (Permission p : Permission.values()) {
                if (c.getArg(1).equalsIgnoreCase(p.name())) {
                    if(Permissions.addPerm(u, p)) {
                        c.getChannel().sendMessage("Permission **" + p.name() + "** given to " + u.getAsMention()).queue();
                    }else{
                        c.getChannel().sendMessage("Wasn't possible give this perm to "+u.getAsMention()+". Maybe she/he already have it!").queue();
                    }
                }
            }
        }
    }

    @Arg(
            arg = "list",
            usage = "perm list PERMISSION_NAME",
            description = "List all the permissions that a member have",
            allow = Permission.ADMIN
    )
    public static void list(Command c){
        for(User u : listAllUsers(c.getMessage())){
            c.getChannel().sendMessage("**"+u.getName()+"'s** perms: "+Permissions.getPerms(u)).queue();
        }
    }

    @Arg(
            arg = "remove",
            usage = "perm remove PERMISSION_NAME @Members @Roles",
            description = "Removes member's permission",
            allow = Permission.ADMIN
    )
    public static void remove(Command c){
        for (User u : listAllUsers(c.getMessage())) {
            for (Permission p : Permission.values()) {
                if (c.getArg(1).equalsIgnoreCase(p.name())) {
                    if(Permissions.removePerm(u, p)) {
                        c.getChannel().sendMessage("Permission **" + p.name() + "** got removed of " + u.getAsMention()).queue();
                    }else{
                        c.getChannel().sendMessage("Wasn't possible remove this perm from "+u.getAsMention()+". Maybe he/she doesn't have it!").queue();
                    }
                }
            }
        }
    }

    @Arg(
            arg = "listall",
            usage = "perm listall PERMISSION_NAME",
            description = "Shows all members with the specified permission",
            allow = Permission.ADMIN
    )
    public static void listall(Command c){
        String s = "";
        for (Member m : Core.jda.getGuildById(Config.GUILD_ID.get(String.class)).getMembers()) {
            for (Permission p : Permission.values()) {
                if (p.name().equalsIgnoreCase(c.getArg(1))) {
                    if (Permissions.getPerms(m.getUser()).contains(p)) {
                        s+=m.getUser().getAsMention()+", ";
                    }
                }
            }
        }
        c.getChannel().sendMessage(s).queue();
    }

    @Arg(
            arg = "removeall",
            usage = "perm listall PERMISSION_NAME",
            description = "Remove the specified perms from all members",
            allow = Permission.ADMIN
    )
    public static void removeall(Command c){
        for (Member m : Core.jda.getGuildById(Config.GUILD_ID.get(String.class)).getMembers()) {
            for (Permission p : Permission.values()) {
                if (p.name().equalsIgnoreCase(c.getArg(1))) {
                    if (Permissions.getPerms(m.getUser()).contains(p)) {
                        Permissions.removePerm(m.getUser(), p);
                       c.getChannel().sendMessage("Permission **" + p + "** got removed from " + m.getAsMention()).queue();
                    }
                }
            }
        }
    }
}

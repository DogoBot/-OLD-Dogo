package cf.nathanpb.Dogo.CommandHandler.enums;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Arg;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.Config;
import me.nathanpb.ProjectMetadata.ProjectMetadataObject;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nathanpb on 7/26/17.
 */
public enum Permission {
    OWNER(),
    ADMIN(),
    MOD(),
    DEVELOPER(),
    VOIDLING(),
    VOID_APPRENDICE(),
    COMMON();
    public static ArrayList<Permission> getPerms(User u){
        ArrayList<Permission> perms = new ArrayList<>();
        ProjectMetadataObject proj = new ProjectMetadataObject(u.getId());
        if(!proj.hasKey("perms")){
            return perms;
        }
        ArrayList<String> string = new ProjectMetadataObject(u.getId()).getAsList("perms", String.class);
        for(Permission p: Permission.values()){
            if(string.contains(p.name())) perms.add(p);
        }
        return perms;
    }
    public boolean add(User u){
        if(this.equals(Permission.OWNER)) return false;
        ArrayList<Permission> perms = getPerms(u);
        boolean b = !perms.contains(this);
        if(!perms.contains(this)){
            perms.add(this);
        }
        ArrayList<String> toPut = new ArrayList<>();
        for(Permission p2 : perms){
            toPut.add(p2.name());
        }
        new ProjectMetadataObject(u.getId()).put("perms", toPut);
        return b;
    }
    public boolean remove(User u){
        ArrayList<Permission> perms = getPerms(u);
        boolean b = perms.contains(this);
        if(perms.contains(this)){
            perms.remove(this);
        }
        ArrayList<String> toPut = new ArrayList<>();
        for(Permission p2 : perms){
            toPut.add(p2.name());
        }
        new ProjectMetadataObject(u.getId()).put("perms", toPut);
        return b;
    }
    public boolean has(User u){
        if(this.equals(Permission.OWNER)) return u.getId().equals(Config.OWNER_ID.get());
        return getPerms(u).contains(this);
    }
    public static boolean canExecute(Command c, Cmd cmd){
        return canExecute(c, cmd.allow(), cmd.disallow());
    }
    public static boolean canExecute(Command c, Arg cmd){
        return canExecute(c, cmd.allow(), cmd.disallow());
    }
    public static boolean canExecute(Command c, Permission[] a, Permission[] d){
        if(c.isOwner()) return true;
        List<Permission> allow = Arrays.asList(a);
        List<Permission> disallow = Arrays.asList(d);
        if (allow.size() == 1 && allow.contains(Permission.OWNER)) return c.getSender().getId().equals(Config.OWNER_ID.get());
        boolean b = allow.contains(Permission.COMMON);
        for(Permission p : getPerms(c.getSender())){
            if(allow.contains(p)) b = true;
            if(disallow.contains(p)) return false;
        }
        return b;
    }
}

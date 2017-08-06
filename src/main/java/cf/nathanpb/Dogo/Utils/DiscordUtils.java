package cf.nathanpb.Dogo.Utils;

import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Core;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathanpb on 7/27/17.
 */
public class DiscordUtils {
    public static User getUserByURL(String url) {
        String ID = url.replace("http://", "");
        ID = ID.replace(".me", "");
        return Core.jda.getUserById(ID);
    }
    public static String getURLWithID(User user) {
        return "http://" + user.getId() + ".me";
    }
    public static String getAvatarURL(User user) {
        if (user.getAvatarUrl() == null) {
            return Config.DEFAULT_AVATAR_URL.get(String.class);
        } else {
            return user.getAvatarUrl();
        }
    }
    public static List<User> getMentionedUsers(Message msg){
        List<User> list = new ArrayList<>(msg.getMentionedUsers());
        for(Role r : msg.getMentionedRoles()){
            for(Member m : msg.getGuild().getMembers()){
                if (!list.contains(m.getUser()) && m.getRoles().contains(r)) {
                    list.add(m.getUser());
                }
            }
        }
        if(msg.mentionsEveryone()) {
            for (Member m : msg.getGuild().getMembers()) {
                if(!list.contains(m.getUser())) {
                    list.add(m.getUser());
                }
            }
        }
        return list;
    }
    public static void sendPrivateMessage(User u, String s) {
        u.openPrivateChannel().complete().sendMessage(s).complete().getPrivateChannel().close();
    }
}

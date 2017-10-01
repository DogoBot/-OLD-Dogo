package cf.nathanpb.Dogo;

import me.nathanpb.ProjectMetadata.ProjectMetadataObject;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by nathanpb on 7/27/17.
 */
public class EXP extends ListenerAdapter{
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if(event.getGuild() == null){
            return;
        }
        if(event.getGuild().getId().equals("140137593412124672") && event.getAuthor().getId().equals("140137402592133120")){
            if(event.getMessage().getContent().contains("terminar")) event.getChannel().sendMessage("@kakeenho vai terminar a rainha").queue();
        }
        if(!event.getGuild().getId().equals(Config.GUILD_ID.get(String.class))
                || event.getAuthor().isBot()
                || event.getAuthor().isFake()){
            return;
        }
        ArrayList<String> words = new ArrayList<>();
        for(String s : event.getMessage().getContent().split(" ")){
            if(!(s.equals(" ") || s.equals(""))){
                words.add(s);
            }
        }
        addExp(event.getAuthor(), words.size());
        updateLastMessage(event.getAuthor());
    }
    public static void addExp(User u, long exp){
        long atual = getExp(u) + exp;
        long expm = 0;
        while(atual > 1000){
            atual -= 1000;
            expm++;
        }
        new ProjectMetadataObject(u.getId()).put("exp", atual);
        new ProjectMetadataObject(u.getId()).put("expm", expm);
    }
    public static void removeExp(User u, long exp){
        new ProjectMetadataObject(u.getId()).put("exp", getExp(u)-exp);
    }
    public static long getExp(User u){
        ProjectMetadataObject o = new ProjectMetadataObject(u.getId());
        if(!o.hasKey("exp")) o.put("exp", 0);
        if(!o.hasKey("expm")) o.put("expm", 0);
        return o.get("exp", Integer.class)+(o.get("expm", Integer.class)*1000);
    }
    public static void updateLastMessage(User u){
        JSONObject time = new JSONObject();
        Date c = Calendar.getInstance(TimeZone.getTimeZone("UTC+0")).getTime();
        time.put("day", new SimpleDateFormat("dd").format(c));
        time.put("month", new SimpleDateFormat("MM").format(c));
        time.put("year", new SimpleDateFormat("yyyy").format(c));
        time.put("hour", new SimpleDateFormat("HH").format(c));
        time.put("minute", new SimpleDateFormat("mm").format(c));
        time.put("second", new SimpleDateFormat("ss").format(c));
        new ProjectMetadataObject(u.getId()).put("lastmsg", time);
    }
    public static JSONObject getLastMessage(User u){
        return new ProjectMetadataObject(u.getId()).get("lastmsg", JSONObject.class);
    }
}

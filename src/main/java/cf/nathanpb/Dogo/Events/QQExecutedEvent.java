package cf.nathanpb.Dogo.Events;

import cf.nathanpb.Dogo.CommandHandler.QuickCommand;
import cf.nathanpb.Dogo.Logger;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.json.JSONObject;

/**
 * Created by nathanpb on 7/28/17.
 */
public class QQExecutedEvent extends BotEvent{
    String name;
    JSONObject object;
    MessageChannel channel;
    User u;
    public QQExecutedEvent(QuickCommand qq){
        this.name = qq.getName();
        this.object = qq.getObject();
        this.channel = qq.getChannel();
        this.u = qq.getSender();
        Logger.log(this);
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public String getName() {
        return name;
    }

    public JSONObject getObject() {
        return object;
    }

    public User getUser() {
        return u;
    }
}

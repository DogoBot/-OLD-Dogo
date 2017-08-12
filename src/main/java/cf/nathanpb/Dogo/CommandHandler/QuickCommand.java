package cf.nathanpb.Dogo.CommandHandler;

import cf.nathanpb.Dogo.CommandHandler.enums.Parameters;
import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Core;
import cf.nathanpb.Dogo.Events.QQExecutedEvent;
import cf.nathanpb.Dogo.JsonMessage;
import cf.nathanpb.Dogo.Logger;
import cf.nathanpb.Dogo.Utils.DiscordUtils;
import cf.nathanpb.Dogo.Utils.HastebinUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by nathanpb on 8/11/17.
 */
public class QuickCommand {
    private User sender;
    private String qqName;
    private List<Parameters> parameters = new ArrayList<>();
    private String raw;
    private JSONObject object;
    private MessageChannel channel;
    private Message message;

    public QuickCommand(Message msg){
        try {
            this.raw = msg.getContent();
            this.sender = msg.getAuthor();
            this.message = msg;
            this.channel = msg.getChannel();
            this.qqName = msg.getContent().split(" ")[0].replace(Config.COMMAND_PREFIX.get(String.class), "");
            for (Parameters p : Parameters.values()) {
                String want = " " + Config.PARAMETHER_PREFIX.get(String.class) + p.getId();
                if (raw.contains(want)) {
                    parameters.add(p);
                    raw = raw.replace(want, "");
                }
            }
            this.object = cf.nathanpb.Dogo.Commands.QuickCommand.getAll().get(qqName);
            Executors.newSingleThreadExecutor().submit(() -> {
                String s = cf.nathanpb.Dogo.Commands.QuickCommand.exec(this.object, msg.getChannel(), msg.getAuthor(), raw);
                try {
                    new JsonMessage(new JSONObject(s)).send(msg.getChannel());
                } catch (Exception e) {
                    msg.getChannel().sendMessage(s).queue();
                }
            });
            if (this.parameters.contains(Parameters.DELETE)) {
                try {
                    msg.delete().complete();
                } catch (Exception e) {
                }
            }
            new QQExecutedEvent(this);
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.getCause().printStackTrace(pw);
            DiscordUtils.sendPrivateMessage(Core.jda.getUserById(Config.OWNER_ID.get()), "An Exception occurred: ``" + e.getCause().getClass().getSimpleName() + "``, " + HastebinUtils.getUrl(HastebinUtils.upload(sw.toString()), false));
            this.getChannel().sendMessage("An ``" + e.getCause().getClass().getSimpleName() + "`` occurred!").queue();
        }
    }
    public static void checkQuickCommand(Message msg){
        if (msg.getContent().startsWith(Config.COMMAND_PREFIX.get(String.class))) {
            String cmdname = msg.getContent().split(" ")[0].replace(Config.COMMAND_PREFIX.get(String.class), "");
            if (cf.nathanpb.Dogo.Commands.QuickCommand.getAll().containsKey(cmdname)) {
                new QuickCommand(msg);
            }
        }
    }

    public User getSender() {
        return sender;
    }

    public String getName() {
        return qqName;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public Message getMessage() {
        return message;
    }

    public List<Parameters> getParameters() {
        return parameters;
    }

    public JSONObject getObject() {
        return object;
    }

    public String getRaw() {
        return raw;
    }
}

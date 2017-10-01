package cf.nathanpb.Dogo.CommandHandler;

import cf.nathanpb.Dogo.CommandHandler.enums.Parameters;
import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Core;
import cf.nathanpb.Dogo.Events.QQExecutedEvent;
import cf.nathanpb.Dogo.Exceptions.EvalException;
import cf.nathanpb.Dogo.JsonMessage;
import cf.nathanpb.Dogo.Logger;
import cf.nathanpb.Dogo.Utils.DiscordUtils;
import cf.nathanpb.Dogo.Utils.HastebinUtils;
import cf.nathanpb.Dogo.Utils.JavaUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import org.json.JSONObject;

import java.awt.*;
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
                try {
                    cf.nathanpb.Dogo.Commands.QuickCommand.exec(this.object, this);
                }catch (EvalException e) {
                    EmbedBuilder s = new EmbedBuilder();
                    s.setColor(Color.RED);
                    s.setTitle(e.getMessage());
                    String s2 = e.getError();
                    s2.replace(cf.nathanpb.Dogo.Config.TOKEN.get(), "<BOT_TOKEN>");
                    if (s2.length() > 500) {
                        s2 = HastebinUtils.getUrl(HastebinUtils.upload(s2), false);
                    } else {
                        s2 = "```" + s2 + "```";
                    }
                    s.setDescription(s2);
                    reply(s.build());
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

    public void reply(JSONObject o) {
        reply(o.toString());
    }

    public void reply(String s) {
        try {
            new JsonMessage(s).send(channel);
        } catch (Exception e) {
            channel.sendMessage(s).complete();
        }
    }

    public void reply(MessageEmbed embed) {
        channel.sendMessage(embed).queue();
    }
}

package cf.nathanpb.Dogo;

import cf.nathanpb.Dogo.Events.*;
import cf.nathanpb.Dogo.Utils.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;

/**
 * Created by nathanpb on 7/27/17.
 */
public class Logger {
    public static void log(BotEvent o) {
        try {
            MessageChannel channel = Core.jda.getTextChannelById(Config.COMMON_LOGGER_CHID.get(String.class));

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(o.getClass().getSimpleName() + ":");
            embed.setColor(Color.GREEN);
            String texto = "";
            if (o instanceof CommandExecutedEvent) {
                CommandExecutedEvent e = (CommandExecutedEvent) o;
                String sender = "**Sender:** " + e.getCmd().getSender().getAsMention();
                String comando = "**Command:** " + e.getCmd().getCmdName();
                String args = "**Args:** " + e.getCmd().getArgs();
                String paramethers = "**Parameters:** " + e.getCmd().getParameters();
                String perm = "**Permissions allowed:** " + e.getCmd().getAllow();
                String raw = "**Raw:** ``" + e.getCmd().getRaw() + "``";
                String channelName = "**Channel: **<#" + e.getCmd().getChannel().getId() + ">";
                texto = sender + "\n" + comando + "\n" + args + "\n" + paramethers + "\n" + perm + "\n" + raw + "\n" + channelName;

                embed.setFooter(o.time, DiscordUtils.getAvatarURL(e.getCmd().getSender()));
            }
            if (o instanceof QQExecutedEvent) {
                QQExecutedEvent e = (QQExecutedEvent) o;
                String sender = "**Sender: **" + e.getUser().getAsMention() + "\n";
                String comando = "**Command: **" + e.getName() + "\n";
                String src = "**Source: **" + e.getObject().get("src") + "\n";
                String ch = "**Channel: **<#" + e.getChannel().getId() + ">\n";
                texto = sender + comando + src + ch;
                embed.setFooter(o.time, DiscordUtils.getAvatarURL(e.getUser()));
            }
            if (o instanceof SoldierRecrutedEvent) {
                SoldierRecrutedEvent e = (SoldierRecrutedEvent) o;

                String a = "";
                String d = "";
                for(User u : e.getAccept()){
                    a+=u.getAsMention()+", ";
                }
                for(User u : e.getDeny()){
                    d+=u.getAsMention()+", ";
                }

                String aceito = "**Accepted:** " + e.getAccepted();
                String recrutado = "**Who applied:** " + e.getRecruted().getAsMention();
                String recrutador = "**Who make the decision: **Accept:"+a+"  |  Deny: "+d;
                texto = aceito + "\n" + recrutado + "\n" + recrutador;

               embed.setFooter(o.time, Config.DEFAULT_AVATAR_URL.get());
            }
            if(o instanceof FormSentToRecruiterEvent){
                FormSentToRecruiterEvent e = (FormSentToRecruiterEvent)o;

                texto = "**User: **" + e.getUser().getAsMention();
                embed.setAuthor(e.getUser().getName(), DiscordUtils.getURLWithID(e.getUser()), DiscordUtils.getAvatarURL(e.getUser()));
            }
            if (o instanceof TriedToEnlistEvent) {
                TriedToEnlistEvent e = (TriedToEnlistEvent) o;
                if (e.getCanEnlist()) {
                    texto = e.getTarget().getAsMention() + " is trying to enlist... Sending form...";
                } else {
                    texto = e.getTarget().getAsMention() + " tried to enlist, but he is already enlisted. Sending message...";
                }
            }
            embed.setDescription(texto.replace(Config.TOKEN.get(String.class), "<BOT_TOKEN>"));
            MessageEmbed embed1 = embed.build();
            channel.sendMessage(embed1).queue();
            for(MessageChannel ch : o.loggers){
                ch.sendMessage(embed1).queue();
            }
        } catch (Exception e) {
            System.out.println("An error ocoured while logging information:");
            e.printStackTrace();
        }
    }
}

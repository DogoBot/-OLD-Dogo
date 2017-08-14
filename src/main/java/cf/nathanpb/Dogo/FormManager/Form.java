package cf.nathanpb.Dogo.FormManager;

import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Core;
import cf.nathanpb.Dogo.Events.FormSentToRecruiterEvent;
import cf.nathanpb.Dogo.Events.SoldierRecrutedEvent;
import cf.nathanpb.Dogo.Logger;
import cf.nathanpb.Dogo.Utils.DiscordUtils;
import me.nathanpb.ProjectMetadata.ProjectMetadataObject;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nathanpb on 7/1/17.
 */
public class Form implements EventListener {
    private static String intro = "Please, answer this form to realize your verification";
    private static String[] perguntas = new String[]{
            "1) How did you get here?",
            "2) What's your intention with joining this server?",
            "3) Why do you think we shouldn't consider you a spy or a griefer??",
            "4) Reddit account username (you don't have to have one)?",
            "5) How old are you?",
            "6) How active do you intend to be? (You need to be active in order for your rank to stay where it's at.)",
            "7) For how long have you been playing pixelcanvas?",
            "8) Were/are you a part of any other faction?",
            "9) Are you aware of the Void alliances and non-aggression pacts? If not, check the #faq channel.",
            "10) Do you agree not to attack our allies and NAP's?"};
    private User target;
    private PrivateChannel channel;
    private ProjectMetadataObject profile;

    public Form(User target) {
        this.target = target;
        this.channel = target.openPrivateChannel().complete();
        profile = new ProjectMetadataObject(target.getId());
        channel.sendMessage(intro).queue();
        nextQuestion();
    }
    private Message lastMsg(int i) {
        ArrayList<Message> list = new ArrayList<>();
        list.addAll(channel.getHistory().retrievePast(i).complete());
        return list.get(list.size() - 1);
    }
    private void nextQuestion() {
        channel.sendMessage(perguntas[this.getRespostas().size()]).queue();
    }
    private ArrayList<String> getRespostas() {
        if (!profile.hasKey("form") || profile.getAsList("form", Object.class).isEmpty()) {
            return new ArrayList<>();
        }
        return  profile.getAsList("form", String.class);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent e = (MessageReceivedEvent) event;
            if (!e.getMessage().getAuthor().getId().equals(Core.jda.getSelfUser().getId()) && e.getChannel().equals(this.channel)){
                if(profile.get("alistado", Boolean.class)){
                    channel.sendMessage("Your profile was already sent! Just wait. If you get promoted, you will be notified.").queue();
                    return;
                }
                if (getRespostas().size() <= perguntas.length && lastMsg(2).getAuthor().equals(Core.jda.getUserById(Core.jda.getSelfUser().getId()))){
                    List<String> array = getRespostas();
                    array.add(e.getMessage().getContent());
                    profile.put("form", array);
                    if(getRespostas().size() < perguntas.length) {
                        nextQuestion();
                    }
                }
                if(getRespostas().size() == perguntas.length){
                    channel.sendMessage("Your profile has been submitted for review!").queue();
                    sendToRecruiters(getRespostas(), e.getAuthor());
                }
            }
        }
    }
    public static void sendToRecruiters(ArrayList<String> ans, User owner) {
        String text = "";
        int counter = 0;
        for(String s : perguntas){
            text +="**"+s+"**\n";
            text += ans.get(counter)+"\n";
            counter++;
        }
         text +="**'Listing' date: **"+new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()) + " | **GMT: **" + new SimpleDateFormat("z").format(Calendar.getInstance().getTime());
        EmbedBuilder embed = new EmbedBuilder();
        try {
            TextChannel channel = Core.jda.getTextChannelById(Config.LISTING_CHANNEL_ID.get());
            embed.setAuthor(owner.getName(), DiscordUtils.getURLWithID(owner), DiscordUtils.getAvatarURL(owner));
            embed.setColor(Color.GREEN);
            embed.setTitle(owner.getName() + "'s profile:", Config.DEFAULT_AVATAR_URL.get());
            embed.setDescription(text);
            embed.setFooter("Developed NathanPB", "https://cdn.discordapp.com/avatars/214173547965186048/4758761393318753f20170cbce6dd6de.png");
            new ProjectMetadataObject(DiscordUtils.getUserByURL(embed.build().getAuthor().getUrl()).getId()).put("alistado", true);
            Message msg = channel.sendMessage(embed.build()).complete();
            msg.addReaction("✅").complete();
            msg.addReaction("❎").complete();
            Logger.log(new FormSentToRecruiterEvent(owner));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void registerSoldier(User u, MessageEmbed msg, User recruiter) {
        Permission.VOID_APPRENDICE.add(u);
        DiscordUtils.sendPrivateMessage(DiscordUtils.getUserByURL(msg.getAuthor().getUrl()), "You got accepted as an Void Apprentice!");
        ProjectMetadataObject profile = new ProjectMetadataObject(u.getId());
        String s = msg.getDescription();
        s+="\n\n"+"**Accepted date: **"+new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()) + " | **GMT: **" + new SimpleDateFormat("z").format(Calendar.getInstance().getTime());;
        profile.put("listing_info", s);
        profile.removeKey("form");
        profile.put("recruiter", recruiter.getId());
    }
    public static void recruiterOrder(boolean accept, Message m, User rec) {
        MessageEmbed embed = m.getEmbeds().get(0);
        Role r1 = Core.jda.getRoleById(Config.APPRENTICE_ROLE_ID.get());
        Role r2 = Core.jda.getRoleById(Config.NEWCOMER_ROLE_ID.get());
        Member member = m.getGuild().getMember(DiscordUtils.getUserByURL(embed.getAuthor().getUrl()));
        if(member == null){
            m.editMessage("Member "+DiscordUtils.getUserByURL(embed.getAuthor().getUrl()).getAsMention()+" left the guild, isn't possible to apply any order!").queue();
            return;
        }
        int acc = 0;
        int den = 0;
        List<User> acceptors = new ArrayList<>();
        List<User> deniers = new ArrayList<>();
        for(MessageReaction reaction : m.getReactions()){
            if(reaction.getEmote().getName().equals("❎") || reaction.getEmote().getName().equals("✅")){
                for(User r : reaction.getUsers()){
                    if(!(r.isBot() || r.isFake())){
                        if(reaction.getEmote().getName().equals("❎")) {
                            deniers.add(r);
                            den++;
                        }
                        if (reaction.getEmote().getName().equals("✅")){
                            acceptors.add(r);
                            acc++;
                        }
                    }
                }
            }
        }
        if(accept && acc < Config.ACCEPT_VOTES.get(Integer.class)) return;
        if(!accept && den < Config.DENY_VOTES.get(Integer.class)) return;
        if (accept) {
            m.getGuild().getController().removeSingleRoleFromMember(member, r2).complete();
            m.getGuild().getController().addSingleRoleToMember(member, r1).complete();
            registerSoldier(DiscordUtils.getUserByURL(embed.getAuthor().getUrl()), embed, rec);
        }
        m.delete().complete();
        Logger.log(new SoldierRecrutedEvent(acceptors, deniers, member.getUser(), accept));
    }

}

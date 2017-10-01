package cf.nathanpb.Dogo;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.Commands.QuickCommand;
import cf.nathanpb.Dogo.FormManager.FormManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathanpb on 7/26/17.
 */
public class Core extends ListenerAdapter{
    public static JDA jda = null;
    public static void main(String[] args) throws Exception{
        jda = new JDABuilder(AccountType.BOT).setToken(Config.TOKEN.get(String.class)).addEventListener(new Core()).buildBlocking();
        jda.addEventListener(new EXP());
        jda.addEventListener(new FormManager());
        jda.addEventListener(new QuickCommand());
        jda.addEventListener(new RecruiterChannelCleaner());
        jda.getPresence().setGame(Game.of("over rewriting"));
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        super.onMessageReceived(e);
        Command.checkCommand(e.getMessage());
    }
    public static int getUptimeHours(){
        return (int) TimeUnit.MILLISECONDS.toHours(ManagementFactory.getRuntimeMXBean().getUptime());
    }
    public static int getUptimeMinutes(){
        return (int) TimeUnit.MILLISECONDS.toMinutes(ManagementFactory.getRuntimeMXBean().getUptime());
    }
}

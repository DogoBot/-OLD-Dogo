package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Arg;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Core;
import cf.nathanpb.Dogo.Utils.DiscordUtils;
import cf.nathanpb.Dogo.Utils.SystemUtils;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by nathanpb on 8/2/17.
 */
@Cmd(
        cmd = "bot",
        description = "Shows info about Dogo",
        usage = "stats",
        freeArgs = false,
        argslengh = 1
)
public class Bot {
    @Arg(
            arg = "stats",
            usage = "bot stats",
            description = "Shows info about Dogo's system"
    )
    public static void Default(Command cmd){
        EmbedBuilder embed = new EmbedBuilder();

        String uptime = "**-> Uptime: **\n   **>AsHours:** " + Core.getUptimeHours() + "\n   **>AsMinutes:** " + Core.getUptimeMinutes();
        String memory = "**-> Memory: **\n   **>Max:** " + Runtime.getRuntime().totalMemory() / (1024 * 1024) + "MB\n   **>Free:**" + Runtime.getRuntime().freeMemory() / (1024 * 1024) + "MB\n   **>Used: **" + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024) + "MB";
        String cpu = "**CPU Usage: **"+getUsage();
        String ping = "**Ping: **"+Core.jda.getPing()+"ms";
        embed.setColor(Color.GREEN);
        embed.setAuthor(Core.jda.getSelfUser().getName(), DiscordUtils.getAvatarURL(Core.jda.getUserById(Core.jda.getSelfUser().getId())), DiscordUtils.getAvatarURL(Core.jda.getUserById(Core.jda.getSelfUser().getId())));
        embed.setTitle("Bot Status:");
        embed.setDescription(uptime + "\n\n" + memory+"\n\n"+cpu+"\n\n"+ping);
        embed.setFooter("Developed by NathanPB", DiscordUtils.getAvatarURL(Core.jda.getUserById(Config.OWNER_ID.get())));

        cmd.getChannel().sendMessage(embed.build()).queue();
    }

    @Arg(
            arg = "update",
            description = "Rebuild Dogo based on GitHub",
            usage = "bot update",
            allow = Permission.OWNER
    )
    public static void Update(Command cmd){
        cmd.reply(SystemUtils.shellCommand("./autobuild.sh"));
        cmd.reply("Updated!");
    }

    @Arg(
            arg = "restart",
            description = "Restart Dogo",
            usage = "bot restart",
            allow = Permission.OWNER
    )
    public static void Restart(Command cmd){
        cmd.reply("Restarting...");
        System.exit(0);
    }
    public static double getUsage() {
        Object value = "";
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("get")
                    && Modifier.isPublic(method.getModifiers())) {
                try {
                    value = method.invoke(operatingSystemMXBean);
                } catch (Exception e) {
                    value = e;
                }
            }
        }
        return Math.round((double)value);
    }
}

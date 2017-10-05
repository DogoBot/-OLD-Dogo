package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.annotations.Default;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Core;
import cf.nathanpb.Dogo.Utils.DiscordUtils;
import me.nathanpb.ProjectMetadata.ProjectMetadataObject;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.util.List;

/**
 * Created by nathanpb on 8/2/17.
 */
@Cmd(
        cmd = "review",
        usage = "review @Members @Roles",
        description = "Shows info about a member",
        allow = Permission.MOD,
        freeArgs = true
)
public class Review {
    @Default
    public static void Default(Command cmd){
        List<User> all = DiscordUtils.getMentionedUsers(cmd.getMessage());
        for(User u : all){
            if (Permission.VOID_APPRENDICE.has(u)) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.GREEN);
                embed.setAuthor(u.getName(), DiscordUtils.getAvatarURL(u), DiscordUtils.getAvatarURL(u));
                embed.setTitle(u.getName()+" info:", "");
                embed.setFooter("Requested by " + cmd.getSender().getName(), DiscordUtils.getAvatarURL(cmd.getSender()));
                String description = new ProjectMetadataObject(u.getId()).get("listing_info", String.class);
                try {
                    description =  "**Accepted by: **"+ Core.jda.getUserById(new ProjectMetadataObject(u.getId()).get("recruiter", String.class)).getAsMention()+"\n\n"+description;
                }catch (Exception e){};
                embed.setDescription(description);
                cmd.getChannel().sendMessage(embed.build()).queue();
            } else {
                cmd.getChannel().sendMessage(u.getAsMention() + " isn't a registered Voidling").queue();
            }
        }
    }
}

package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.annotations.Default;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nathanpb on 8/2/17.
 */
@Cmd(
        cmd = "cleaner",
        usage = "cleaner <how many messages you want to clean (max 100)>",
        description = "Clean messages on the channel",
        allow = Permission.ADMIN,
        freeArgs = true,
        argslengh = 1
)
public class Cleaner {
    @Default
    public static void Default(Command cmd){
        if(StringUtils.isNumeric(cmd.getArg(0))){
            int i = Integer.valueOf(cmd.getArg(0));
            if(i > 100){
                cmd.getChannel().sendMessage("Your arg is greater than 100 :/").queue();
                return;
            }
            List<Message> msgs = new ArrayList<>();
            MessageHistory h = cmd.getChannel().getHistory();
            for(Message m :  h.retrievePast(i).complete()){
                msgs.add(m);
            }
            cmd.getChannel().deleteMessages(msgs).complete();
            cmd.getChannel().sendMessage(msgs.size()+" messages deleted!").queue();
            return;
        }
        cmd.getChannel().sendMessage("Wrong Args! Use ``help`` command to get info").queue();
    }
}

package cf.nathanpb.Dogo.FormManager;


import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Core;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by nathanpb on 8/2/17.
 */
public class FormManager extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        super.onMessageReactionAdd(e);
        if (Permission.getPerms(e.getUser()).contains(Permission.MOD)) {
            if (!e.getUser().getId().equals(Core.jda.getSelfUser().getId())) {
                Message m = e.getTextChannel().getMessageById(e.getMessageId()).complete();
                if (e.getReactionEmote().getName().equals("✅")) {
                    Form.recruiterOrder(true, m, e.getUser());
                }
                if (e.getReactionEmote().getName().equals("❎")) {
                    Form.recruiterOrder(false, m, e.getUser());
                }
            }
        }
    }
}

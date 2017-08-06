package cf.nathanpb.Dogo.Events;

import net.dv8tion.jda.core.entities.User;

/**
 * Created by nathanpb on 7/3/17.
 */
public class FormSentToRecruiterEvent extends BotEvent{
    private User user;
    public FormSentToRecruiterEvent(User user){
        this.user = user;
    }


    public User getUser() {
        return this.user;
    }
}
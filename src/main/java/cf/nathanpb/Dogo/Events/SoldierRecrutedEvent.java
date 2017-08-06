package cf.nathanpb.Dogo.Events;

import cf.nathanpb.Dogo.Config;
import cf.nathanpb.Dogo.Core;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

/**
 * Created by nathanpb on 7/2/17.
 */
public class SoldierRecrutedEvent extends BotEvent{
    private List<User> accept;
    private List<User> deny;
    private User recruted;
    private boolean accepted;

    public SoldierRecrutedEvent(List<User> accept, List<User> deny, User recruited, boolean accepted){
        super();
        this.accept = accept;
        this.recruted = recruited;
        this.accepted = accepted;
        this.deny = deny;
        loggers.add(Core.jda.getTextChannelById(Config.VERIFY_LOGGER_ID.get()));
    }

    public List<User> getAccept(){
        return this.accept;
    }
    public List<User> getDeny(){ return this.deny;}
    public User getRecruted(){
        return this.recruted;
    }
    public boolean getAccepted(){ return this.accepted;}
}

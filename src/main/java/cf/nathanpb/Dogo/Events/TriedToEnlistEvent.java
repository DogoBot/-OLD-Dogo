package cf.nathanpb.Dogo.Events;

import me.nathanpb.ProjectMetadata.ProjectMetadataObject;
import net.dv8tion.jda.core.entities.User;

/**
 * Created by nathanpb on 7/2/17.
 */
public class TriedToEnlistEvent extends BotEvent{
    private User target;
    private boolean canEnlist;

    public TriedToEnlistEvent(User u){
        this.target = u;
        ProjectMetadataObject profile = new ProjectMetadataObject(u.getId());
        if(!profile.hasKey("alistado")) {
            profile.put("alistado", false);
        }
        this.canEnlist = !profile.get("alistado", Boolean.class);
    }
    public User getTarget(){
        return this.target;
    }
    public boolean getCanEnlist(){
        return this.canEnlist;
    }
}

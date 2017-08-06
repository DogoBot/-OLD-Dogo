package cf.nathanpb.Dogo.Events;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.Logger;

/**
 * Created by nathanpb on 7/27/17.
 */
public class CommandExecutedEvent extends BotEvent {
    Command cmd;
    public CommandExecutedEvent(Command c){
        cmd = c;
        Logger.log(this);
    }

    public Command getCmd() {
        return cmd;
    }
}

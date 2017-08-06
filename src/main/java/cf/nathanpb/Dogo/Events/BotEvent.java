package cf.nathanpb.Dogo.Events;

import net.dv8tion.jda.core.entities.MessageChannel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nathanpb on 7/27/17.
 */
public class BotEvent {
    public String time = "Time: " + new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(Calendar.getInstance().getTime()) + " | GMT: " + new SimpleDateFormat("z").format(Calendar.getInstance().getTime());
    public List<MessageChannel> loggers = new ArrayList<>();
}

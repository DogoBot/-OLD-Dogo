package cf.nathanpb.Dogo;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by nathanpb on 7/6/17.
 */
public class RecruiterChannelCleaner implements EventListener {
    @Override
    public void onEvent(Event event) {
        Executors.newSingleThreadExecutor().submit(() -> {
                    if (event instanceof MessageReceivedEvent) {
                        MessageReceivedEvent e = (MessageReceivedEvent) event;
                        if (e.getChannel().getId().equalsIgnoreCase(Config.LISTING_CHANNEL_ID.get())) {
                            MessageChannel channel = Core.jda.getTextChannelById(Config.LISTING_CHANNEL_ID.get());
                            List<Message> msgs = channel.getHistory().retrievePast(Integer.valueOf(Config.LISTING_CHANNEL_MESSAGE_LIMIT.get(Integer.class))).complete();
                            if (msgs.size() >= Config.LISTING_CHANNEL_MESSAGE_LIMIT.get(Integer.class)) {
                                int deleted = 0;
                                channel.sendMessage("Starting auto-cleaning process...").queue();
                                for (Message m : msgs) {
                                    if (m.getEmbeds().size() == 0 || !m.getEmbeds().get(0).getTitle().equals(null) ||!m.getEmbeds().get(0).getTitle().contains("profile")) {
                                        m.delete().complete();
                                        deleted++;
                                    }
                                }
                                channel.sendMessage(deleted + " messages was deleted!").queue();
                            }
                        }
                    }
                }
        );
    }
}

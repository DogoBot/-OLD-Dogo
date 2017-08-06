package cf.nathanpb.Dogo;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;

/**
 * Created by nathanpb on 7/31/17.
 */
public class JsonMessage {
    JSONObject object;
    Message message;
    public JsonMessage(JSONObject object){
        this.object = object;
    }
    public JsonMessage(String json){
        this.object = new JSONObject(json);
    }

    public JsonMessage send(MessageChannel channel){
        if(object.has("message") && object.get("message") instanceof String){
            message = channel.sendMessage(object.getString("message")).complete();
        }
        if(object.has("embed") && object.get("embed") instanceof JSONObject){
            EmbedBuilder embed = new EmbedBuilder();
            JSONObject eo = object.getJSONObject("embed");
            if(eo.has("color") && eo.get("color") instanceof String) embed.setColor(Color.getColor(eo.getString("color")));
            if(eo.has("author") && eo.get("author") instanceof JSONArray)embed.setAuthor(eo.getJSONArray("author").getString(0), eo.getJSONArray("author").getString(1), eo.getJSONArray("author").getString(2));
            if(eo.has("description") && eo.get("description") instanceof String) embed.setDescription(eo.getString("description"));
            if(eo.has("footer") && eo.get("footer") instanceof JSONArray) embed.setFooter(eo.getJSONArray("footer").getString(0), eo.getJSONArray("footer").getString(1));
            if(eo.has("image") && eo.get("image") instanceof String) embed.setImage(eo.getString("image"));
            if(eo.has("thumbnail") && eo.get("thumbnail") instanceof String) embed.setImage(eo.getString("thumbnail"));
            if(eo.has("title")){
                if(eo.get("title") instanceof String) embed.setTitle(eo.getString("title"));
                if(eo.get("title") instanceof JSONArray){
                    if(eo.getJSONArray("title").length() == 1){
                        embed.setTitle(eo.getJSONArray("title").getString(0));
                    }
                    if(eo.getJSONArray("title").length() == 2){
                        embed.setTitle(eo.getJSONArray("title").getString(0), eo.getJSONArray("title").getString(1));
                    }
                }
            }
            message = channel.sendMessage(embed.build()).complete();
        }
        return this;
    }
}

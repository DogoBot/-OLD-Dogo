package cf.nathanpb.Dogo;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
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
    public static JSONObject from(Message msg){
        JSONObject object = new JSONObject();
        if(msg.getEmbeds().size() > 0){
            MessageEmbed embed = (MessageEmbed)msg;
            JSONObject embedObject = new JSONObject();
            if(!embed.getColor().equals(null)) embedObject.put("color", embed.getColor().toString());
            if(!embed.getDescription().equals(null)) embedObject.put("description", embed.getDescription());
            if(!embed.getImage().equals(null)) embedObject.put("image", embed.getImage());
            if(!embed.getThumbnail().equals(null)) embedObject.put("thumbnail", embed.getThumbnail());
            if(!embed.getFooter().equals(null)) embedObject.put("footer", new JSONArray().put(embed.getFooter().getText()).put(embed.getFooter().getText()));
            if(!embed.getAuthor().equals(null)) embedObject.put("author", new JSONArray(embed.getAuthor().getName()).put(embed.getAuthor().getUrl()).put(embed.getAuthor().getIconUrl()));
            if(!embed.getTitle().equals(null)){
                if(!embed.getUrl().equals(null)) {
                    embedObject.put("title", new JSONArray().put(embed.getTitle()).put(embed.getUrl()));
                }
                embedObject.put("title", embed.getTitle());
            }
            object.put("embed", embed);
        }else{
            object.put("message", msg.getContent());
        }
        return object;
    }
}

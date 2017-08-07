package cf.nathanpb.Dogo;

import me.nathanpb.ProjectMetadata.ProjectMetadataObject;

import java.io.File;

/**
 * Created by nathanpb on 7/27/17.
 */
public enum  Config {
    TOKEN("<NO_TOKEN_FOR_YOU ^-^>"),
    OWNER_ID("214173547965186048"),

    COMMAND_PREFIX("!"),
    PARAMETHER_PREFIX("-"),

    DEFAULT_AVATAR_URL("http://i.prntscr.com/hQjOgi7NS3O27FIZBPiGRw.png"),

    LISTING_CHANNEL_ID("<NO_GUILD_FOR_YOU ^-^>"),

    COMMON_LOGGER_CHID("342752317269082113"),
    VERIFY_LOGGER_ID("<NO_GUILD_FOR_YOU ^-^"),

    APACHE_HOME("/var/www/html"),
    WEBSITE_URL("http://localhost/"),

    GUILD_ID("<NO_GUILD_FOR_YOU ^-^"),

    NEWCOMER_ROLE_ID("<NO_GUILD_FOR_YOU ^-^"),
    APPRENTICE_ROLE_ID("<NO_GUILD_FOR_YOU ^-^"),
    VOIDLING_ROLE_ID("<NO_GUILD_FOR_YOU ^-^"),
    BRAZIL_ROLE_ID("<NO_GUILD_FOR_YOU ^-^"),

    ACCEPT_VOTES(2),
    DENY_VOTES(2),

    CONTENTS_PATH(new File(".dynamic")),

    LISTING_CHANNEL_MESSAGE_LIMIT(100);

    ProjectMetadataObject pro = new ProjectMetadataObject("config");
    Config(Object o){
        if(!pro.hasKey(this.name())){
            pro.put(this.name(), o);
        }
    }
    public void set(Object o){
        pro.put(this.name(), o);
    }
    public <T>T get(Class<T> target) {
        return pro.get(this.name(), target);
    }
    public String get() {
        return pro.get(this.name(), String.class);
    }
}

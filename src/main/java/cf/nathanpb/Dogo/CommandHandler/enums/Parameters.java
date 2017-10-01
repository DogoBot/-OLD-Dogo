package cf.nathanpb.Dogo.CommandHandler.enums;

/**
 * Created by nathanpb on 7/26/17.
 */
public enum Parameters {
    OWNER(
            "o",
            new Permission[]{Permission.OWNER},
            new Permission[]{}
    ),
    JAVA(
            "j",
            new Permission[]{Permission.DEVELOPER},
            new Permission[]{}
    ),
    PHP(
            "php",
            new Permission[]{Permission.DEVELOPER},
            new Permission[]{}
    ),
    DELETE(
            "d",
            new Permission[]{Permission.OWNER},
            new Permission[]{}
    );

    String id;
    Permission[] allow;
    Permission[] disallow;

    Parameters(String id, Permission[] allow, Permission[] disallow){
        this.id = id;
        this.allow = allow;
        this.disallow = disallow;
    }

    public String getId() {
        return id;
    }

    public Permission[] getAllow() {
        return allow;
    }

    public Permission[] getDisallow() {
        return disallow;
    }
}

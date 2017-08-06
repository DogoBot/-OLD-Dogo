package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Arg;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Utils.ArgsUtils;

import java.util.Arrays;

/**
 * Created by nathanpb on 7/28/17.
 */
@Cmd(
        cmd = "config",
        description = "Manange the bot configs",
        usage = "Why you want to know? Only me can use this :P",
        allow = Permission.OWNER,
        argslengh = 1

)
public class Config {
    @Arg(
            arg = "list",
            description = "List all available configs",
            usage = "config list"
    )
    public static void list(Command cmd){
        cmd.getChannel().sendMessage("**Configurações disponíveis: **``"+ Arrays.asList(cf.nathanpb.Dogo.Config.values())+"``").queue();
    }
    @Arg(
            arg = "set",
            description = "Changes a config",
            usage = "config CONFIG_NAME value",
            argsLenght = 3
    )
    public static void set(Command cmd){
        try{
            cf.nathanpb.Dogo.Config.valueOf(cmd.getArg(1).toUpperCase()).set(ArgsUtils.merge(cmd.getArgs(), 2));
            cmd.getChannel().sendMessage("Definido com sucesso!");
        }catch (Exception e){
            cmd.getChannel().sendMessage("Esta config não existe!").queue();
        }

    }
    @Arg(
            arg = "get",
            description = "Read the config's value",
            usage = "config get CONFIG_NAME",
            argsLenght = 2
    )
    public static void get(Command cmd){
        try{
            cmd.getChannel().sendMessage(((Object)cf.nathanpb.Dogo.Config.valueOf(cmd.getArg(1).toUpperCase()).get(Object.class)).toString()).queue();
        }catch (Exception e){
            cmd.getChannel().sendMessage("This config does not exists!").queue();
        }
    }
}

package cf.nathanpb.Dogo.Commands;

import cf.nathanpb.Dogo.CommandHandler.Command;
import cf.nathanpb.Dogo.CommandHandler.annotations.Cmd;
import cf.nathanpb.Dogo.CommandHandler.annotations.Default;
import cf.nathanpb.Dogo.CommandHandler.enums.Permission;
import cf.nathanpb.Dogo.Core;
import cf.nathanpb.Dogo.EXP;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.util.*;

/**
 * Created by nathanpb on 7/27/17.
 */
@Cmd(
        cmd = "Top",
        description = "Shows the EXP rank, if used with no args, shows simple statistics",
        usage = "top <INDEX>",
        freeArgs = true,
        argslengh =  0,
        allow = Permission.MOD
)
public class Top {
    @Default
    public static void init(Command cmd){
        if(cmd.getArgs().size() > 0 && StringUtils.isNumeric(cmd.getArg(0)) && Integer.valueOf(cmd.getArg(0)) > 0) sendRank(cmd, Integer.valueOf(cmd.getArg(0)));
        if(cmd.getArgs().size() == 0) sendDefault(cmd);
    }
    public static void sendRank(Command cmd, int rank){
        rank = rank*10;
        TreeMap<Long, User> map = new TreeMap();
        String s = "";
        for(File f : new File("ProjectMetadataDatabase").listFiles()){
            if (f.getName().endsWith(".json")) {
                if(StringUtils.isNumeric(f.getName().replace(".json", ""))) {
                    User u = Core.jda.getUserById(f.getName().replace(".json", ""));
                    if(u != null && !u.isFake() && !u.isBot()) {
                        map.put(EXP.getExp(u), u);
                    }
                }
            }
        }
        int count = 1;
        int pos = 0;
        for(Map.Entry<Long, User> entry : map.descendingMap().entrySet()){
            if(entry.getValue().getId().equals(cmd.getSender().getId())){
                pos = count;
            }
            if(count>rank-10 && count<=rank){
                s+="**"+count+") "+entry.getValue().getName()+": **"+entry.getKey()+"\n";
            }
            count++;
        }
        s+="\n\nYour Rank: "+pos+"  |  Your EXP: "+EXP.getExp(cmd.getSender());

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.yellow);
        embed.setTitle("Ranks:");
        embed.setDescription(s);
        cmd.reply(embed.build());
    }
    public static void sendDefault(Command cmd){
        ArrayList<Map.Entry<Long, User>> exp = new ArrayList<>();
        String s = "";
        TreeMap<Long, User> map = new TreeMap();
        for(File f : new File("ProjectMetadataDatabase").listFiles()){
            if (f.getName().endsWith(".json")) {
                if(StringUtils.isNumeric(f.getName().replace(".json", ""))) {
                    User u = Core.jda.getUserById(f.getName().replace(".json", ""));
                    if(u != null && !u.isFake() && !u.isBot()) {
                        map.put(EXP.getExp(u), u);
                    }
                }
            }
        }
        int count = 1;
        for(Map.Entry<Long, User> entry : map.descendingMap().entrySet()){
            if(exp.size() == 0){
                exp.add(entry);
            }
            if(exp.size() >= 2) {
                exp.remove(1);
            }
            exp.add(entry);
            count++;
        }
        s =   "**EXP RANK:**\n" +
                "   >First: \n" +
                "      >Member: "+exp.get(0).getValue().getName()+"\n"+
                "      >EXP: "+exp.get(0).getKey()+"\n" +
                "      >Rank: 1\n" +
                "    >Last:\n" +
                "      >Member: "+exp.get(1).getValue().getName()+"\n" +
                "      >EXP: "+exp.get(1).getKey()+"\n" +
                "      >Rank: "+count;
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.yellow);
        embed.setTitle("Ranks:");
        embed.setDescription(s);
        cmd.reply(embed.build());
    }
}

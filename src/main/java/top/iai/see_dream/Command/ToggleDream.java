package top.iai.see_dream.Command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import top.iai.see_dream.Events.Dream;

public class ToggleDream extends CommandBase {

    boolean b;

    @Override
    public String getName() {
        return "dream";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.dream.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // 判断命令发送者是否为玩家
        if(sender instanceof EntityPlayer){
            if (args.length < 1) {
            sender.sendMessage(new TextComponentString("无参数，后接true或false"));
            return;
            }
            String a = args[0];
            b = Boolean.parseBoolean(a);
            if (args.length == 7) {
                int[] xyz = new int[6];
                xyz[0] = Integer.parseInt(args[1]);
                xyz[1] = Integer.parseInt(args[2]);
                xyz[2] = Integer.parseInt(args[3]);
                xyz[3] = Integer.parseInt(args[4]);
                xyz[4] = Integer.parseInt(args[5]);
                xyz[5] = Integer.parseInt(args[6]);
                sender.sendMessage(new TextComponentString("dream值为"+" "+b+"变化区域为"+" "+xyz[0]+" "+xyz[1]+" "+xyz[2]+" "+xyz[3]+" "+xyz[4]+" "+xyz[5]));
            }
            else {
                int[] xyz = {-100,-100,-100,100,100,100};
                toggleDream(xyz);
                // 向命令发送者发送消息
                sender.sendMessage(new TextComponentString("dream值为" + " " + b + "默认变化区域"));
            }
        }
        else {
            throw new WrongUsageException("commands.dream.usage");
        }
    }
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
    public void toggleDream(int[] xyz) {
        Dream.dream = b;
        Dream.xyz = xyz;
    }
}

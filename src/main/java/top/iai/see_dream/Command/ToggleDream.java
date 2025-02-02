package top.iai.see_dream.Command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import top.iai.see_dream.Events.Dream;

public class ToggleDream extends CommandBase {
    @Override
    public String getName() {
        return "dream";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/dream - 切换做梦状态（开启或关闭方块生成）";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender.getEntityWorld().isRemote) {
            return;
        }
        toggleDream();
        sender.sendMessage(new TextComponentString("方块生成状态改变"));
    }

    public void toggleDream() {
        Dream.dream = !Dream.dream;
    }
}

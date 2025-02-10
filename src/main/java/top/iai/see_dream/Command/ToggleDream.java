package top.iai.see_dream.Command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import top.iai.see_dream.Events.Dream;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ToggleDream extends CommandBase {

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
        // 命令执行逻辑
        toggleDream();
        sender.sendMessage(new TextComponentString("dream值改变"));
    }
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
    public void toggleDream() {
        Dream.dream = !Dream.dream;
    }
}

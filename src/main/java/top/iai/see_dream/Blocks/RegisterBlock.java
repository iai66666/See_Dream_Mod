package top.iai.see_dream.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import top.iai.see_dream.See_Dream;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = See_Dream.MODID)
public class RegisterBlock {

    // 新建存放所有待注册方块的列表
    public static final List<Block> BLOCK_LIST = new ArrayList<>();

    // 新建方块对象
    public static final Block CONG_YU = new BlockBaseDraw(Material.WOOD, "cong_yu").setHardness(1f).setLightOpacity(0);
    public static final Block JB = new JB(Material.IRON, "jb").setHardness(1f).setLightOpacity(0);

    @SubscribeEvent
    public static void handleBlock(RegistryEvent.Register<Block> event) {
        // 将所有待注册的方块注册
        event.getRegistry().registerAll(BLOCK_LIST.toArray(new Block[0]));
    }
}

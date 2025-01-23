package top.iai.see_dream.Blocks;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import top.iai.see_dream.See_Dream;

@Mod.EventBusSubscriber(modid = See_Dream.MODID)
public class RegisterTileEntity {

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        //示范代码，请勿使用，用于创建方块实体
        //GameRegistry.registerTileEntity(J_B.class,new ResourceLocation(See_Dream.MODID, "j_b"));
    }
}

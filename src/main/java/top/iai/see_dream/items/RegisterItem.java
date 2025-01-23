package top.iai.see_dream.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import top.iai.see_dream.See_Dream;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.iai.see_dream.items.ItemPi.PIX;
import static top.iai.see_dream.items.ItemPi.PI_1;

@Mod.EventBusSubscriber(modid = See_Dream.MODID)
public class RegisterItem {

    public static final List<Item> ITEM_LIST = new ArrayList<>();

    public static final Item NULL = new ItemExe("java.lang.nullpointerexception");
    public static final Item PI_X = new ItemPi("pix", PIX);
    public static final Item PI = new ItemPi("pi", PI_1);

    @SubscribeEvent
    public static void handleItem(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ITEM_LIST.toArray(new Item[0]));
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void modelRegister(ModelRegistryEvent event) {
        for (Item item : ITEM_LIST) {
            ModelLoader.setCustomModelResourceLocation(
                    item,
                    0,
                    new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()),"inventory")
            );
        }
    }
}

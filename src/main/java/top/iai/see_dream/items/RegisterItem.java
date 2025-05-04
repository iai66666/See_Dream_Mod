package top.iai.see_dream.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
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

// 事件订阅者，用于注册类事件
@Mod.EventBusSubscriber(modid = See_Dream.MODID)
public class RegisterItem {

    // 用于存放所有待注册物品的列表
    public static final List<Item> ITEM_LIST = new ArrayList<>();

    // 创建物品对象，并将其添加到ITEM_LIST列表中
    public static final Item NULL = new ItemExe("java.lang.nullpointerexception");
    public static final Item PI_X = new ItemPi("pix", PIX);
    public static final Item PI = new ItemPi("pi", PI_1);
    public static final Item JBMASK = new ItemJBMask(ItemJBMask.JBMASK, EntityEquipmentSlot.HEAD, "jbmask");

    @SubscribeEvent
    public static void handleItem(RegistryEvent.Register<Item> event) {
        // 注册所有待注册的物品
        event.getRegistry().registerAll(ITEM_LIST.toArray(new Item[0]));
    }
    //加入sideonly注解表示该方法只在客户端执行，不加注解服务端运行会崩溃
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void modelRegister(ModelRegistryEvent event) {
        // 为所有待注册的物品设置自定义模型资源位置
        for (Item item : ITEM_LIST) {
            ModelLoader.setCustomModelResourceLocation(
                    item,
                    0,
                    new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()),"inventory")
            );
        }
    }
}

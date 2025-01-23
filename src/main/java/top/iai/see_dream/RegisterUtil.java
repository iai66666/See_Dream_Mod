package top.iai.see_dream;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import top.iai.see_dream.Blocks.RegisterBlock;
import top.iai.see_dream.items.RegisterItem;

public class RegisterUtil {
    public static void initItem(Item item , String name){
        item.setTranslationKey(name);
        item.setRegistryName(name);
        item.setCreativeTab(ModTabs.THE_DREAM);
        RegisterItem.ITEM_LIST.add(item);
    }
    public static void initBlock(Block block , String name){
        block.setRegistryName(name);
        block.setTranslationKey(name);
        block.setCreativeTab(ModTabs.THE_DREAM);
        Item item = new ItemBlock(block);
        item.setMaxStackSize(64);
        RegisterBlock.BLOCK_LIST.add(block);
        initItem(item,name);
    }
}

package top.iai.see_dream;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import top.iai.see_dream.Blocks.RegisterBlock;
import top.iai.see_dream.items.RegisterItem;

public class RegisterUtil {
    public static void initItem(Item item , String name){
        // 设置物品的翻译键
        item.setTranslationKey(name);
        // 设置物品的注册名
        item.setRegistryName(name);
        // 设置物品的创造模式Tab
        item.setCreativeTab(ModTabs.THE_DREAM);
        // 将物品添加到物品注册列表中
        RegisterItem.ITEM_LIST.add(item);
    }

    public static void initBlock(Block block , String name){
        // 设置区方块的注册名
        block.setRegistryName(name);
        // 设置方块的翻译键
        block.setTranslationKey(name);
        // 设置方块的创造模式Tab
        block.setCreativeTab(ModTabs.THE_DREAM);
        // 创建一个新的方块物品，对应方块
        Item item = new ItemBlock(block);
        // 设置物品的最大堆叠数量
        item.setMaxStackSize(64);
        // 将方块添加到方块注册列表中
        RegisterBlock.BLOCK_LIST.add(block);
        // 初始化物品，并将其添加到物品注册列表中
        initItem(item,name);
    }

}

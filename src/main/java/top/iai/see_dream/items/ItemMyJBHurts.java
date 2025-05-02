package top.iai.see_dream.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import top.iai.see_dream.ModTabs;
import top.iai.see_dream.RegisterUtil;

public class ItemMyJBHurts extends ItemArmor {

    public ItemMyJBHurts(ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn, String name) {
        super(materialIn, 5, equipmentSlotIn);
        RegisterUtil.initItem(this,name); // 注册物品
        setCreativeTab(ModTabs.THE_DREAM);
    }
    //盔甲
    public static final ArmorMaterial myJBHurts = EnumHelper.addArmorMaterial(
            "see_dream:MyJBHurts", "textures/models/myjbhurts",
            100, new int[]{78, 78, 78, 78}, 78, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 78F
    ).setRepairItem(new ItemStack(Blocks.END_ROD));


}

package top.iai.see_dream.items;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import top.iai.see_dream.ModTabs;
import top.iai.see_dream.Model.ModelJBMask;
import top.iai.see_dream.RegisterUtil;
import top.iai.see_dream.Sound.SoundRegister;

public class ItemJBMask extends ItemArmor {

    public ItemJBMask(ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn, String name) {
        super(materialIn, 5, equipmentSlotIn);
        RegisterUtil.initItem(this,name); // 注册物品
        setCreativeTab(ModTabs.THE_DREAM);
    }

    // 创建盔甲材质
    public static final ArmorMaterial JBMASK = EnumHelper.addArmorMaterial(
            "see_dream:jbmask", "see_dream:jbmask",
            78, new int[]{78, 78, 78, 78}, 78, SoundRegister.JBMASK, 78F
    ).setRepairItem(new ItemStack(Blocks.END_ROD));

    // 重写方法
    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack,
                                    EntityEquipmentSlot slot, ModelBiped defaultModel) {
        if (slot == EntityEquipmentSlot.HEAD) {
            return new ModelJBMask();
        }
        return null;
    }

}

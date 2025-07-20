package top.iai.see_dream.Entity;


import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;


public class EntityItemAntiBoom extends EntityItem {
    public EntityItemAntiBoom (World worldIn,double x,double y,double z){
        super(worldIn,x,y,z);
    }
    public EntityItemAntiBoom(World world, double x, double y, double z, ItemStack stack) {
        super(world,x,y,z,stack);
    }
    public EntityItemAntiBoom(World worldIn){
        super(worldIn);
    }
    //物品防爆炸，防火代码
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.isExplosion()) {
            return false;
        }
        if (source.isFireDamage()) {
            return false;
        }

        if (source.getDamageType().equals("inFire")){
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }
}

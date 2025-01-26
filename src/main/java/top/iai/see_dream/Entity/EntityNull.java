package top.iai.see_dream.Entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static top.iai.see_dream.items.RegisterItem.NULL;
import static top.iai.see_dream.items.RegisterItem.modelRegister;

public class EntityNull extends EntityThrowable {

    // 构造函数：创建一个由指定实体抛出的Null实体
    public EntityNull(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    /**
     * 处理World#setEntityState方法的状态更新
     * 仅在客户端执行
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            // 在Null实体周围生成8个粒子效果，此处使用ITEM_CRACK粒子效果，并指定NULL物品的ID，虽然粒子效果因未知原因无法正常显示，但仍保留此处代码以供参考
            for (int i = 0; i < 8; ++i) {
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK,
                        this.posX, this.posY, this.posZ,
                        ((double) this.rand.nextFloat() - 0.5D) * 0.08D,
                        ((double) this.rand.nextFloat() - 0.5D) * 0.08D,
                        ((double) this.rand.nextFloat() - 0.5D) * 0.08D,
                        Item.getIdFromItem(NULL));
            }
        }
    }

    /**
     * 当此可投掷实体撞击到方块或实体时调用
     */
    @Override
    protected void onImpact(RayTraceResult result) {
        // 如果撞击到了实体，则对该实体造成最大伤害值（Integer.MAX_VALUE）的伤害
        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.ignoreEntity), (float) Integer.MAX_VALUE);
            if(Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null ){
                Minecraft.getMinecraft().player.sendMessage(ITextComponent.Serializer.jsonToComponent("{\"text\":\"它catch了\"}"));
            }
        }
        // 如果未撞击到任何实体，则抛出NullPointerException异常
        if (result.entityHit == null) {
            throw new NullPointerException("你抛出了一个java.lang.NullPointerException");
        }
        // 如果不是客户端世界，则更新实体状态并销毁此实体
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte) 3);
            this.setDead();
        }
    }
}

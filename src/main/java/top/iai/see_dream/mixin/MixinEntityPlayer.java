package top.iai.see_dream.mixin;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer {

    @Shadow
    public abstract float getEyeHeight();

    @Shadow
    public float cameraYaw;
    protected float cameraPitch = this.cameraPitch;
    protected double PosX = this.PosX;
    protected double PosY = this.PosY;
    protected double PosZ = this.PosZ;
}
    // 获取玩家的视野范围
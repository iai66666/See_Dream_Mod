package top.iai.see_dream.mixin;
// mixins/MyMixinChunkProviderServer.java
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;
import java.util.Objects;

@Mixin(AnvilChunkLoader.class)
public abstract class MyMixinAnvilChunkLoader {

    @Inject(method = "writeChunkToNBT", at = @At("HEAD"), cancellable = true)
    private void onWriteChunkToNBT(Chunk chunkIn, World worldIn, NBTTagCompound compound, CallbackInfo ci) {
        // 获取玩家列表
        List<EntityPlayerMP> players = Objects.requireNonNull(worldIn.getMinecraftServer()).getPlayerList().getPlayers();

        // 遍历所有玩家
        for (EntityPlayerMP player : players) {
            // 计算玩家与当前区块的距离
            double distance = player.getDistanceSq(BlockPos.fromLong(ChunkPos.asLong(chunkIn.x, chunkIn.z)));

            // 获取玩家的视野范围
            int viewDistance = player.server.getPlayerList().getViewDistance() << 4; // 转换为方块距离

            // 如果玩家在视野范围内，不修改区块
            if (distance <= viewDistance * viewDistance) {
                return;
            }
        }

        // 如果没有玩家在视野范围内，随机变化地形
        randomizeTerrain(chunkIn);
    }

    private void randomizeTerrain(Chunk chunk) {
        // 实现地形随机变化的逻辑
        // 例如，修改区块中的方块或生物群系
    }
}

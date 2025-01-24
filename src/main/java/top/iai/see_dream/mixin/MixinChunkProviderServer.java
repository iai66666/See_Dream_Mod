package top.iai.see_dream.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ChunkProviderServer.class)
public abstract class MixinChunkProviderServer implements IChunkProvider {
    // ... 其他代码 ...

    @Inject(method = "loadChunk", at = @At("HEAD"), cancellable = true)
    private void onLoadChunk(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> cir) {
        ChunkProviderServer provider = (ChunkProviderServer) (Object) this;
        World world = provider.world;
        // 检查区块是否在玩家视野内
        if (!isChunkInViewDistance(world, chunkX, chunkZ)) {
            // 加载区块
            Chunk chunk = provider.loadChunk(chunkX, chunkZ);
            if (chunk != null) {
                modifyChunkTerrain(chunk);
                cir.setReturnValue(chunk);
            }
        }
    }

    private boolean isChunkInViewDistance(World world, int chunkX, int chunkZ) {
        for (EntityPlayer player : world.playerEntities) {
            IViewerPositionAndAngles viewer = (IViewerPositionAndAngles) player;
            float[] viewerPosAndAngles = viewer.getViewerPositionAndAngles();
            float posX = viewerPosAndAngles[0];
            float posY = viewerPosAndAngles[1];
            float posZ = viewerPosAndAngles[2];
            float yaw = viewerPosAndAngles[3];
            float pitch = viewerPosAndAngles[4];

            // 计算玩家视线方向
            double dx = Math.cos(Math.toRadians(yaw));
            double dz = Math.sin(Math.toRadians(yaw));
            double dy = -Math.sin(Math.toRadians(pitch));

            // 计算玩家与区块的距离
            double chunkCenterX = (chunkX << 4) + 8;
            double chunkCenterZ = (chunkZ << 4) + 8;
            double distance = Math.sqrt(Math.pow(posX - chunkCenterX, 2) + Math.pow(posZ - chunkCenterZ, 2));

            // 判断区块是否在玩家视野内
            if (distance < 64 && Math.abs(posY - (chunkCenterX + 8)) < 64) {
                return true;
            }
        }
        return false;
    }

    private void modifyChunkTerrain(Chunk chunk) {
        // 获取区块的随机数生成器
        Random random = new Random();
        // 获取区块的方块存储数组
        IBlockState[] blockStorage = (IBlockState[]) chunk.getBlockStorageArray();
        // 遍历区块中的每个方块
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < chunk.getHeight(chunk.getPos().getBlock(x,y,z)); y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState state = blockStorage[y >> 4];
                    // 根据随机逻辑修改方块状态
                    if (random.nextFloat() < 0.01) { // 1% 的概率进行变化
                        IBlockState newState = getModifiedBlockState(state, random);
                        if (newState != state) {
                            chunk.setBlockState(pos, newState);
                        }
                    }
                }
            }
        }
    }

    private IBlockState getModifiedBlockState(IBlockState state, Random random) {
        // 实现具体的区块变化逻辑
        // 这里可以根据需要添加更复杂的逻辑，例如根据周围方块类型决定新的方块状态
        // 示例：随机替换空气为草方块
        if (state.getBlock() == Blocks.AIR) {
            return Blocks.GRASS.getDefaultState();
        }
        return state; // 如果不是空气，不进行变化
    }
}
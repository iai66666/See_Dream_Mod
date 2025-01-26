package top.iai.see_dream.Events;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

import static net.minecraftforge.common.DimensionManager.getWorld;

public class Dream {
    int randomX;
    int randomY;
    int randomZ;
    World world;
    WorldServer worldServer;
    private final Random random = new Random();
    private long lastExecutionTime = 0;
    public Dream() {
    }
    //第一个玩家（如果想找虐想让所有玩家都同时做梦就写成玩家列表）
    EntityPlayer player;
    //各种表
    private static final List<Block> logsBlocks = Arrays.asList(Blocks.LOG, Blocks.LOG2);
    private static final List<Block> leavesBlocks = Arrays.asList(Blocks.LEAVES, Blocks.LEAVES2);
    private static final List<Block> dirtBlocks = Arrays.asList(Blocks.DIRT, Blocks.GRASS, Blocks.GRASS_PATH, Blocks.FARMLAND);
    private static final List<Block> stonesBlocks = Arrays.asList(Blocks.STONE, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE);
    private static final List<Block> plantsBlocks = Arrays.asList(Blocks.TALLGRASS, Blocks.DOUBLE_PLANT);
    private static final List<Block> airBlocks = Arrays.asList(Blocks.ICE, Blocks.GLASS, Blocks.TNT);
    //表的表
    private static final List<List<Block>> blocksListsList = Arrays.asList(logsBlocks, leavesBlocks, dirtBlocks, stonesBlocks, plantsBlocks);
    //字典
    private static final Map<Block, List<Block>> blockTransformMap = new HashMap<>();

    static {
        //神秘增强for循环（写字典）
        for (List<Block> blocksList : blocksListsList) {
            for (Block block : blocksList) {
                blockTransformMap.put(block, blocksList);
            }
        }
    }
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTick(TickEvent event) {
        world = Minecraft.getMinecraft().world != null ? Minecraft.getMinecraft().world : null;
        if(world == null||Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null ) return;
        //每？秒执行一次
        long currentTime = world.getTotalWorldTime();
        if (currentTime % 5 == 0 && currentTime != lastExecutionTime) {
            lastExecutionTime = currentTime;
            Dreaming();
        }
    }
    @SubscribeEvent
    public void onSeverTick(TickEvent event) {
        worldServer = getWorld(0) != null ? getWorld(0) : null;
        if(worldServer == null) return;
        //每？秒执行一次
        long currentTime = worldServer.getWorldTime();
        if (currentTime % 5 == 0 && currentTime != lastExecutionTime) {
            lastExecutionTime = currentTime;
            DreamingServer();
        }
    }
    @SideOnly(Side.CLIENT)
    //做梦
    private void Dreaming() {
        //获取第一个玩家
        if (world == null) return;
        if (world.playerEntities.isEmpty()) {
            return;
        }
            player = world.playerEntities.get(0);


        //计算玩家视线方向
        double var3 = Math.cos(-(player.rotationYaw % 360) * 0.017453292 - (float)Math.PI);
        double var5 = Math.sin(-(player.rotationYaw % 360) * 0.017453292 - (float)Math.PI);
        double var7 = -Math.cos(-player.rotationPitch * 0.017453292);
        double var9 = Math.sin(-player.rotationPitch * 0.017453292);
        //玩家视线的方向
        Vec3d VEC = new Vec3d(var5 * var7, var9, var3 * var7);
        //定义变化范围，范围半径为128长、128宽、8高
        int rangeX = 128;
        int rangeY = 8;
        int rangeZ = 128;

        //在范围内取随机数
        randomX = (int) player.posX - rangeX + random.nextInt(2 * rangeX + 1);
        randomY = (int) (player.posY + player.getEyeHeight()) - rangeY + random.nextInt(2 * rangeY + 1);
        randomZ = (int) player.posZ - rangeZ + random.nextInt(2 * rangeZ + 1);
        BlockPos blockPos = new BlockPos(randomX, randomY, randomZ);

        //距离判断 + 视锥判断（先判断距离避免不必要的计算
        if (Math.sqrt(Math.pow(randomX - player.posX, 2) + Math.pow(randomY - player.posY + player.getEyeHeight(), 2) + Math.pow(randomZ - player.posZ, 2)) > 15
                && !isPointInCone(randomX, randomY, randomZ, player.posX, player.posY + player.getEyeHeight(), player.posZ, VEC.x, VEC.y, VEC.z, 65)) {

            //获取要替换的方块
            Block currentBlock = world.getBlockState(blockPos).getBlock();
            //声明新方块
            Block newBlock;
            //尝试获取该方块的变化表
            List<Block> possibleTransformations = blockTransformMap.get(currentBlock);
            //判断是不是特殊方块
            if (possibleTransformations != null && !possibleTransformations.isEmpty()) {
                // 从对应的表中随机选择一个方块
                newBlock = possibleTransformations.get(random.nextInt(possibleTransformations.size()));
            }
            //啥也不是，从空气表中选
            else {
                newBlock = airBlocks.get(random.nextInt(airBlocks.size()));
            }
            //替换方块
            world.setBlockState(blockPos, newBlock.getDefaultState(), 1|2|3);
        }

        //玩家眼睛的位置
        Vec3d rayStartPoint = player.getPositionVector().add(0, player.getEyeHeight(), 0);
        //射线最大距离
        double maxDistance = 100.0;
        // 执行射线检测
        RayTraceResult result = world.rayTraceBlocks(rayStartPoint, rayStartPoint.add(-VEC.x * maxDistance, -VEC.y * maxDistance, -VEC.z * maxDistance));

        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            // 获取碰撞到的方块位置
            BlockPos hitPos = result.getBlockPos();
            //如果碰到的方块距离较小，不执行
            if(Math.sqrt(Math.pow(hitPos.getX() - player.posX, 2) + Math.pow(hitPos.getY() - player.posY + player.getEyeHeight(), 2) + Math.pow(hitPos.getZ() - player.posZ, 2)) < 4) return;
            // 获取当前方块
            Block currentBlock = world.getBlockState(hitPos).getBlock();
            Block newBlock;

            // 查找该方块的变化表
            List<Block> possibleTransformations = blockTransformMap.get(currentBlock);

            // 如果有对应变化的方块，随机选择一个
            if (possibleTransformations != null && !possibleTransformations.isEmpty()) {
                //草方块增生
                if (currentBlock == Blocks.GRASS || possibleTransformations == plantsBlocks){
                    newBlock = Blocks.GRASS;
                    if (newBlock != null) {
                        world.addBlockEvent(blockPos, newBlock, 0, 0);
                    }
                    world.setBlockState(hitPos.add(random.nextInt(3)-1, random.nextInt(2),random.nextInt(3)-1), newBlock.getDefaultState(), 1|2|3);
                    return;
                }
                newBlock = possibleTransformations.get(random.nextInt(possibleTransformations.size()));
            }
            // 否则从空气方块表中随机选择
            else {
                newBlock = airBlocks.get(random.nextInt(airBlocks.size()));
            }

            // 替换方块

            world.setBlockState(hitPos, newBlock.getDefaultState(), 1|2|3);
        }
    }

    //判断一个点是否在圆锥内
    //(Px, Py, Pz) 是要判断的点
    //(Vx, Vy, Vz) 是圆锥顶点
    //(dX, dY, Dz) 是圆锥轴线的方向单位向量
    //angle 是圆锥的半角，单位度
    public static boolean isPointInCone(double Px, double Py, double Pz, double Vx, double Vy, double Vz, double dX, double dY, double Dz, double angle)
    {
        //计算点 P 和圆锥顶点 V 之间的向量
        double vectorVPx = Px - Vx;
        double vectorVPy = Py - Vy;
        double vectorVPz = Pz - Vz;
        //计算向量 VP 和圆锥轴线方向 d 的点积
        double dotProduct = vectorVPx * dX + vectorVPy * dY + vectorVPz * Dz;
        //计算向量 VP 的模长
        double lengthVP = Math.sqrt(vectorVPx * vectorVPx + vectorVPy * vectorVPy + vectorVPz * vectorVPz);
        //计算点积和模长的商得到向量 VP 和轴线方向 d 之间的余弦值
        double cosThetaPrime = dotProduct / lengthVP;
        //计算圆锥的半角的余弦值
        double cosTheta = Math.cos(Math.toRadians(angle));
        //判断点是否在圆锥内
        return cosThetaPrime >= cosTheta;
    }
    private void DreamingServer(){//获取第一个玩家
        if (worldServer.playerEntities.isEmpty()) {
            return;
        }
            player = worldServer.playerEntities.get(0);


        //计算玩家视线方向
        double var3 = Math.cos(-(player.rotationYaw % 360) * 0.017453292 - (float)Math.PI);
        double var5 = Math.sin(-(player.rotationYaw % 360) * 0.017453292 - (float)Math.PI);
        double var7 = -Math.cos(-player.rotationPitch * 0.017453292);
        double var9 = Math.sin(-player.rotationPitch * 0.017453292);
        //玩家视线的方向
        Vec3d VEC = new Vec3d(var5 * var7, var9, var3 * var7);
        //定义变化范围，范围半径为128长、128宽、8高
        int rangeX = 128;
        int rangeY = 8;
        int rangeZ = 128;

        //在范围内取随机数
        randomX = (int) player.posX - rangeX + random.nextInt(2 * rangeX + 1);
        randomY = (int) (player.posY + player.getEyeHeight()) - rangeY + random.nextInt(2 * rangeY + 1);
        randomZ = (int) player.posZ - rangeZ + random.nextInt(2 * rangeZ + 1);
        BlockPos blockPos = new BlockPos(randomX, randomY, randomZ);

        //距离判断 + 视锥判断（先判断距离避免不必要的计算
        if (Math.sqrt(Math.pow(randomX - player.posX, 2) + Math.pow(randomY - player.posY + player.getEyeHeight(), 2) + Math.pow(randomZ - player.posZ, 2)) > 15
                && !isPointInCone(randomX, randomY, randomZ, player.posX, player.posY + player.getEyeHeight(), player.posZ, VEC.x, VEC.y, VEC.z, 65)) {

            //获取要替换的方块
            Block currentBlock = worldServer.getBlockState(blockPos).getBlock();
            //声明新方块
            Block newBlock;
            //尝试获取该方块的变化表
            List<Block> possibleTransformations = blockTransformMap.get(currentBlock);
            //判断是不是特殊方块
            if (possibleTransformations != null && !possibleTransformations.isEmpty()) {
                // 从对应的表中随机选择一个方块
                newBlock = possibleTransformations.get(random.nextInt(possibleTransformations.size()));
            }
            //啥也不是，从空气表中选
            else {
                newBlock = airBlocks.get(random.nextInt(airBlocks.size()));
            }
            //替换方块
            worldServer.setBlockState(blockPos, newBlock.getDefaultState(), 1|2|3);
        }

        //玩家眼睛的位置
        Vec3d rayStartPoint = player.getPositionVector().add(0, player.getEyeHeight(), 0);
        //射线最大距离
        double maxDistance = 100.0;
        // 执行射线检测
        RayTraceResult result = worldServer.rayTraceBlocks(rayStartPoint, rayStartPoint.add(-VEC.x * maxDistance, -VEC.y * maxDistance, -VEC.z * maxDistance));

        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            // 获取碰撞到的方块位置
            BlockPos hitPos = result.getBlockPos();
            //如果碰到的方块距离较小，不执行
            if(Math.sqrt(Math.pow(hitPos.getX() - player.posX, 2) + Math.pow(hitPos.getY() - player.posY + player.getEyeHeight(), 2) + Math.pow(hitPos.getZ() - player.posZ, 2)) < 4) return;
            // 获取当前方块
            Block currentBlock = worldServer.getBlockState(hitPos).getBlock();
            Block newBlock;

            // 查找该方块的变化表
            List<Block> possibleTransformations = blockTransformMap.get(currentBlock);

            // 如果有对应变化的方块，随机选择一个
            if (possibleTransformations != null && !possibleTransformations.isEmpty()) {
                //草方块增生
                if (currentBlock == Blocks.GRASS || possibleTransformations == plantsBlocks){
                    newBlock = Blocks.GRASS;
                    if (newBlock != null) {
                        worldServer.addBlockEvent(blockPos, newBlock, 0, 0);
                    }
                    worldServer.setBlockState(hitPos.add(random.nextInt(3)-1, random.nextInt(2),random.nextInt(3)-1), newBlock.getDefaultState(), 1|2|3);
                    return;
                }
                newBlock = possibleTransformations.get(random.nextInt(possibleTransformations.size()));
            }
            // 否则从空气方块表中随机选择
            else {
                newBlock = airBlocks.get(random.nextInt(airBlocks.size()));
            }

            // 替换方块

            worldServer.setBlockState(hitPos, newBlock.getDefaultState(), 1|2|3);
        }
    }
}

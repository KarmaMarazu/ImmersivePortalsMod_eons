package qouteall.imm_ptl.core.mixin.common.entity_sync;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import qouteall.imm_ptl.core.ducks.IEServerPlayerEntity;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements IEServerPlayerEntity {
    @Shadow
    public ServerGamePacketListenerImpl connection;
    @Shadow
    private Vec3 enteredNetherPosition;
    
//    private HashMultimap<RegistryKey<World>, Entity> myRemovedEntities;
    
    @Shadow
    private boolean isChangingDimension;
    
    @Shadow protected abstract void triggerDimensionChangeTriggers(ServerLevel origin);
    
    public MixinServerPlayer(Level world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }
    
//    @Inject(
//        method = "Lnet/minecraft/server/network/ServerPlayerEntity;tick()V",
//        at = @At("TAIL")
//    )
//    private void onTicking(CallbackInfo ci) {
//        if (myRemovedEntities != null) {
//            myRemovedEntities.keySet().forEach(dimension -> {
//                Set<Entity> list = myRemovedEntities.get(dimension);
//                networkHandler.sendPacket(
//                    MyNetwork.createRedirectedMessage(
//                        dimension,
//                        new EntityDestroyS2CPacket(
//                            list.stream().mapToInt(
//                                Entity::getId
//                            ).toArray()
//                        )
//                    )
//                );
//            });
//            myRemovedEntities = null;
//        }
//    }
    
//    @Inject(
//        method = "Lnet/minecraft/server/network/ServerPlayerEntity;copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V",
//        at = @At("RETURN")
//    )
//    private void onCopyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
//        HashMultimap<RegistryKey<World>, Entity> oldPlayerRemovedEntities =
//            ((MixinServerPlayerEntity) (Object) oldPlayer).myRemovedEntities;
//        if (oldPlayerRemovedEntities != null) {
//            myRemovedEntities = HashMultimap.create();
//            this.myRemovedEntities.putAll(oldPlayerRemovedEntities);
//        }
//    }
    
//    /**
//     * @author qouteall
//     */
//    @Overwrite
//    public void onStoppedTracking(Entity entity_1) {
//        if (entity_1 instanceof PlayerEntity) {
//            this.networkHandler.sendPacket(
//                MyNetwork.createRedirectedMessage(
//                    entity_1.world.getRegistryKey(),
//                    new EntityDestroyS2CPacket(entity_1.getId())
//                )
//            );
//        }
//        else {
//            if (myRemovedEntities == null) {
//                myRemovedEntities = HashMultimap.create();
//            }
//            //do not use entity.dimension
//            //or it will work abnormally when changeDimension() is run
//            myRemovedEntities.put(entity_1.world.getRegistryKey(), entity_1);
//        }
//
//    }
    
//    /**
//     * @author qouteall
//     */
//    @Overwrite
//    public void onStartedTracking(Entity entity_1) {
//        if (myRemovedEntities != null) {
//            myRemovedEntities.remove(entity_1.world.getRegistryKey(), entity_1);
//        }
//    }
    
    @Override
    public void setEnteredNetherPos(Vec3 pos) {
        enteredNetherPosition = pos;
    }
    
    @Override
    public void updateDimensionTravelAdvancements(ServerLevel fromWorld) {
        triggerDimensionChangeTriggers(fromWorld);
    }
    
    @Override
    public void setIsInTeleportationState(boolean arg) {
        isChangingDimension = arg;
    }
    
    @Override
    public void stopRidingWithoutTeleportRequest() {
        super.stopRiding();
    }
    
    @Override
    public void startRidingWithoutTeleportRequest(Entity newVehicle) {
        super.startRiding(newVehicle, true);
    }
    
    @Override
    public void portal_worldChanged(ServerLevel fromWorld) {
        triggerDimensionChangeTriggers(fromWorld);
    }
}
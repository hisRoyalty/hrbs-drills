package com.hisroyalty.hrbsdrills.entity;

import com.google.common.collect.Lists;
import com.hisroyalty.hrbsdrills.Config;
import com.hisroyalty.hrbsdrills.DrillsMod;
import com.hisroyalty.hrbsdrills.container.DrillContainer;
import com.hisroyalty.hrbsdrills.sound.SoundRegistry;
import com.hisroyalty.hrbsdrills.util.UtilLightBlock;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.level.PistonEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Properties;

import static com.hisroyalty.hrbsdrills.DrillsMod.DRILL;

public class DrillEntity extends Entity implements GeoEntity, MenuProvider {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
        public boolean isMoving = this.onGround() && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-9D;
        private boolean breakableBlocks;

        public double speedMultiplier = 0;








        public static final EntityDataAccessor<Integer> MAX_HEALTH = SynchedEntityData.defineId(DrillEntity.class, EntityDataSerializers.INT);
        public static final EntityDataAccessor<Integer> HEALTH = SynchedEntityData.defineId(DrillEntity.class, EntityDataSerializers.INT);

        public static final EntityDataAccessor<Integer> PROGRESS = SynchedEntityData.defineId(DrillEntity.class, EntityDataSerializers.INT);
        public static final EntityDataAccessor<Integer> MAX_PROGRESS = SynchedEntityData.defineId(DrillEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> WPROGRESS = SynchedEntityData.defineId(DrillEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> MAX_WPROGRESS = SynchedEntityData.defineId(DrillEntity.class, EntityDataSerializers.INT);


    public static final EntityDataAccessor<Boolean> NETHERITE = SynchedEntityData.defineId(DrillEntity.class, EntityDataSerializers.BOOLEAN);



    private float previousRotationYaw;






    private final ItemStackHandler itemHandler = new ItemStackHandler(3);
    public final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);






    public int accelerationTime;
        public int timeInVehicle;

        public float yrot;


        private boolean inputLeft;
        private boolean inputRight;
        private boolean inputUp;
        private boolean inputDown;


//protected final ContainerData data;


    public DrillEntity(EntityType<? extends DrillEntity> entityType, Level world) {


            super(entityType, world);
            /*this.data = new ContainerData() {
                @Override
                public int get(int pIndex) {
                    return switch (pIndex) {
                        case 0 -> DrillEntity.this.getProgress();
                        case 1 -> DrillEntity.this.getMaxProgress();
                        case 2 -> DrillEntity.this.getWProgress();
                        case 3 -> DrillEntity.this.getMaxWProgress();
                        default -> 0;
                    };
                }

                @Override
                public void set(int pIndex, int pValue) {
                    switch (pIndex) {
                        case 0 -> DrillEntity.this.setProgress(pValue);
                        case 1 -> DrillEntity.this.setMaxProgress(pValue);
                        case 2 -> DrillEntity.this.setWProgress(pValue);
                        case 3 -> DrillEntity.this.setMaxWProgress(pValue);

                    }
                }

                @Override
                public int getCount() {
                    return 2;
                }
            }*/;

        }




        @Override
        public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand) {
            ItemStack itemstack = player.getItemInHand(hand);
            if (itemstack.getItem() == Items.NETHERITE_BLOCK && !this.getNetherite()) {
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.entityData.set(NETHERITE, true);
            }

            else if (canAddPassenger(player)) {
                player.startRiding(this, true);
                return InteractionResult.PASS;
            }
            return InteractionResult.FAIL;
        }


    public void center() {
        Direction facing = getDirection();
        switch (facing) {
            case SOUTH:
                setYRot(0F);
                break;
            case NORTH:
                setYRot(180F);
                break;
            case EAST:
                setYRot(-90F);
                break;
            case WEST:
                setYRot(90F);
                break;
        }
    }

        @Override
        protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
            super.checkFallDamage(pY, pOnGround, pState, pPos);
        }


        @Override
        public Vec3 getDismountLocationForPassenger(LivingEntity pLivingEntity) {
            Vec3 vec3 = getCollisionHorizontalEscapeVector((double) (this.getBbWidth() * Mth.SQRT_OF_TWO), (double) pLivingEntity.getBbWidth(), pLivingEntity.getYRot());
            double d0 = this.getX() + vec3.x;
            double d1 = this.getZ() + vec3.z;
            BlockPos blockpos = BlockPos.containing(d0, this.getBoundingBox().maxY, d1);
            BlockPos blockpos1 = blockpos.below();
            if (!this.level().isWaterAt(blockpos1)) {
                List<Vec3> list = Lists.newArrayList();
                double d2 = this.level().getBlockFloorHeight(blockpos);
                if (DismountHelper.isBlockFloorValid(d2)) {
                    list.add(new Vec3(d0, (double) blockpos.getY() + d2, d1));
                }

                double d3 = this.level().getBlockFloorHeight(blockpos1);
                if (DismountHelper.isBlockFloorValid(d3)) {
                    list.add(new Vec3(d0, (double) blockpos1.getY() + d3, d1));
                }

                for (Pose pose : pLivingEntity.getDismountPoses()) {
                    for (Vec3 vec31 : list) {
                        if (DismountHelper.canDismountTo(this.level(), vec31, pLivingEntity, pose)) {
                            pLivingEntity.setPose(pose);
                            return vec31;
                        }
                    }
                }
            }

            return super.getDismountLocationForPassenger(pLivingEntity);

        }

        @Override
        protected void addPassenger(Entity pPassenger) {
            super.addPassenger(pPassenger);
            if (this.isControlledByLocalInstance() && this.lerpSteps > 0) {
                this.lerpSteps = 0;
                this.absMoveTo(this.lerpX, this.lerpY, this.lerpZ, (float) this.lerpYRot, (float) this.lerpXRot);
            }
        }


        @Override
        public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
            super.onSyncedDataUpdated(pKey);

        }


        @Override
        public int getMaxFallDistance() {
            return 30;
        }









        @Override
        public void push(Entity pEntity) {
            super.push(pEntity);
            LivingEntity entity = (LivingEntity) pEntity;
            Entity rider = this.getControllingPassenger();


            if (this.hasControllingPassenger() && pEntity != rider) {
                Player playerRider = (Player) rider;
                assert playerRider != null;
                if (this.getControllingPassenger() instanceof Player) {
                    entity.hurt(this.damageSources().playerAttack((Player) this.getControllingPassenger()), (float) this.accelerationTime / 10);
                }
            }
        }


        public int getProgress() {
            return entityData.get(PROGRESS);
        }

        public void setProgress(int progress) {
            entityData.set(PROGRESS, progress);
        }

        public int getWProgress() {
            return entityData.get(WPROGRESS);
        }

        public void setWProgress(int wprogress) {
            entityData.set(WPROGRESS, wprogress);
        }




        public int getMaxProgress() {
            return entityData.get(MAX_PROGRESS);
        }

        public void setMaxProgress(int maxProgress) {
            entityData.set(MAX_PROGRESS, maxProgress);
        }

        public int getMaxWProgress() {
            return entityData.get(MAX_WPROGRESS);
        }


        public void setMaxWProgress(int maxWProgress) {
            entityData.set(MAX_WPROGRESS, maxWProgress);
        }


        public boolean isBreakableBlock(BlockState blockstate) {


            return blockstate.is(BlockTags.MINEABLE_WITH_PICKAXE) || blockstate.is(BlockTags.MINEABLE_WITH_SHOVEL);
        }


        private boolean destroyBlocks(AABB pArea) {

            final Vec3 facing = Vec3.directionFromRotation(this.getRotationVector());
            final double floatingHeight = 0.5;
            final Vec3 floatingOffset = new Vec3(0, floatingHeight, 0);
            final AABB box = this.getBoundingBox().move(facing.normalize().add(floatingOffset));

            boolean flag = false;

            BlockPos.betweenClosedStream(box).forEach(blockPos -> {

                BlockState blockstate = this.level().getBlockState(blockPos);

                if (isBreakableBlock(blockstate) && blockstate.canOcclude()) {
                    this.level().destroyBlock(blockPos, Config.DROP_BLOCK.get(), this);
                }
            });
            return flag;
    }





        public void drops() {
            SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
            for(int i = 0; i < itemHandler.getSlots(); i++) {
                inventory.setItem(i, itemHandler.getStackInSlot(i));
            }
            Containers.dropContents(level(), this, inventory);
        }



        @Override
        public boolean shouldRiderSit() {
            return false;
        }






        @Override
        public boolean startRiding(Entity p_21396_, boolean p_21397_) {
            return super.startRiding(p_21396_, p_21397_);
        }


        @Override
        protected void positionRider(Entity pPassenger, MoveFunction pCallback) {
            //pPassenger.setYRot(pPassenger.getYHeadRot());
            super.positionRider(pPassenger, pCallback);
        }

        @Override
        public Iterable<ItemStack> getArmorSlots() {
            return null;
        }

        @Override
        public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

        }

        public void setInput(boolean pInputLeft, boolean pInputRight, boolean pInputUp, boolean pInputDown) {
            this.inputLeft = pInputLeft;
            this.inputRight = pInputRight;
            this.inputUp = pInputUp;
            this.inputDown = pInputDown;
        }



        private void control() {
            if (this.isVehicle() && this.hasControllingPassenger() && getProgress() >0) {
                double gravity = 0.875D;
                double upwardForce = 0.5f;

                if (!this.onGround()) {
                    Vec3 motion = this.getDeltaMovement();
                    this.setDeltaMovement(motion.x, motion.y - gravity, motion.z);
                }

                LivingEntity livingEntity = this.getControllingPassenger();

                if (this.getControllingPassenger() == livingEntity) {
                    double moveX = 0.0;
                    double moveY = 0.0;
                    double moveZ = 0.0;

                    if (this.hasControllingPassenger()) {
                        if (this.inputLeft) {
                            this.setYRot(this.getYRot() - 1.0F);
                        }

                        if (this.inputRight) {
                            this.setYRot(this.getYRot() + 1.0F);
                        }

                        double yawRad = Math.toRadians(this.getYRot());
                        double x = -Math.sin(yawRad);
                        double z = Math.cos(yawRad);

                        if (this.inputUp) {
                            moveX += getWProgress()>0 ? x * 0.1 : x * 0.025;
                            moveZ += getWProgress()>0 ? z * 0.1 : z * 0.025;
                        }
                        if (this.inputDown) {
                            moveX -= getWProgress()>0 ? x * 0.1 : x * 0.025;
                            moveZ -= getWProgress()>0 ? z * 0.1 : z * 0.025;
                        }

                        

                        Vec3 deltaMovement = new Vec3(moveX, moveY, moveZ);
                        Vec3 currentDeltaMovement = this.getDeltaMovement();
                        Vec3 updatedDeltaMovement = currentDeltaMovement.add(deltaMovement);

                        if (!this.horizontalCollision) {
                            this.setDeltaMovement(updatedDeltaMovement);
                        } else {
                            if (this.onGround()) {
                                this.setDeltaMovement(updatedDeltaMovement.add(0.0, upwardForce, 0.0));
                            }
                        }
                    }
                } else {
                    this.setYRot(this.getYRot());
                }
            }
        }







        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if(cap == ForgeCapabilities.ITEM_HANDLER) {
                return lazyItemHandler.cast();
            }
            return super.getCapability(cap, side);
        }




        protected float deltaRotation;

        private Vec3 previousPosition = Vec3.ZERO;


        private int lerpSteps;
        private double lerpX;
        private double lerpY;
        private double lerpZ;
        private double lerpYRot;
        private double lerpXRot;




















        @Override
        public boolean canBeCollidedWith() {
            return true;
        }

        @Override
        public boolean displayFireAnimation() {
            return false;
        }

        @Override

        public boolean isPushable() {
            return true;
        }

        @Override
        public boolean isPickable() {
            return isAlive();
        }



        public void tickLerp() {
            if (this.isControlledByLocalInstance()) {
                this.lerpSteps = 0;
                syncPacketPositionCodec(getX(), getY(), getZ());
            } else if (lerpSteps > 0) {
                this.setPos(
                        this.getX() + ((this.lerpX - this.getX()) / (double)this.lerpSteps),
                        this.getY() + ((this.lerpY - this.getY()) / (double)this.lerpSteps),
                        this.getZ() + ((this.lerpZ - this.getZ()) / (double)this.lerpSteps)
                );
                this.setYRot((float) (this.getYRot() + (Mth.wrapDegrees(this.lerpYRot - this.getYRot()) / (float)this.lerpSteps)));

                this.lerpSteps--;
            }
        }

        @Override
        public InteractionResult interact(Player player, InteractionHand hand) {
            if (!player.isShiftKeyDown()) {
                if (player.getVehicle() != this) {
                    if (!level().isClientSide) {
                    }
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }


        @Override
        public Packet<ClientGamePacketListener> getAddEntityPacket() {
            return new ClientboundAddEntityPacket(this);

        }


        @Override
        public void invalidateCaps() {
            super.invalidateCaps();
            lazyItemHandler.invalidate();
}

        @Override
        public void tick() {


            if (this.hasControllingPassenger() && this.getNetherite()) {
                Player player = (Player) this.getControllingPassenger();
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 1));
            }

            // TODO: IT SOMETIMES BREAKS BLOCKS ON TOP BECAUSE IT  TRIES TO CLIMB THEM

//            if (tickCount % 4 == 0) {
//                // attempt to place light block
//                BlockPos posIn = getOnPos().above();
//                BlockState blockIn = level().getBlockState(posIn);
//                // check if current block can be replaced
//
//                if ((blockIn.isAir() || blockIn.is(Blocks.WATER) && blockIn.getFluidState().isSource())
//                        && !DrillsMod.LIGHT.get().defaultBlockState().is(blockIn.getBlock())) {
//                    // determine waterlog value
//                    boolean waterlogged = blockIn.getFluidState().isSource() && blockIn.getFluidState().is(FluidTags.WATER);
//                    // create light block
//                    BlockState lightBlock = DrillsMod.LIGHT.get()
//                            .defaultBlockState()
//                            .setValue(UtilLightBlock.LEVEL, 15)
//                            .setValue(UtilLightBlock.WATERLOGGED, waterlogged);
//                    // place light block
//                    level().setBlock(posIn, lightBlock, Block.UPDATE_ALL);
//                }
//            }
            // SLOT 1 -> FUEL SLOT
            // SLOT 0 -> WATER SLOT
            if (!level().isClientSide) {
            if (getWProgress()>0) {
                setWProgress(getWProgress() - 1);
            } else if (this.isAlive()) {
                ItemStack waterStack = itemHandler.getStackInSlot(0);
                if (waterStack.is(Items.WATER_BUCKET)) {
                    itemHandler.setStackInSlot(0, new ItemStack(Items.BUCKET));
                    setWProgress(8000);
                    setMaxWProgress(8000);
                }
            }
                if (getProgress() > 0) {
                    setProgress(getProgress() - Config.fuelEfficiency);
                } else if (this.isAlive()) {
                    ItemStack itemStack = itemHandler.getStackInSlot(1);
                    int itemBurnTime = ForgeHooks.getBurnTime(itemStack, null);
                    if (itemBurnTime > 0) {
                        setProgress(itemBurnTime);
                        setMaxProgress(itemBurnTime);
                        if (itemStack.hasCraftingRemainingItem()) {
                            itemHandler.setStackInSlot(1, itemStack.getCraftingRemainingItem());
                        } else {
                            itemHandler.extractItem(1, 1, false);
                        }
                    }
                }
                this.xo = getX();
                this.yo = getY();
                this.zo = getZ();
            }
            this.setAnimData(DataTickets.ACTIVE, true);
            super.tick();
            tickLerp();

            // unconditional, not sure why this works
            this.setDeltaMovement(Vec3.ZERO);
            this.yrot = this.getYRot();


            // TODO: Revisit this temporary way of checking if it is moving
            if (!this.getDeltaMovement().equals(Vec3.ZERO) || !this.position().equals(this.previousPosition)) {
                this.previousPosition = this.position();
            }

            if (this.getYRot() != this.previousRotationYaw) {
                this.previousRotationYaw = this.getYRot();
            }


            if (this.isControlledByLocalInstance()) {
                if (this.level().isClientSide) {
                    control();
                }

                this.move(MoverType.SELF, this.getDeltaMovement());
            } else {
                this.setDeltaMovement(Vec3.ZERO);
            }


            if (this.isVehicle()) {
                timeInVehicle++;
            }
            destroyBlocks(this.getBoundingBox());

        }





       @Override
        public void lerpTo(double pX, double pY, double pZ, float pYRot, float pXRot, int pLerpSteps, boolean pTeleport) {
            this.lerpX = pX;
            this.lerpY = pY;
            this.lerpZ = pZ;
            this.lerpYRot = (double)pYRot;
            this.lerpXRot = (double)pXRot;
            this.lerpSteps = this.getType().updateInterval()+1;
        }



        @Override
        protected void defineSynchedData() {
            entityData.define(HEALTH, 100);
            entityData.define(MAX_HEALTH, 100);
            entityData.define(PROGRESS, 0);
            entityData.define(MAX_PROGRESS, 0);
            entityData.define(WPROGRESS, 0);
            entityData.define(MAX_WPROGRESS, 0);
            entityData.define(NETHERITE, false);





        }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return DRILL.get().getDefaultInstance();
    }

    public void setHealth(int health) {
            entityData.set(HEALTH, Math.max(health, 0));
        }

        public int getHealth() {
            return entityData.get(HEALTH);
        }

        public void setNetherite(boolean netherite) {
            entityData.set(NETHERITE, netherite);
        }

        public boolean getNetherite() {
            return entityData.get(NETHERITE);
        }

        public int getMaxHealth() {
            return entityData.get(MAX_HEALTH);
        }



        @Override
        public void readAdditionalSaveData(CompoundTag pCompound) {
            itemHandler.deserializeNBT(pCompound.getCompound("inventory"));



            if (pCompound.contains("max_health")) {
                int maxHealth = pCompound.getInt("max_health");
                if (maxHealth <= 0) {
                    maxHealth = 20;
                }
                entityData.set(MAX_HEALTH, maxHealth);
            }

            if (pCompound.contains("health")) {
                int health = pCompound.getInt("health");
                entityData.set(HEALTH, health);
            }

            if (pCompound.contains("progress")) {
                setProgress(pCompound.getInt("progress"));
            }
            if (pCompound.contains("maxProgress")) {
                setMaxProgress(pCompound.getInt("maxProgress"));
            }
            if (pCompound.contains("wprogress")) {
                setWProgress(pCompound.getInt("wprogress"));
            }
            if (pCompound.contains("maxWProgress")) {
                setMaxWProgress(pCompound.getInt("maxWProgress"));
            }
            if (pCompound.contains("netherite")) {
                entityData.set(NETHERITE, pCompound.getBoolean("netherite"));
            }


        }



        @Override
        public void addAdditionalSaveData(CompoundTag pCompound) {
            pCompound.put("inventory", itemHandler.serializeNBT());


            pCompound.putInt("health", entityData.get(HEALTH));
            pCompound.putInt("max_health", entityData.get(MAX_HEALTH));

            pCompound.putInt("progress", getProgress());
            pCompound.putInt("maxProgress", getMaxProgress());
            pCompound.putInt("wprogress", getWProgress());
            pCompound.putInt("maxWProgress", getMaxWProgress());



            pCompound.putBoolean("netherite", entityData.get(NETHERITE));





        }


        public ItemStack getItemStack() {
            ItemStack itemStack = getItem().getDefaultInstance();
            CompoundTag compound = new CompoundTag();
            addAdditionalSaveData(compound);
            compound.putInt("health", entityData.get(MAX_HEALTH));
            compound.putBoolean("netherite", entityData.get(NETHERITE));
            itemStack.addTagElement("Compound", compound);
            return itemStack;
        }
        protected void dropItem() {
            ItemStack itemStack = getItemStack();
            spawnAtLocation(itemStack);
        }


        protected Item getItem() {
            return DRILL.get();
        }






        @Override
        public float getStepHeight() {
            return 1.0f;
        }




        @Override
        public boolean fireImmune() {
            return false;
        }




        @Override
        public boolean hurt(DamageSource pSource, float pAmount) {

            if(getHealth()<=0) {
                kill();
                dropItem();
                drops();
            }

            pAmount = 3;

            if (pSource.type() == level().damageSources().cactus().type() || pSource.type() == level().damageSources().sweetBerryBush().type()) {
                return false;
            }


            if (getNetherite() && (pSource.type() == level().damageSources().lava().type() || pSource.type() == level().damageSources().inFire().type() || pSource.type() == level().damageSources().onFire().type())) {
                return false;
            }



            if (pSource == level().damageSources().drown()) {
                return false;
            }

            if (pSource.type() == level().damageSources().cactus().type() || pSource.type() == level().damageSources().sweetBerryBush().type()) {
                return false;
            }


            this.setHealth((int) (this.entityData.get(HEALTH)-pAmount));

            return super.hurt(pSource, pAmount);
        }

        @Override
        protected void playStepSound(BlockPos pPos, BlockState pBlock) {
            this.playSound(SoundRegistry.DRILL.get(), 0.25f, 1F);
        }

        @Override
        protected void removePassenger(Entity pPassenger) {
            super.removePassenger(pPassenger);
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
            controllers.add(new AnimationController<>(this, "controller", 1, this::predicate));

        }


        protected <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event) {
            if (this.xo != this.getX() || this.zo != this.getZ()) {
                if (this.getNetherite()) {
                    event.getController().setAnimation(RawAnimation.begin().then("drill_v5", Animation.LoopType.LOOP));
                } else {
                    event.getController().setAnimation(RawAnimation.begin().then("drill_v1", Animation.LoopType.LOOP));
                }
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        }
        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return cache;
        }


        @org.jetbrains.annotations.Nullable
        @Override
        public LivingEntity getControllingPassenger() {
            return this.getPassengers().isEmpty() ? null : (LivingEntity) this.getPassengers().get(0);
        }

    @Override
    protected Vec3 getRelativePortalPosition(Direction.Axis pAxis, BlockUtil.FoundRectangle pPortal) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(pAxis, pPortal));
    }

    @Override
        public void stopRiding() {
            super.stopRiding();
        }



        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
            return new DrillContainer(pContainerId, pPlayerInventory, this);
        }
}

package com.hisroyalty.hrbsdrills.util;

import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class UtilLightBlock extends LightBlock implements SimpleWaterloggedBlock {
    public UtilLightBlock(Properties properties) {
        super(properties.strength(-1F).noCollission().randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));

    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (this.isRandomlyTicking(state)) {
            worldIn.scheduleTick(pos, this, 9);
            if (state.getValue(BlockStateProperties.WATERLOGGED)) {
                worldIn.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
            }
            worldIn.updateNeighborsAt(pos, this);
        }

    }

    @Override
    public boolean isPossibleToRespawnInThis(BlockState pState) {
        return true;
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
return true;    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        super.tick(state, level, pos, rand);
        level.scheduleTick(pos, this, 9);

        if (this.isRandomlyTicking(state)) {
            level.scheduleTick(pos, this, 9);
            if (state.getValue(BlockStateProperties.WATERLOGGED)) {
                level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }
        }

        AABB aabb = new AABB(pos).inflate(0.5d);
        List<DrillEntity> list = level.getEntitiesOfClass(DrillEntity.class, aabb);
        boolean hasLightAttachment = !list.isEmpty() && isExist(list);



        if (hasLightAttachment) {
            // light entity is nearby, schedule another update
            level.scheduleTick(pos, state.getBlock(), 9);
        } else {
            this.remove(level, state, pos, Block.UPDATE_ALL);
        }

    }

    protected boolean remove(final Level level, final BlockState state, final BlockPos pos, final int flag) {
        // remove this block and replace with air or water
        final BlockState replaceWith = state.getValue(WATERLOGGED) ? Fluids.WATER.getSource().defaultFluidState().createLegacyBlock()
                : Blocks.AIR.defaultBlockState();
        // replace with air OR water depending on waterlogged state
        return level.setBlock(pos, replaceWith, flag);
    }
    public static boolean isExist(final List<DrillEntity> drills) {
        for (DrillEntity d : drills) {
            if (d.isAlive()) {
                return true;
            }
        }
        return false;
    }

}
package com.hisroyalty.hrbsdrills.upgrade.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class TreeProcessor {
    public static final int MAX_TREE_HEIGHT = 32;
    private static final Direction[] DIRECTIONS = Direction.values();
    private final LevelAccessor level;

    public TreeProcessor(LevelAccessor level) {
        this.level = level;
    }

    public Set<BlockPos> gatherTree(BlockPos startPos) {
        return gatherLogs(startPos);
    }

    private Set<BlockPos> gatherLogs(BlockPos startPos) {
        Set<BlockPos> blocks = new HashSet<>();
        Queue<BlockPos> toProcess = new LinkedList<>();
        Set<BlockPos> processed = new HashSet<>();

        BlockState initialState = level.getBlockState(startPos);
        toProcess.add(startPos);

        while (!toProcess.isEmpty()) {
            BlockPos current = toProcess.poll();

            if (processed.contains(current) || !isTree(level.getBlockState(current), initialState)) {
                continue;
            }

            blocks.add(current);
            processed.add(current);

            for (Direction dir : DIRECTIONS) {
                BlockPos neighbor = current.relative(dir);
                if (neighbor.getY() >= startPos.getY() && blocks.size() < MAX_TREE_HEIGHT) {
                    toProcess.add(neighbor);
                }
            }
        }

        return blocks;
    }

    private boolean isTree(BlockState state, BlockState initialState) {
        return (state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES))
                && (initialState.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES));
    }

    public void processTree(Set<BlockPos> treeBlocks, boolean dropItems, @Nullable Entity breaker) {
        treeBlocks.forEach(pos -> level.destroyBlock(pos, dropItems, breaker));
    }
}
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
    private static final int MAX_LEAVES_DISTANCE = 12;
    private static final Direction[] DIRECTIONS = Direction.values();

    private final LevelAccessor level;

    public TreeProcessor(LevelAccessor level) {
        this.level = level;
    }

    public Set<BlockPos> gatherTree(BlockPos startPos) {
        Set<BlockPos> logs = gatherLogs(startPos);
        Set<BlockPos> leaves = gatherLeaves(logs);

        // Combine logs and leaves into a single tree structure
        logs.addAll(leaves);
        return logs;
    }

    private Set<BlockPos> gatherLogs(BlockPos startPos) {
        Set<BlockPos> logs = new HashSet<>();
        Queue<BlockPos> toProcess = new LinkedList<>();
        Set<BlockPos> processed = new HashSet<>();

        BlockState initialState = level.getBlockState(startPos);
        toProcess.add(startPos);

        while (!toProcess.isEmpty()) {
            BlockPos current = toProcess.poll();

            if (processed.contains(current) || !isLog(level.getBlockState(current), initialState)) {
                continue;
            }

            logs.add(current);
            processed.add(current);

            for (Direction dir : DIRECTIONS) {
                BlockPos neighbor = current.relative(dir);
                if (neighbor.getY() >= startPos.getY() && logs.size() < MAX_TREE_HEIGHT) {
                    toProcess.add(neighbor);
                }
            }
        }

        return logs;
    }

    private Set<BlockPos> gatherLeaves(Set<BlockPos> logs) {
        Set<BlockPos> leaves = new HashSet<>();
        Set<BlockPos> processed = new HashSet<>();
        Queue<BlockPos> toProcess = new LinkedList<>();

        for (BlockPos log : logs) {
            for (Direction dir : DIRECTIONS) {
                BlockPos leafPos = log.relative(dir);
                if (isLeaf(level.getBlockState(leafPos))) {
                    toProcess.add(leafPos);
                }
            }
        }

        while (!toProcess.isEmpty()) {
            BlockPos current = toProcess.poll();
            if (processed.contains(current) || leaves.size() >= MAX_LEAVES_DISTANCE) {
                continue;
            }

            leaves.add(current);
            processed.add(current);

            for (Direction dir : DIRECTIONS) {
                BlockPos neighbor = current.relative(dir);
                if (!processed.contains(neighbor) && isLeaf(level.getBlockState(neighbor))) {
                    toProcess.add(neighbor);
                }
            }
        }

        return leaves;
    }

    private boolean isLog(BlockState state, BlockState initialState) {
        return state.is(BlockTags.LOGS) && state.getBlock() == initialState.getBlock();
    }

    private boolean isLeaf(BlockState state) {
        return state.is(BlockTags.LEAVES) &&
                (!state.hasProperty(BlockStateProperties.PERSISTENT) ||
                        !state.getValue(BlockStateProperties.PERSISTENT));
    }

    public void processTree(Set<BlockPos> treeBlocks, boolean dropItems, @Nullable Entity breaker) {
        treeBlocks.forEach(pos -> level.destroyBlock(pos, dropItems, breaker));
    }
}

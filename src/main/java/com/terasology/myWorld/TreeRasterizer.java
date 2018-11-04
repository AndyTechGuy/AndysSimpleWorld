/*
 * Copyright 2018 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.terasology.myWorld;

import org.terasology.math.ChunkMath;
import org.terasology.math.Region3i;
import org.terasology.math.geom.BaseVector3i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;

import java.util.Map.Entry;

/**
 * Draws the blocks for trees onto the surface of the map.
 */
public class TreeRasterizer implements WorldRasterizer {

    // tree blocks
    private Block trunk;
    private Block leaf;

    @Override
    public void initialize() {
        trunk = CoreRegistry.get(BlockManager.class).getBlock(new BlockUri("core:OakTrunk"));
        leaf = CoreRegistry.get(BlockManager.class).getBlock(new BlockUri("core:GreenLeaf"));
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        TreeFacet treeFacet = chunkRegion.getFacet(TreeFacet.class);

        for (Entry<BaseVector3i, Tree> entry : treeFacet.getWorldEntries().entrySet()) {
            int baseHeight = entry.getValue().getBaseHeight(); // the height of the bottom of the tree to the first leaves.
            int coreHeight = entry.getValue().getCoreHeight(); // the height of the tree region with both the tree trunk and outer leaves.
            int wideHeight = entry.getValue().getWideLeavesHeight(); // the height of the tree region with leaves the same width as the core.
            int thinHeight = entry.getValue().getThinLeavesHeight(); // the height of the tree region with leaves smaller than the core.
            int coreRadius = entry.getValue().getCoreRadius(); // the radius of the core region of the tree.
            int thinRadius = entry.getValue().getThinRadius(); // the radius of the thin region of the tree.

            Vector3i treeBase = new Vector3i(entry.getKey()).addY(1); // tree is placed above surface, so add one to the Y-axis.
            Vector3i treeCorner = new Vector3i(treeBase).sub(coreRadius, 0, coreRadius); // the corner of the tree's bounding box.

            // the total region of the tree.
            Region3i treeArea = Region3i.createFromMinAndSize(
                    treeCorner,
                    new Vector3i((2 * coreRadius) + 1, baseHeight + coreHeight + wideHeight + thinHeight, (2 * coreRadius) + 1));
            // the region containing the trunk and inner branch of the tree.
            Region3i innerLog = Region3i.createFromMinAndSize(
                    treeBase,
                    new Vector3i(1, baseHeight + coreHeight, 1));
            // the region containing leaves with the same width as the total region.
            Region3i coreLeaves = Region3i.createFromMinAndSize(
                    new Vector3i(treeCorner).add(0, baseHeight, 0),
                    new Vector3i((2 * coreRadius) + 1, coreHeight + wideHeight, (2 * coreRadius) + 1));
            // the region containing leaves with a smaller width than the total region.
            Region3i thinLeaves = Region3i.createFromMinAndSize(
                    new Vector3i(treeCorner).add(coreRadius - thinRadius, baseHeight + coreHeight + wideHeight, coreRadius - thinRadius),
                    new Vector3i((2 * thinRadius) + 1, thinHeight, (2 * thinRadius) + 1));

            for (Vector3i newBlockPosition : treeArea) {
                if (chunkRegion.getRegion().encompasses(newBlockPosition)) {
                    if (innerLog.encompasses(newBlockPosition)) { // draws tree trunk.
                        chunk.setBlock(ChunkMath.calcBlockPos(newBlockPosition), trunk);
                    }
                    // leaves in coreLeaves and thinLeaves, excluding innerLog.
                    if ((coreLeaves.encompasses(newBlockPosition) || thinLeaves.encompasses(newBlockPosition)) && !innerLog.encompasses(newBlockPosition)) {
                        chunk.setBlock(ChunkMath.calcBlockPos(newBlockPosition), leaf);
                    }
                }
            }
        }
    }
}

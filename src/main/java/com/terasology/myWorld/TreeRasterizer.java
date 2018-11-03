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
import org.terasology.world.liquid.LiquidData;
import org.terasology.world.liquid.LiquidType;

import java.util.Map.Entry;

public class TreeRasterizer implements WorldRasterizer {
    private Block trunk;
    private Block leaf;
    private Block water;

    @Override
    public void initialize() {
        trunk = CoreRegistry.get(BlockManager.class).getBlock(new BlockUri("core:OakTrunk"));
        leaf = CoreRegistry.get(BlockManager.class).getBlock(new BlockUri("core:GreenLeaf"));
        water = CoreRegistry.get(BlockManager.class).getBlock("core:water");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        TreeFacet treeFacet = chunkRegion.getFacet(TreeFacet.class);

        for(Entry<BaseVector3i, Tree> entry : treeFacet.getWorldEntries().entrySet()) {
            Vector3i treeBase = new Vector3i(entry.getKey()).addY(1);
            Vector3i treeCorner = new Vector3i(treeBase).sub(2, 0, 2);

            int baseHeight = entry.getValue().getBaseHeight();
            int coreHeight = entry.getValue().getCoreHeight();
            int wideHeight = entry.getValue().getWideLeavesHeight();
            int thinHeight = entry.getValue().getThinLeavesHeight();

            Region3i treeArea = Region3i.createFromMinAndSize(treeCorner, new Vector3i(5, baseHeight+coreHeight+wideHeight+thinHeight, 5));
            Region3i innerLog = Region3i.createFromMinAndSize(treeBase, new Vector3i(1, baseHeight+coreHeight, 1));
            Region3i coreLeaves = Region3i.createFromMinAndSize(new Vector3i(treeCorner).add(0, baseHeight, 0), new Vector3i(5, coreHeight+wideHeight, 5));
            Region3i thinLeaves = Region3i.createFromMinAndSize(new Vector3i(treeCorner).add(1, baseHeight+coreHeight+wideHeight, 1), new Vector3i(3, thinHeight, 3));

            for (Vector3i newBlockPosition : treeArea) {
                if(chunkRegion.getRegion().encompasses(newBlockPosition)) {
                    if(innerLog.encompasses(newBlockPosition)) {
                        chunk.setBlock(ChunkMath.calcBlockPos(newBlockPosition), trunk);
                    }
                    if ((coreLeaves.encompasses(newBlockPosition) || thinLeaves.encompasses(newBlockPosition)) && !innerLog.encompasses(newBlockPosition)) {
                        chunk.setBlock(ChunkMath.calcBlockPos(newBlockPosition), leaf);
                    }
                }
            }
        }
    }
}

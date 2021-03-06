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
import org.terasology.math.TeraMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generation.facets.DensityFacet;
import org.terasology.world.generation.facets.SeaLevelFacet;

/**
 * The main rasterizer for Simple World. Places blocks and creates a landscape based on information from
 * SurfaceHeightFacet, DensityFacet, and SeaLevelFacet.
 */
public class SimpleWorldRasterizer implements WorldRasterizer {
    // surface blocks
    private Block dirt;
    private Block grass;
    private Block stone;
    private Block water;
    private Block snow;

    @Override
    public void initialize() {
        dirt = CoreRegistry.get(BlockManager.class).getBlock("Core:Dirt");
        grass = CoreRegistry.get(BlockManager.class).getBlock("Core:Grass");
        stone = CoreRegistry.get(BlockManager.class).getBlock("Core:Stone");
        water = CoreRegistry.get(BlockManager.class).getBlock("Core:water");
        snow = CoreRegistry.get(BlockManager.class).getBlock("Core:Snow");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        DensityFacet densityFacet = chunkRegion.getFacet(DensityFacet.class);
        SeaLevelFacet seaLevelFacet = chunkRegion.getFacet(SeaLevelFacet.class);

        for (Vector3i position : chunkRegion.getRegion()) {
            float surfaceHeight = surfaceHeightFacet.getWorld(position.x, position.z);
            float density = densityFacet.getWorld(position); // distance from the surface.
            float blockHeight = position.y + chunk.getChunkWorldOffsetY(); // Accounts for chunks in y-axis.

            Block block = null;

            if (density >= 2) { // more than 2 blocks above
                block = stone;
            } else if (density >= 0) { // surface to 4 blocks above
                if (TeraMath.floorToInt(surfaceHeight) - position.y > 0 || blockHeight < seaLevelFacet.getSeaLevel()) { // not on surface or below water
                    block = dirt;
                } else if (TeraMath.floorToInt(surfaceHeight) - position.y == 0) { // surface block
                    if (blockHeight > 80) { // snow on mountain
                        block = snow;
                    } else {
                        block = grass;
                    }
                }
            } else { // above surface
                if (blockHeight <= seaLevelFacet.getSeaLevel()) { // below sea level
                    block = water;
                }
            }

            if (block != null) { // null for a block of air
                chunk.setBlock(ChunkMath.calcBlockPos(position), block);
            }
        }
    }
}

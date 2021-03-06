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

import org.terasology.math.TeraMath;
import org.terasology.math.geom.Rect2i;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.WhiteNoise;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetBorder;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

/**
 * Provides the positions of trees to {@link TreeFacet}'s.
 */
@Produces(TreeFacet.class)
@Requires(@Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(bottom = 8, sides = 2)))
public class TreeProvider implements FacetProvider {

    /**
     * The noise that will define the placement of trees.
     */
    private Noise treeNoise;

    @Override
    public void setSeed(long seed) {
        treeNoise = new WhiteNoise(seed);
    }

    @Override
    public void process(GeneratingRegion region) {
        Border3D border = region.getBorderForFacet(TreeFacet.class).extendBy(0, 8, 2);
        TreeFacet facet = new TreeFacet(region.getRegion(), border);
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        SeaLevelFacet seaLevelFacet = region.getRegionFacet(SeaLevelFacet.class);

        Rect2i worldRegion = surfaceHeightFacet.getWorldRegion();

        for (int wz = worldRegion.minY(); wz <= worldRegion.maxY(); wz++) {
            for (int wx = worldRegion.minX(); wx <= worldRegion.maxX(); wx++) {
                int surfaceHeight = TeraMath.floorToInt(surfaceHeightFacet.getWorld(wx, wz));

                if (surfaceHeight >= facet.getWorldRegion().minY() && // no trees below the facet region.
                        surfaceHeight <= facet.getWorldRegion().maxY() && // no trees above the facet region.
                            surfaceHeight > seaLevelFacet.getSeaLevel()) { // no trees in the water.

                    if (treeNoise.noise(wx, wz) > 0.99) { // results in ~1% chance of a tree.
                        facet.setWorld(wx, surfaceHeight, wz, new Tree());
                    }
                }
            }
        }

        region.setRegionFacet(TreeFacet.class, facet);
    }
}

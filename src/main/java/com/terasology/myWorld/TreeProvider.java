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
import org.terasology.world.generation.*;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(TreeFacet.class)
@Requires(@Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(bottom = 8, sides = 2)))
public class TreeProvider implements FacetProvider {

    private Noise treeNoise;

    public TreeProvider(){

    }

    @Override
    public void setSeed(long seed) {
        treeNoise = new WhiteNoise(seed);
    }

    @Override
    public void initialize() {

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

                if(surfaceHeight >= facet.getWorldRegion().minY() &&
                        surfaceHeight <= facet.getWorldRegion().maxY() &&
                            surfaceHeight > seaLevelFacet.getSeaLevel()) {

                    if(treeNoise.noise(wx, wz) > 0.99){
                        facet.setWorld(wx, surfaceHeight, wz, new Tree());
                    }
                }
            }
        }

        region.setRegionFacet(TreeFacet.class, facet);
    }
}

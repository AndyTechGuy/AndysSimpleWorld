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
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2f;
import org.terasology.utilities.procedural.BrownianNoise;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.PerlinNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Updates;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Updates(@Facet(SurfaceHeightFacet.class))
public class OceanProvider implements FacetProvider {

    private Noise oceanNoise;

    @Override
    public void setSeed(long seed) {
        oceanNoise = new SubSampledNoise(new BrownianNoise(new PerlinNoise(seed - 5), 8), new Vector2f(0.001f, 0.001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {
        SurfaceHeightFacet surfaceFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        float oceanDepth = 200;

        Rect2i processRegion = surfaceFacet.getWorldRegion();
        for (BaseVector2i position : processRegion.contents()){
            float subtractiveOceanDepth = oceanNoise.noise(position.x(), position.y()) * oceanDepth;
            subtractiveOceanDepth = TeraMath.clamp(subtractiveOceanDepth, 0, oceanDepth);

            surfaceFacet.setWorld(position, surfaceFacet.getWorld(position) - subtractiveOceanDepth);
        }
    }
}

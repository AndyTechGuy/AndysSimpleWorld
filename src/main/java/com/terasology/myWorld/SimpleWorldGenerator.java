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

import org.terasology.caves.CaveFacetProvider;
import org.terasology.caves.CaveRasterizer;
import org.terasology.registry.In;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.core.world.generator.facetProviders.SeaLevelProvider;
import org.terasology.core.world.generator.facetProviders.SurfaceToDensityProvider;
import org.terasology.engine.SimpleUri;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;
import org.terasology.caves.CaveToDensityProvider;

/**
 * The core generator for Simple World.
 */
@RegisterWorldGenerator(id = "simpleWorld", displayName = "AndyTechGuy's Simple World")
public class SimpleWorldGenerator extends BaseFacetedWorldGenerator {
    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    public SimpleWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    protected WorldBuilder createWorld() {
        return new WorldBuilder(worldGeneratorPluginLibrary)
                .addProvider(new SimpleSurfaceProvider())
                .addProvider(new SeaLevelProvider(30)) // Sea level of 30
                .addProvider(new CaveFacetProvider())
                .addProvider(new SurfaceToDensityProvider())
                .addProvider(new CaveToDensityProvider())
                .addProvider(new MountainProvider())
                .addProvider(new OceanProvider())
                .addProvider(new TreeProvider())
                .addRasterizer(new SimpleWorldRasterizer())
                .addRasterizer(new CaveRasterizer("engine:air"))
                .addRasterizer(new TreeRasterizer());
    }
}

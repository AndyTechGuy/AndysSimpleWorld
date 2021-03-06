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

import org.terasology.math.Region3i;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.facets.base.SparseObjectFacet3D;

/**
 * A facet containing information about tree position within a defined region.
 */
public class TreeFacet extends SparseObjectFacet3D<Tree> {

    /**
     * Creates a new TreeFacet in a specified region and border.
     *
     * @param targetRegion The region for this facet.
     * @param border The border that tree blocks can be contained in.
     */
    public TreeFacet(Region3i targetRegion, Border3D border) {
        super(targetRegion, border);
    }
}

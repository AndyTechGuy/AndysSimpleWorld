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

public class Tree {
    /**
     * The radius of the tree excluding the center block.
     *
     * @return The tree radius.
     */
    public int getRadius() {
        return 4;
    }

    /**
     * The height of the trunk of the tree to the core.
     *
     * @return The base height.
     */
    public int getBaseHeight() {
        return 3;
    }

    /**
     * The height of the core of the tree to the end of the interior branch.
     *
     * @return The core height.
     */
    public int getCoreHeight(){
        return 2;
    }

    /**
     * The height of the top of the core to the highest wide leaf level.
     *
     * @return The wide leaves height.
     */
    public int getWideLeavesHeight(){
        return 1;
    }

    /**
     * The height of the top of the highest wide leaf level to the highest thin leaf level.
     *
     * @return The thin leaves
     */
    public int getThinLeavesHeight(){
        return 1;
    }
}

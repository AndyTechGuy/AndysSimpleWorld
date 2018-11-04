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

/**
 * The variables surrounding a single tree.
 */
public class Tree {

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
     * @return The thin leaves height.
     */
    public int getThinLeavesHeight(){
        return 1;
    }

    /**
     * The radius (excluding the center block) of the core part of the tree.
     *
     * @return The core radius.
     */
    public int getCoreRadius() {
        return 2;
    }

    /**
     * The radius (excluding the center block) of the thin leaves on the top of the tree.
     *
     * @return The thin leaves radius.
     */
    public int getThinRadius(){
        return 1;
    }
}

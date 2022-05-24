/*
 * BlackHoleBedWars
 * Copyright (c) 2022. YumaHisai
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.yumahisai.blholebw.api.arena.generator;

import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.events.gameplay.GameStateChangeEvent;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public interface IGenerator {

    /**
     * Get holograms associated to this generator.
     * Language iso, Hologram instance.
     */
    HashMap<String, IGenHolo> getLanguageHolograms();

    /**
     * Disable a generator and destroy its data.
     */
    void disable();

    /**
     * Manage what to do when the generator upgrade is called from {@link IArena#updateNextEvent}
     */
    void upgrade();

    /**
     * This will attempt to spawn the items every second.
     */
    void spawn();

    /**
     * This will drop the item at a given location.
     *
     * @param location You can customize this location in order to drop items near a player if it's a base generator with multiple teammates.
     */
    void dropItem(Location location);

    /**
     * Change the item that this generator will spawn.
     */
    void setOre(ItemStack ore);

    /**
     * Get the arena assigned to this generator.
     */
    IArena getArena();

    /**
     * This method is called every tick to manage the block rotation.
     */
    void rotate();

    /**
     * Change item spawn delay. In seconds.
     */
    void setDelay(int delay);

    /**
     * Set how many items should the generator spawn at once.
     */
    void setAmount(int amount);

    /**
     * Get the generator location.
     */
    Location getLocation();

    /**
     * Get generator ore.
     */
    ItemStack getOre();

    /**
     * This will hide generator holograms with a different iso.
     *
     * @param iso player language iso.
     */
    void updateHolograms(Player p, String iso);

    /**
     * Enable generator rotation.
     * Make sure it has a helmet set.
     * DIAMOND and EMERALD generator types will get
     * the rotation activated when the arena starts.
     * If you want to have a different rotating type you should call this manually at {@link GameStateChangeEvent}
     */
    void enableRotation();

    /**
     * This is the limit when the generator will stop spawning new items until they are collected.
     */
    void setSpawnLimit(int value);

    /**
     * Get the team assigned to this generator.
     *
     * @return null if this is not a team generator.
     */
    ITeam getBwt();

    /**
     * Get generator hologram holder (armor stand) containing the rotating item.
     *
     * @return null if there is no rotating item.
     */
    ArmorStand getHologramHolder();

    /**
     * Get generator type.
     */
    GeneratorType getType();

    /**
     * Get the amount of items that are dropped once.
     */
    int getAmount();

    /**
     * Get spawn rate delay.
     */
    int getDelay();

    /**
     * Get seconds before next item spawn.
     */
    int getNextSpawn();

    /**
     * Get the spawn limit of the generators.
     * If there is this amount of items dropped near the generator
     * it will stop spawning new items.
     */
    int getSpawnLimit();

    /**
     * Set the remaining time till the next item spawn.
     */
    void setNextSpawn(int nextSpawn);

    /**
     * Should the dropped items be stacked?
     */
    void setStack(boolean stack);

    /**
     * Check if the dropped items can be stacked.
     */
    boolean isStack();

    /**
     * Set generator type.
     * This may break things.
     */
    void setType(GeneratorType type);

    /**
     * This only must be called by the arena instance when it restarts.
     * Do never call it unless you have a custom arena.
     * Manage your data destroy.
     */
    void destroyData();
}

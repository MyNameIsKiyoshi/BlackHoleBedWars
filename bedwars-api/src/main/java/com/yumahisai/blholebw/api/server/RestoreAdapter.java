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

package com.yumahisai.blholebw.api.server;

import com.yumahisai.blholebw.api.arena.IArena;
import org.bukkit.plugin.Plugin;

import java.util.List;

public abstract class RestoreAdapter {

    private Plugin plugin;

    /**
     * @param owner adapter owner.
     */
    public RestoreAdapter(Plugin owner) {
        this.plugin = owner;
    }

    /**
     * Get adapter owner.
     */
    public Plugin getOwner() {
        return plugin;
    }

    /**
     * Load the world.
     * Arenas will be initialized automatically based on WorldLoadEvent.
     */
    public abstract void onEnable(IArena a);

    /**
     * Restore the world.
     * call new Arena when it's done.
     */
    public abstract void onRestart(IArena a);

    /**
     * Unload the world.
     * This is usually used for /bw unloadArena name
     */
    public abstract void onDisable(IArena a);

    /**
     * Load the world for setting it up.
     */
    public abstract void onSetupSessionStart(ISetupSession s);

    /**
     * Unload the world.
     */
    public abstract void onSetupSessionClose(ISetupSession s);

    /**
     * Remove lobby blocks.
     */
    public abstract void onLobbyRemoval(IArena a);

    /**
     * Check if given world exists.
     */
    public abstract boolean isWorld(String name);

    /**
     * Delete a world.
     */
    public abstract void deleteWorld(String name);

    /**
     * Clone an arena world.
     */
    public abstract void cloneArena(String name1, String name2);

    /**
     * Get world container.
     */
    public abstract List<String> getWorldsList();

    /**
     * Convert worlds if it is necessary before loading them.
     * Let them load on BlackHoleBedWars main Thread so they will be converted before getting loaded.
     */
    public abstract void convertWorlds();
}

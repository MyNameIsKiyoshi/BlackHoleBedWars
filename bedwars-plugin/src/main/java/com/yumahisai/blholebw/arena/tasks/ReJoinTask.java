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

package com.yumahisai.blholebw.arena.tasks;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReJoinTask implements Runnable {

    private static final List<ReJoinTask> reJoinTasks = new ArrayList<>();

    private final IArena arena;
    private final ITeam bedWarsTeam;
    private final BukkitTask task;

    public ReJoinTask(IArena arena, ITeam bedWarsTeam) {
        this.arena = arena;
        this.bedWarsTeam = bedWarsTeam;
        task = Bukkit.getScheduler().runTaskLater(BedWars.plugin, this, BedWars.config.getInt(ConfigPath.GENERAL_CONFIGURATION_REJOIN_TIME) * 20L);
    }

    @Override
    public void run() {
        if (arena == null) {
            destroy();
            return;
        }
        if (bedWarsTeam == null) {
            destroy();
            return;
        }
        if (bedWarsTeam.getMembers() == null){
            destroy();
            return;
        }
        if (bedWarsTeam.getMembers().isEmpty()) {
            bedWarsTeam.setBedDestroyed(true);
            destroy();
        }
    }

    /**
     * Get arena
     */
    public IArena getArena() {
        return arena;
    }

    /**
     * Destroy task
     */
    public void destroy() {
        reJoinTasks.remove(this);
        task.cancel();
    }

    /**
     * Get tasks list
     */
    @NotNull
    @Contract(pure = true)
    public static Collection<ReJoinTask> getReJoinTasks() {
        return Collections.unmodifiableCollection(reJoinTasks);
    }

    public void cancel() {
        task.cancel();
    }
}

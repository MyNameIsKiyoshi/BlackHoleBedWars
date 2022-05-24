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

package com.yumahisai.blholebw.halloween;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class CobWebRemover {

    private static final LinkedHashMap<IArena, CobWebRemover> taskByArena = new LinkedHashMap<>();

    private int taskId;
    private IArena arena;
    private LinkedHashMap<Block, Long> cobWebs = new LinkedHashMap<>();

    protected CobWebRemover(IArena arena) {
        taskByArena.remove(arena);
        taskByArena.put(arena, this);
        this.arena = arena;
        taskId = Bukkit.getScheduler().runTaskTimer(BedWars.plugin, new RemovalTask(), 20L, 20L).getTaskId();
    }

    public void addCobWeb(Block block) {
        cobWebs.put(block, System.currentTimeMillis() + 7500L);
    }

    public int getTaskId() {
        return taskId;
    }

    public IArena getArena() {
        return arena;
    }

    public static CobWebRemover getByArena(IArena arena){
        return taskByArena.get(arena);
    }

    public static CobWebRemover getByArenaWorld(String world){
        Optional<Map.Entry<IArena, CobWebRemover>> entry = taskByArena.entrySet().stream().filter(arena -> arena.getKey().getWorldName().equals(world)).findFirst();
        return entry.map(Map.Entry::getValue).orElse(null);
    }

    public void destroy() {
        Bukkit.getScheduler().cancelTask(getTaskId());
        taskByArena.remove(arena);
    }

    private class RemovalTask implements Runnable {

        private final LinkedList<Block> toBeRemoved = new LinkedList<>();

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            cobWebs.forEach((key, value) -> {
                if (value <= currentTime) {
                    toBeRemoved.add(key);
                    if (key.getType().toString().contains("WEB")) {
                        key.breakNaturally();
                    }
                }
            });
            toBeRemoved.forEach(block -> cobWebs.remove(block));
            toBeRemoved.clear();
        }
    }
}

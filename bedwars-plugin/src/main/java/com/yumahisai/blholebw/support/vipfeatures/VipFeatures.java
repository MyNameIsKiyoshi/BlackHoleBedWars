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

package com.yumahisai.blholebw.support.vipfeatures;

import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.arena.Arena;
import com.andrei1058.vipfeatures.api.MiniGame;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class VipFeatures extends MiniGame {

    public VipFeatures(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean isPlaying(Player p) {
        IArena a = Arena.getArenaByPlayer(p);
        if (a != null) {
            return !(a.getStatus() == GameState.waiting || a.getStatus() == GameState.starting);
        }
        return false;
    }

    @Override
    public boolean hasBoosters() {
        return false;
    }

    @Override
    public String getDisplayName() {
        return "BlackHoleBedWars";
    }
}

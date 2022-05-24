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

package com.yumahisai.blholebw.lobbysocket;

import com.yumahisai.blholebw.support.preloadedparty.PreLoadedParty;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.LinkedList;
import java.util.List;

public class LoadedUsersCleaner implements Runnable {

    private final List<LoadedUser> toRemove = new LinkedList<>();

    @Override
    public void run() {
        for (LoadedUser lu : LoadedUser.getLoaded().values()) {
            if (lu.isTimedOut()) {
                toRemove.add(lu);
            }
        }
        if (!toRemove.isEmpty()) {
            toRemove.forEach(c -> {
                OfflinePlayer op = Bukkit.getOfflinePlayer(c.getUuid());
                if (op != null && op.getName() != null) {
                    PreLoadedParty plp = PreLoadedParty.getPartyByOwner(op.getName());
                    if (plp != null) {
                        plp.clean();
                    }
                }
                c.destroy("Removed by cleaner task.");
            });
            toRemove.clear();
        }
    }
}

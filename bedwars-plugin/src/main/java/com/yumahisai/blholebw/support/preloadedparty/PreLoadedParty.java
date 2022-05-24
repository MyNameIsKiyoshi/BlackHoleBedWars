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

package com.yumahisai.blholebw.support.preloadedparty;

import com.yumahisai.blholebw.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PreLoadedParty {

    private String owner;
    private List<Player> members = new ArrayList<>();

    private static ConcurrentHashMap<String, PreLoadedParty> preLoadedParties = new ConcurrentHashMap<>();

    public PreLoadedParty(String owner){
        PreLoadedParty plp = getPartyByOwner(owner);
        if (plp != null){
            plp.clean();
        }
        this.owner = owner;
        preLoadedParties.put(owner, this);
    }

    public static PreLoadedParty getPartyByOwner(String owner){
        return preLoadedParties.getOrDefault(owner, null);
    }

    public void addMember(Player player){
        members.add(player);
    }

    public void teamUp(){
        if (this.owner == null) return;
        Player owner = Bukkit.getPlayer(this.owner);
        if (owner == null) return;
        if (!owner.isOnline()) return;
        for (Player player : members){
            if (!player.getName().equalsIgnoreCase(this.owner)){
                BedWars.getParty().addMember(owner, player);
            }
        }
        preLoadedParties.remove(this.owner);
    }

    public static ConcurrentHashMap<String, PreLoadedParty> getPreLoadedParties() {
        return preLoadedParties;
    }
    public void clean(){
        preLoadedParties.remove(this.owner);
    }
}

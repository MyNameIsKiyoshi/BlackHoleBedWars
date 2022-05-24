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

package com.yumahisai.blholebw.support.citizens;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.arena.Misc;
import com.yumahisai.blholebw.configuration.Sounds;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRemoveEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.yumahisai.blholebw.api.language.Language.getMsg;

public class CitizensListener implements Listener {

    @EventHandler
    public void removeNPC(NPCRemoveEvent e) {
        if (e == null) return;
        List<String> locations = BedWars.config.getYml().getStringList(ConfigPath.GENERAL_CONFIGURATION_NPC_LOC_STORAGE);
        boolean removed = false;
        if (JoinNPC.npcs.containsKey(e.getNPC().getId())) {
            JoinNPC.npcs.remove(e.getNPC().getId());
            removed = true;
        }
        for (String s : new ArrayList<>(locations)) {
            String[] data = s.split(",");
            if (data.length >= 10) {
                if (Misc.isNumber(data[9])) {
                    if (Integer.parseInt(data[9]) == e.getNPC().getId()) {
                        locations.remove(s);
                        removed = true;
                    }
                }
            }
        }
        for (Entity e2 : e.getNPC().getEntity().getNearbyEntities(0, 3, 0)) {
            if (e2.getType() == EntityType.ARMOR_STAND) {
                e2.remove();
            }
        }
        if (removed) BedWars.config.set(ConfigPath.GENERAL_CONFIGURATION_NPC_LOC_STORAGE, locations);
    }

    @EventHandler
    // Citizens support
    public void onNPCInteract(PlayerInteractEntityEvent e) {
        if (!JoinNPC.isCitizensSupport()) return;
        if (e.getPlayer().isSneaking()) return;
        if (!e.getRightClicked().hasMetadata("NPC")) return;
        net.citizensnpcs.api.npc.NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getRightClicked());
        if (npc == null) return;
        if (JoinNPC.npcs.containsKey(npc.getId())) {
            if (!Arena.joinRandomFromGroup(e.getPlayer(), JoinNPC.npcs.get(npc.getId()))) {
                e.getPlayer().sendMessage(getMsg(e.getPlayer(), Messages.COMMAND_JOIN_NO_EMPTY_FOUND));
                Sounds.playSound("join-denied", e.getPlayer());
            } else {
                Sounds.playSound("join-allowed", e.getPlayer());
            }
        }
    }

}

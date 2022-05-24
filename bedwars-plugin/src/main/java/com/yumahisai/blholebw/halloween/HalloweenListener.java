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
import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.events.gameplay.GameStateChangeEvent;
import com.yumahisai.blholebw.api.events.player.PlayerJoinArenaEvent;
import com.yumahisai.blholebw.api.events.player.PlayerKillEvent;
import com.yumahisai.blholebw.api.events.player.PlayerXpGainEvent;
import com.yumahisai.blholebw.api.events.server.ArenaDisableEvent;
import com.yumahisai.blholebw.api.events.server.ArenaEnableEvent;
import com.yumahisai.blholebw.api.events.server.ArenaRestartEvent;
import com.yumahisai.blholebw.arena.Misc;
import com.yumahisai.blholebw.levels.internal.PlayerLevel;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class HalloweenListener implements Listener {

    private final Sound ambienceSound;
    private final Sound ghastSound;

    public HalloweenListener() {
        ambienceSound = Sound.valueOf(BedWars.getForCurrentVersion("AMBIENCE_CAVE", "AMBIENT_CAVE", "AMBIENT_CAVE"));
        ghastSound = Sound.valueOf(BedWars.getForCurrentVersion("GHAST_SCREAM2", "ENTITY_GHAST_SCREAM", "ENTITY_GHAST_SCREAM"));
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.isCancelled()) return;
        LivingEntity entity = e.getEntity();
        if (entity.getType() == EntityType.ARMOR_STAND) return;
        entity.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent e) {
        // check if it is time to disable this special
        if (HalloweenSpecial.getINSTANCE() != null) {
            if (!HalloweenSpecial.checkAvailabilityDate()) {
                CreatureSpawnEvent.getHandlerList().unregister(this);
            }
        }
    }

    @EventHandler
    public void onPlayerDie(PlayerKillEvent e) {
        if (e.getKiller() != null) {
            Location location = e.getVictim().getLocation().add(0, 1, 0);
            if (location.getBlock().getType() == Material.AIR) {
                location.getWorld().playSound(location, ghastSound, 2f, 1f);
                if (!Misc.isBuildProtected(location, e.getArena())) {
                    location.getBlock().setType(Material.valueOf(BedWars.getForCurrentVersion("WEB", "WEB", "COBWEB")));
                    e.getArena().addPlacedBlock(location.getBlock());
                    location.getBlock().setMetadata("give-bw-exp", new FixedMetadataValue(BedWars.plugin, "ok"));
                    CobWebRemover remover = CobWebRemover.getByArena(e.getArena());
                    if (remover != null) {
                        remover.addCobWeb(location.getBlock());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        if (e.getBlock().hasMetadata("give-bw-exp")) {
            PlayerLevel level = PlayerLevel.getLevelByPlayer(e.getPlayer().getUniqueId());
            if (level != null) {
                e.getBlock().getDrops().clear();
                level.addXp(5, PlayerXpGainEvent.XpSource.OTHER);
                e.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "+5 xp!");
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinArenaEvent e) {
        if (!e.isSpectator()) {
            Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), ambienceSound, 3f, 1f), 20L);
        }
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent e) {
        if (e.getNewState() == GameState.restarting) {
            CobWebRemover remover = CobWebRemover.getByArena(e.getArena());
            if (remover != null) {
                remover.destroy();
            }
        }
    }

    @EventHandler
    public void onRestart(ArenaRestartEvent e) {
        CobWebRemover remover = CobWebRemover.getByArenaWorld(e.getWorldName());
        if (remover != null) {
            remover.destroy();
        }
    }

    @EventHandler
    public void onDisable(ArenaDisableEvent e) {
        CobWebRemover remover = CobWebRemover.getByArenaWorld(e.getWorldName());
        if (remover != null) {
            remover.destroy();
        }
    }

    @EventHandler
    public void onEnable(ArenaEnableEvent e){
        new CobWebRemover(e.getArena());
    }
}

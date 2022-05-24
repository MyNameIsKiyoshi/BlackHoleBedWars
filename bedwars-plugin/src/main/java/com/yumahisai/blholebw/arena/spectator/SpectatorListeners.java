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

package com.yumahisai.blholebw.arena.spectator;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.events.player.PlayerKillEvent;
import com.yumahisai.blholebw.api.events.player.PlayerLeaveArenaEvent;
import com.yumahisai.blholebw.api.events.spectator.SpectatorFirstPersonEnterEvent;
import com.yumahisai.blholebw.api.events.spectator.SpectatorFirstPersonLeaveEvent;
import com.yumahisai.blholebw.api.events.spectator.SpectatorTeleportToPlayerEvent;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.configuration.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import static com.yumahisai.blholebw.api.language.Language.getMsg;

public class SpectatorListeners implements Listener {

    @EventHandler
    public void onSpectatorItemInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack i = BedWars.nms.getItemInHand(p);
        if (i == null) return;
        if (i.getType() == Material.AIR) return;
        if (!BedWars.nms.isCustomBedWarsItem(i)) return;
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) return;
        if (!a.isSpectator(p)) return;

        // Disable spectator interact
        e.setCancelled(true);
    }

    @EventHandler
    public void onSpectatorBlockInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (!BedWars.getAPI().getArenaUtil().isSpectating(e.getPlayer())) return;
        if (e.getClickedBlock().getType().toString().contains("DOOR"))
        // Disable spectator interact
        e.setCancelled(true);
    }

    @EventHandler
    public void onSpectatorInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        TeleporterGUI.closeGUI(p);
    }

    @EventHandler
    public void onSpectatorClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getGameMode() == GameMode.SPECTATOR) {
            e.setCancelled(true);
            return;
        }
        if (e.getCurrentItem() == null) return;
        ItemStack i = e.getCurrentItem();
        if (i.getType() == Material.AIR) return;
        Player p = (Player) e.getWhoClicked();
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) return;
        if (!a.isSpectator(p)) return;

        // Teleporter heads
        if (BedWars.nms.isPlayerHead(i.getType().toString(), 3) && BedWars.nms.itemStackDataCompare(i, (short) 3)) {
            if (BedWars.nms.isCustomBedWarsItem(i)) {
                e.setCancelled(true);

                String data = BedWars.nms.getCustomData(i);
                if (data.contains(TeleporterGUI.NBT_SPECTATOR_TELEPORTER_GUI_HEAD)) {
                    String player = data.replace(TeleporterGUI.NBT_SPECTATOR_TELEPORTER_GUI_HEAD, "");
                    Player target = Bukkit.getPlayer(player);
                    if (target == null) return;
                    if (target.isDead()) return;
                    if (!target.isOnline()) return;
                    SpectatorTeleportToPlayerEvent event = new SpectatorTeleportToPlayerEvent(p, target, a);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        p.teleport(target);
                    }
                    Sounds.playSound("spectator-gui-click", p);
                    p.closeInventory();
                }
            }
        }
    }

    @EventHandler
    // Refresh placeholders from GUIs
    public void onHealthChange(EntityRegainHealthEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) return;
        Player p = (Player) e.getEntity();
        IArena arena = Arena.getArenaByPlayer(p);
        if (arena == null) return;
        if (arena.isPlayer(p)) {
            TeleporterGUI.refreshAllGUIs();
        }
    }

    @EventHandler
    // Refresh placeholders from GUIs
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) return;
        Player p = (Player) e.getEntity();
        IArena arena = Arena.getArenaByPlayer(p);
        if (arena == null) return;
        if (arena.isPlayer(p)) {
            TeleporterGUI.refreshAllGUIs();
        }
    }

    @EventHandler
    // Refresh placeholders from GUIs
    public void onPlayerLeave(PlayerLeaveArenaEvent e) {
        if (e.getArena().isPlayer(e.getPlayer())) {
            TeleporterGUI.refreshAllGUIs();
        }
    }

    @EventHandler
    // Triggered when a spectator starts spectating in first person
    public void onSpectatorInteractPlayer(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() != EntityType.PLAYER) return;
        Player p = e.getPlayer();
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) return;
        if (a.isPlayer(p)) return;
        e.setCancelled(true);
        Player target = (Player) e.getRightClicked();
        if (a.isPlayer(target)) {
            if (p.getSpectatorTarget() != null) {
                SpectatorFirstPersonLeaveEvent e2 = new SpectatorFirstPersonLeaveEvent(p, a, player -> getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_TITLE), player -> getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_SUBTITLE));
                Bukkit.getPluginManager().callEvent(e2);
            }
            SpectatorFirstPersonEnterEvent event = new SpectatorFirstPersonEnterEvent(p, target, a, player -> getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_ENTER_TITLE), player -> getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_ENTER_SUBTITLE));
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;
            p.getInventory().setHeldItemSlot(5);
            p.setGameMode(GameMode.SPECTATOR);
            p.setSpectatorTarget(target);
            BedWars.nms.sendTitle(p, event.getTitle().apply(p).replace("{playername}", p.getName()).replace("{player}", target.getDisplayName()), event.getSubTitle().apply(p).replace("{player}", target.getDisplayName()), event.getFadeIn(), event.getStay(), event.getFadeOut());
        }
    }

    @EventHandler
    // Triggered when a spectator leaves first person
    public void onSneak(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        IArena a = Arena.getArenaByPlayer(p);
        if (a == null) return;
        if (a.isSpectator(p) && p.getSpectatorTarget() != null) {
            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(true);
            p.setFlying(true);
            SpectatorFirstPersonLeaveEvent event = new SpectatorFirstPersonLeaveEvent(p, a, player -> getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_TITLE), player -> getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_SUBTITLE));
            Bukkit.getPluginManager().callEvent(event);
            BedWars.nms.sendTitle(p, event.getTitle().apply(p), event.getSubTitle().apply(p), event.getFadeIn(), event.getStay(), event.getFadeOut());
        }
    }

    @EventHandler
    // Prevent game-mode 3 menu
    public void onTeleport(PlayerTeleportEvent e) {
        IArena a = Arena.getArenaByPlayer(e.getPlayer());
        if (a == null) return;
        if (a.isSpectator(e.getPlayer())){
            if (!(e.getTo().getWorld().equals(e.getPlayer().getWorld())) && e.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
                Player p = e.getPlayer();
                e.setCancelled(true);
                p.setGameMode(GameMode.ADVENTURE);
                p.setAllowFlight(true);
                p.setFlying(true);
                SpectatorFirstPersonLeaveEvent event = new SpectatorFirstPersonLeaveEvent(p, Arena.getArenaByPlayer(p), player -> getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_TITLE), player -> getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_SUBTITLE));
                Bukkit.getPluginManager().callEvent(event);
                BedWars.nms.sendTitle(p, event.getTitle().apply(p), event.getSubTitle().apply(p), event.getFadeIn(), event.getStay(), event.getFadeOut());
            }
        }
    }

    @EventHandler
    // Remove from first person on target die
    public void onTargetDeath(PlayerKillEvent e) {
        for (Player p : e.getArena().getSpectators()) {
            if (p.getSpectatorTarget() == null) continue;
            if (p.getSpectatorTarget() == e.getVictim()) {
                p.setGameMode(GameMode.ADVENTURE);
                p.setAllowFlight(true);
                p.setFlying(true);
                SpectatorFirstPersonLeaveEvent event = new SpectatorFirstPersonLeaveEvent(p, e.getArena(), player -> getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_TITLE), player -> getMsg(player, Messages.ARENA_SPECTATOR_FIRST_PERSON_LEAVE_SUBTITLE));
                Bukkit.getPluginManager().callEvent(event);
                BedWars.nms.sendTitle(p, event.getTitle().apply(p), event.getSubTitle().apply(p), event.getFadeIn(), event.getStay(), event.getFadeOut());
            }
        }
    }

    @EventHandler
    // Disable hits from spectators
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;
        IArena a = Arena.getArenaByIdentifier(e.getEntity().getWorld().getName());
        if (a == null) return;
        Player damager = null;
        if (e.getDamager() instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) e.getDamager()).getShooter();
            if (shooter instanceof Player) {
                damager = (Player) shooter;
            }
        } else if (e.getDamager() instanceof Player) {
            damager = (Player) e.getDamager();
            if (a.getRespawnSessions().containsKey(damager)) {
                e.setCancelled(true);
                return;
            }
        } else if (e.getDamager() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) e.getDamager();
            if (tnt.getSource() instanceof Player) {
                damager = (Player) tnt.getSource();
            }
        }
        if (damager == null) return;
        if (a.isSpectator(damager)) {
            e.setCancelled(true);
        }
    }
}

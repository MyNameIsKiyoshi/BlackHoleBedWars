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

package com.yumahisai.blholebw.listeners;

import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.events.player.PlayerGeneratorCollectEvent;
import com.yumahisai.blholebw.api.server.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static com.yumahisai.blholebw.support.version.common.VersionCommon.api;

public class ItemDropPickListener {

    // 1.11 or older
    public static class PlayerDrop implements Listener {
        @EventHandler
        public void onDrop(PlayerDropItemEvent e){
            if (manageDrop(e.getPlayer(), e.getItemDrop())) e.setCancelled(true);
        }
    }

    // 1.11 or older
    public static class PlayerPickup implements Listener {
        @SuppressWarnings("deprecation")
        @EventHandler
        public void onDrop(PlayerPickupItemEvent e){
            if (managePickup(e.getItem(), e.getPlayer())) e.setCancelled(true);
        }
    }

    // 1.13 or newer
    public static class EntityDrop implements Listener {
        @EventHandler
        public void onDrop(EntityDropItemEvent e){
            if (manageDrop(e.getEntity(), e.getItemDrop())) e.setCancelled(true);
        }
    }

    // 1.12 or newer
    public static class EntityPickup implements Listener {
        @EventHandler
        public void onPickup(EntityPickupItemEvent e){
            if (managePickup(e.getItem(), e.getEntity())) e.setCancelled(true);
        }
    }

    // 1.9 or newer
    public static class ArrowCollect implements Listener {
        @EventHandler
        public void onArrowPick(PlayerPickupArrowEvent e){
            if (api.getArenaUtil().isSpectating(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }

    // common
    public static class GeneratorCollect implements Listener {
        @EventHandler
        //Prevent AFK players from picking items
        public void onCollect(PlayerGeneratorCollectEvent e){
            if (api.getAFKUtil().isPlayerAFK(e.getPlayer())){
                e.setCancelled(true);
            }
        }
    }

    /**
     * @return true if event should be cancelled
     */
    private static boolean managePickup(Item item, LivingEntity player) {
        if (!(player instanceof Player)) return false;
        if (api.getServerType() != ServerType.BUNGEE) {
            //noinspection ConstantConditions
            if (player.getLocation().getWorld().getName().equalsIgnoreCase(api.getLobbyWorld())) {
                return true;
            }
        }
        IArena a = api.getArenaUtil().getArenaByPlayer((Player) player);
        if (a == null) return false;
        if (!a.isPlayer((Player) player)) {
            return true;
        }
        if (a.getStatus() != GameState.playing) {
            return true;
        }
        if (a.getRespawnSessions().containsKey(player)) {
            return true;
        }
        if (item.getItemStack().getType() == Material.ARROW) {
            item.setItemStack(api.getVersionSupport().createItemStack(item.getItemStack().getType().toString(), item.getItemStack().getAmount(), (short) 0));
            return false;
        }

        if (item.getItemStack().getType().toString().equals("BED")) {
            item.remove();
            return true;
        } else if (item.getItemStack().hasItemMeta()) {
            //noinspection ConstantConditions
            if (item.getItemStack().getItemMeta().hasDisplayName()) {
                if (item.getItemStack().getItemMeta().getDisplayName().contains("custom")) {
                    Material material = item.getItemStack().getType();
                    ItemMeta itemMeta = new ItemStack(material).getItemMeta();

                    //Call ore pick up event
                    PlayerGeneratorCollectEvent event = new PlayerGeneratorCollectEvent((Player) player, item, a);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return true;
                    } else {
                        item.getItemStack().setItemMeta(itemMeta);
                    }
                }
            }
        }
        return false;
    }

    /**
     * @return true to cancel the event.
     */
    private static boolean manageDrop(Entity player, Item item) {
        if (!(player instanceof Player)) return false;
        if (api.getServerType() != ServerType.BUNGEE) {
            //noinspection ConstantConditions
            if (player.getLocation().getWorld().getName().equalsIgnoreCase(api.getLobbyWorld())) {
                return true;
            }
        }
        IArena a = api.getArenaUtil().getArenaByPlayer((Player) player);
        if (a == null) return false;

        if (!a.isPlayer((Player) player)) {
            return true;
        }

        if (a.getStatus() != GameState.playing) {
            return true;
        } else {
            ItemStack i = item.getItemStack();
            if (i.getType() == Material.COMPASS) {
                return true;
            }
        }

        return a.getRespawnSessions().containsKey(player);
    }
}

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

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.api.server.ServerType;
import com.yumahisai.blholebw.arena.Arena;
import com.yumahisai.blholebw.configuration.Sounds;
import com.yumahisai.blholebw.shop.ShopCache;
import com.yumahisai.blholebw.shop.listeners.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Openable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import static com.yumahisai.blholebw.api.language.Language.getMsg;

public class Interact implements Listener {

    private final double fireballSpeedMultiplier;
    private final double fireballCooldown;
    private final float fireballExplosionSize;

    public Interact() {
        this.fireballSpeedMultiplier = BedWars.config.getYml().getDouble(ConfigPath.GENERAL_FIREBALL_SPEED_MULTIPLIER);
        this.fireballCooldown = BedWars.config.getYml().getDouble(ConfigPath.GENERAL_FIREBALL_COOLDOWN);
        this.fireballExplosionSize = (float) BedWars.config.getYml().getDouble(ConfigPath.GENERAL_FIREBALL_EXPLOSION_SIZE);
    }

    @EventHandler
    /* Handle custom items with commands on them */
    public void onItemCommand(PlayerInteractEvent e) {
        if (e == null) return;
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack i = BedWars.nms.getItemInHand(p);
            if (!BedWars.nms.isCustomBedWarsItem(i)) return;
            final String[] customData = BedWars.nms.getCustomData(i).split("_");
            if (customData.length >= 2) {
                if (customData[0].equals("RUNCOMMAND")) {
                    e.setCancelled(true);
                    Bukkit.getScheduler().runTask(BedWars.plugin, () -> Bukkit.dispatchCommand(p, customData[1]));
                }
            }
        }
    }

    @EventHandler
    //Check if player is opening an inventory
    public void onInventoryInteract(PlayerInteractEvent e) {
        if (e == null) return;
        if (e.isCancelled()) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block b = e.getClickedBlock();
        if (b == null) return;
        if ((BedWars.getServerType() == ServerType.MULTIARENA && b.getWorld().getName().equals(BedWars.getLobbyWorld()) && !BreakPlace.isBuildSession(e.getPlayer())) || Arena.getArenaByPlayer(e.getPlayer()) != null) {
            if (b.getType() == BedWars.nms.materialCraftingTable() && BedWars.config.getBoolean(ConfigPath.GENERAL_CONFIGURATION_DISABLE_CRAFTING)) {
                e.setCancelled(true);
            } else if (b.getType() == BedWars.nms.materialEnchantingTable() && BedWars.config.getBoolean(ConfigPath.GENERAL_CONFIGURATION_DISABLE_ENCHANTING)) {
                e.setCancelled(true);
            } else if (b.getType() == Material.FURNACE && BedWars.config.getBoolean(ConfigPath.GENERAL_CONFIGURATION_DISABLE_FURNACE)) {
                e.setCancelled(true);
            } else if (b.getType() == Material.BREWING_STAND && BedWars.config.getBoolean(ConfigPath.GENERAL_CONFIGURATION_DISABLE_BREWING_STAND)) {
                e.setCancelled(true);
            } else if (b.getType() == Material.ANVIL && BedWars.config.getBoolean(ConfigPath.GENERAL_CONFIGURATION_DISABLE_ANVIL)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e == null) return;
        Player p = e.getPlayer();
        Arena.afkCheck.remove(p.getUniqueId());
        if (BedWars.getAPI().getAFKUtil().isPlayerAFK(e.getPlayer())) {
            BedWars.getAPI().getAFKUtil().setPlayerAFK(e.getPlayer(), false);
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (b == null) return;
            if (b.getType() == Material.AIR) return;
            IArena a = Arena.getArenaByPlayer(p);
            if (a != null) {
                if (a.getRespawnSessions().containsKey(e.getPlayer())) {
                    e.setCancelled(true);
                    return;
                }
                if (BedWars.nms.isBed(b.getType())) {
                    if (p.isSneaking()) {
                        ItemStack i = BedWars.nms.getItemInHand(p);
                        if (i == null) {
                            e.setCancelled(true);
                        } else if (i.getType() == Material.AIR) {
                            e.setCancelled(true);
                        }
                    } else {
                        e.setCancelled(true);
                    }
                    return;
                }
                if (b.getType() == Material.CHEST) {
                    if (a.isSpectator(p) || a.getRespawnSessions().containsKey(p)) {
                        e.setCancelled(true);
                        return;
                    }
                    //make it so only team members can open chests while team is alive, and all when is eliminated
                    ITeam owner = null;
                    int isRad = a.getConfig().getInt(ConfigPath.ARENA_ISLAND_RADIUS);
                    for (ITeam t : a.getTeams()) {
                        if (t.getSpawn().distance(e.getClickedBlock().getLocation()) <= isRad) {
                            owner = t;
                        }
                    }
                    if (owner != null) {
                        if (!owner.isMember(p)) {
                            if (!(owner.getMembers().isEmpty() && owner.isBedDestroyed())) {
                                e.setCancelled(true);
                                p.sendMessage(getMsg(p, Messages.INTERACT_CHEST_CANT_OPEN_TEAM_ELIMINATED));
                            }
                        }
                    }
                }
                if (a.isSpectator(p) || a.getRespawnSessions().containsKey(p)) {
                    switch (b.getType().toString()) {
                        case "CHEST":
                        case "ENDER_CHEST":
                        case "ANVIL":
                        case "WORKBENCH":
                        case "HOPPER":
                        case "TRAPPED_CHEST":
                        case "CRAFTING_TABLE":
                            e.setCancelled(true);
                            break;
                    }
                    if (b.getState() instanceof Openable) {
                        e.setCancelled(true);
                    }
                }
            }
            if (b.getState() instanceof Sign) {
                for (IArena a1 : Arena.getArenas()) {
                    if (a1.getSigns().contains(b)) {
                        if (a1.addPlayer(p, false)) {
                            Sounds.playSound("join-allowed", p);
                        } else {
                            Sounds.playSound("join-denied", p);
                        }
                        return;
                    }
                }
            }
        }
        //check hand
        ItemStack inHand = e.getItem();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (inHand == null) return;
            IArena a = Arena.getArenaByPlayer(p);
            if (a != null) {
                if (a.isPlayer(p)) {
                    if (inHand.getType() == BedWars.nms.materialFireball()) {

                        e.setCancelled(true);

                        if(System.currentTimeMillis() - a.getFireballCooldowns().getOrDefault(p.getUniqueId(), 0L) > (fireballCooldown*1000)) {
                            a.getFireballCooldowns().put(p.getUniqueId(), System.currentTimeMillis());
                            Fireball fb = p.launchProjectile(Fireball.class);
                            Vector direction = p.getEyeLocation().getDirection();
                            fb = BedWars.nms.setFireballDirection(fb, direction);
                            fb.setVelocity(fb.getDirection().multiply(fireballSpeedMultiplier));
                            //fb.setIsIncendiary(false); // apparently this on <12 makes the fireball not explode on hit. wtf bukkit?
                            fb.setYield(fireballExplosionSize);
                            fb.setMetadata("blholebw", new FixedMetadataValue(BedWars.plugin, "ceva"));
                            BedWars.nms.minusAmount(p, inHand, 1);
                        }

                    }
                }
            }
        }
    }



    @EventHandler
    public void disableItemFrameRotation(PlayerInteractEntityEvent e) {
        if (e == null) return;
        if (e.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            if (((ItemFrame) e.getRightClicked()).getItem().getType().equals(Material.AIR)) {
                //prevent from putting upgradable items in it
                ItemStack i = BedWars.nms.getItemInHand(e.getPlayer());
                if (i != null) {
                    if (i.getType() != Material.AIR) {
                        ShopCache sc = ShopCache.getShopCache(e.getPlayer().getUniqueId());
                        if (sc != null) {
                            if (InventoryListener.shouldCancelMovement(i, sc)) {
                                e.setCancelled(true);
                            }
                        }
                    }
                }
                return;
            }
            IArena a = Arena.getArenaByIdentifier(e.getPlayer().getWorld().getName());
            if (a != null) {
                e.setCancelled(true);
            }
            if (BedWars.getServerType() == ServerType.MULTIARENA) {
                if (BedWars.getLobbyWorld().equals(e.getPlayer().getWorld().getName()) && !BreakPlace.isBuildSession(e.getPlayer())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        if (e == null) return;
        IArena a = Arena.getArenaByPlayer(e.getPlayer());
        if (a == null) return;
        Location l = e.getRightClicked().getLocation();
        for (ITeam t : a.getTeams()) {
            Location l2 = t.getShop(), l3 = t.getTeamUpgrades();
            if (l.getBlockX() == l2.getBlockX() && l.getBlockY() == l2.getBlockY() && l.getBlockZ() == l2.getBlockZ()) {
                e.setCancelled(true);
            } else if (l.getBlockX() == l3.getBlockX() && l.getBlockY() == l3.getBlockY() && l.getBlockZ() == l3.getBlockZ()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent e) {
        if (e == null) return;
        if (Arena.getArenaByPlayer(e.getPlayer()) != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onArmorManipulate(PlayerArmorStandManipulateEvent e) {
        if (e == null) return;
        if (e.isCancelled()) return;
        //prevent from breaking generators
        if (Arena.getArenaByPlayer(e.getPlayer()) != null) {
            e.setCancelled(true);
        }

        //prevent from stealing from armor stands in lobby
        if (BedWars.getServerType() == ServerType.MULTIARENA && e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld()) && !BreakPlace.isBuildSession(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrafting(PrepareItemCraftEvent e) {
        if (e == null) return;
        if (Arena.getArenaByPlayer((Player) e.getView().getPlayer()) != null) {
            if (BedWars.config.getBoolean(ConfigPath.GENERAL_CONFIGURATION_DISABLE_CRAFTING)) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }
}

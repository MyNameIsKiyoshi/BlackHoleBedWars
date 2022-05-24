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

package com.yumahisai.blholebw.shop.main;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.shop.IBuyItem;
import com.yumahisai.blholebw.api.arena.team.TeamEnchant;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.configuration.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("WeakerAccess")
public class BuyItem implements IBuyItem {

    private ItemStack itemStack;
    private boolean autoEquip = false;
    private boolean permanent = false;
    private boolean loaded = false;
    private final String upgradeIdentifier;

    /**
     * Create a shop item
     */
    public BuyItem(String path, YamlConfiguration yml, String upgradeIdentifier, ContentTier parent) {
        BedWars.debug("Loading BuyItems: " + path);
        this.upgradeIdentifier = upgradeIdentifier;

        if (yml.get(path + ".material") == null) {
            BedWars.plugin.getLogger().severe("BuyItem: Material not set at " + path);
            return;
        }

        itemStack = BedWars.nms.createItemStack(yml.getString(path + ".material"),
                yml.get(path + ".amount") == null ? 1 : yml.getInt(path + ".amount"),
                (short) (yml.get(path + ".data") == null ? 1 : yml.getInt(path + ".data")));

        if (yml.get(path + ".name") != null) {
            ItemMeta im = itemStack.getItemMeta();
            if (im != null) {
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r"+yml.getString(path + ".name")));
                itemStack.setItemMeta(im);
            }
        }

        if (yml.get(path + ".enchants") != null && itemStack.getItemMeta() != null) {
            ItemMeta imm = itemStack.getItemMeta();
            String[] enchant = yml.getString(path + ".enchants").split(",");
            for (String enc : enchant) {
                String[] stuff = enc.split(" ");
                try {
                    Enchantment.getByName(stuff[0]);
                } catch (Exception ex) {
                    BedWars.plugin.getLogger().severe("BuyItem: Invalid enchants " + stuff[0] + " at: " + path + ".enchants");
                    continue;
                }
                int ieee = 1;
                if (stuff.length >= 2) {
                    try {
                        ieee = Integer.parseInt(stuff[1]);
                    } catch (Exception exx) {
                        BedWars.plugin.getLogger().severe("BuyItem: Invalid int " + stuff[1] + " at: " + path + ".enchants");
                        continue;
                    }
                }
                imm.addEnchant(Enchantment.getByName(stuff[0]), ieee, true);
            }
            itemStack.setItemMeta(imm);
        }

        if (yml.get(path + ".potion") != null && (itemStack.getType() == Material.POTION)) {
            // 1.16+ custom color
            if (yml.getString(path + ".potion-color") != null && !yml.getString(path + ".potion-color").isEmpty()) {
                itemStack = BedWars.nms.setTag(itemStack, "CustomPotionColor", yml.getString(path + ".potion-color"));
            }
            PotionMeta imm = (PotionMeta) itemStack.getItemMeta();
            if (imm != null) {
                String[] enchant = yml.getString(path + ".potion").split(",");
                for (String enc : enchant) {
                    String[] stuff = enc.split(" ");
                    try {
                        PotionEffectType.getByName(stuff[0].toUpperCase());
                    } catch (Exception ex) {
                        BedWars.plugin.getLogger().severe("BuyItem: Invalid potion effect " + stuff[0] + " at: " + path + ".potion");
                        continue;
                    }
                    int duration = 50, amplifier = 1;
                    if (stuff.length >= 3) {
                        try {
                            duration = Integer.parseInt(stuff[1]);
                        } catch (Exception exx) {
                            BedWars.plugin.getLogger().severe("BuyItem: Invalid int (duration) " + stuff[1] + " at: " + path + ".potion");
                            continue;
                        }
                        try {
                            amplifier = Integer.parseInt(stuff[2]);
                        } catch (Exception exx) {
                            BedWars.plugin.getLogger().severe("BuyItem: Invalid int (amplifier) " + stuff[2] + " at: " + path + ".potion");
                            continue;
                        }
                    }
                    imm.addCustomEffect(new PotionEffect(PotionEffectType.getByName(stuff[0].toUpperCase()), duration * 20, amplifier), true);
                }
                itemStack.setItemMeta(imm);
            }

            itemStack = BedWars.nms.setTag(itemStack, "Potion", "minecraft:water");
            if (parent.getItemStack().getType() == Material.POTION && imm != null && !imm.getCustomEffects().isEmpty()) {
                ItemStack parentItemStack = parent.getItemStack();
                if (parentItemStack.getItemMeta() != null) {
                    PotionMeta potionMeta = (PotionMeta) parentItemStack.getItemMeta();
                    for (PotionEffect potionEffect : imm.getCustomEffects()) {
                        potionMeta.addCustomEffect(potionEffect, true);
                    }
                    parentItemStack.setItemMeta(potionMeta);
                }
                parentItemStack = BedWars.nms.setTag(parentItemStack, "Potion", "minecraft:water");
                parent.setItemStack(parentItemStack);
            }
        }

        if (yml.get(path + ".auto-equip") != null) {
            autoEquip = yml.getBoolean(path + ".auto-equip");
        }
        if (yml.get(upgradeIdentifier + "." + ConfigPath.SHOP_CATEGORY_CONTENT_IS_PERMANENT) != null) {
            permanent = yml.getBoolean(upgradeIdentifier + "." + ConfigPath.SHOP_CATEGORY_CONTENT_IS_PERMANENT);

        }

        loaded = true;
    }

    /**
     * Check if object created properly
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Give to a player
     */
    public void give(Player player, IArena arena) {

        ItemStack i = itemStack.clone();
        BedWars.debug("Giving BuyItem: " + getUpgradeIdentifier() + " to: " + player.getName());

        if (autoEquip && BedWars.nms.isArmor(itemStack)) {
            Material m = i.getType();

            ItemMeta im = i.getItemMeta();
            // idk dadea erori
            if (arena.getTeam(player) == null) {
                BedWars.debug("Could not give BuyItem to " + player.getName() + " - TEAM IS NULL");
                return;
            }
            if (im != null) {
                for (TeamEnchant e : arena.getTeam(player).getArmorsEnchantments()) {
                    im.addEnchant(e.getEnchantment(), e.getAmplifier(), true);
                }
                if (permanent) BedWars.nms.setUnbreakable(im);
                i.setItemMeta(im);
            }

            if (m == Material.LEATHER_HELMET || m == Material.CHAINMAIL_HELMET || m == Material.DIAMOND_HELMET || m == BedWars.nms.materialGoldenHelmet() || m == Material.IRON_HELMET) {
                if (permanent) i = BedWars.nms.setShopUpgradeIdentifier(i, upgradeIdentifier);
                player.getInventory().setHelmet(i);
            } else if (m == Material.LEATHER_CHESTPLATE || m == Material.CHAINMAIL_CHESTPLATE || m == BedWars.nms.materialGoldenChestPlate() || m == Material.DIAMOND_CHESTPLATE || m == Material.IRON_CHESTPLATE) {
                if (permanent) i = BedWars.nms.setShopUpgradeIdentifier(i, upgradeIdentifier);
                player.getInventory().setChestplate(i);
            } else if (m == Material.LEATHER_LEGGINGS || m == Material.CHAINMAIL_LEGGINGS || m == Material.DIAMOND_LEGGINGS || m == BedWars.nms.materialGoldenLeggings() || m == Material.IRON_LEGGINGS) {
                if (permanent) i = BedWars.nms.setShopUpgradeIdentifier(i, upgradeIdentifier);
                player.getInventory().setLeggings(i);
            } else {
                if (permanent) i = BedWars.nms.setShopUpgradeIdentifier(i, upgradeIdentifier);
                player.getInventory().setBoots(i);
            }
            player.updateInventory();
            Sounds.playSound("shop-auto-equip", player);

            Bukkit.getScheduler().runTaskLater(BedWars.plugin, () -> {
                // #274
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    for (Player p : arena.getPlayers()) {
                        BedWars.nms.hideArmor(player, p);
                    }
                }
                //
            }, 20L);
            return;
        } else {

            ItemMeta im = i.getItemMeta();
            i = BedWars.nms.colourItem(i, arena.getTeam(player));
            if (im != null) {
                if (permanent) BedWars.nms.setUnbreakable(im);

                if (i.getType() == Material.BOW) {
                    if (permanent) BedWars.nms.setUnbreakable(im);
                    for (TeamEnchant e : arena.getTeam(player).getBowsEnchantments()) {
                        im.addEnchant(e.getEnchantment(), e.getAmplifier(), true);
                    }
                } else if (BedWars.nms.isSword(i) || BedWars.nms.isAxe(i)) {
                    for (TeamEnchant e : arena.getTeam(player).getSwordsEnchantments()) {
                        im.addEnchant(e.getEnchantment(), e.getAmplifier(), true);
                    }
                }
                i.setItemMeta(im);
            }

            if (permanent) {
                i = BedWars.nms.setShopUpgradeIdentifier(i, upgradeIdentifier);
            }
        }

        //Remove swords with lower damage
        if (BedWars.nms.isSword(i)) {
            for (ItemStack itm : player.getInventory().getContents()) {
                if (itm == null) continue;
                if (itm.getType() == Material.AIR) continue;
                if (!BedWars.nms.isSword(itm)) continue;
                if (itm == i) continue;
                if (BedWars.nms.isCustomBedWarsItem(itm) && BedWars.nms.getCustomData(itm).equals("DEFAULT_ITEM")) {
                    if (BedWars.nms.getDamage(itm) <= BedWars.nms.getDamage(i)) {
                        player.getInventory().remove(itm);
                    }
                }
            }
        }
        //
        player.getInventory().addItem(i);
        player.updateInventory();
    }


    /**
     * Get upgrade identifier.
     * Used to remove old tier items.
     */
    public String getUpgradeIdentifier() {
        return upgradeIdentifier;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean isAutoEquip() {
        return autoEquip;
    }

    public void setAutoEquip(boolean autoEquip) {
        this.autoEquip = autoEquip;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }
}

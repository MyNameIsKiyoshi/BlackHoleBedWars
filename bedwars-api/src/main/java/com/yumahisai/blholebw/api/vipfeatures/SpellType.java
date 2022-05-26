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

package com.yumahisai.blholebw.api.vipfeatures;

import com.yumahisai.blholebw.api.vipfeatures.event.BlockChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.function.Function;

public enum SpellType {

    NONE(null, ""),
    EXPLOSIVE(location -> {
        location.getWorld().createExplosion(location, 2f);
        return null;
    }, "vipfeatures.spells.explosive"),
    FIRE(location -> {
        BlockChangeEvent bce = new BlockChangeEvent(location, location.getBlock().getType(), Material.FIRE);
        if (!bce.isCancelled()) {
            location.getBlock().setType(Material.FIRE);
        }
        return null;
    }, "vipfeatures.spells.fire"),
    WEB(location -> {
        IVipFeatures api = Bukkit.getServicesManager().getRegistration(IVipFeatures.class).getProvider();
        BlockChangeEvent bce = new BlockChangeEvent(location, location.getBlock().getType(), Material.valueOf(api.getVersionUtil().getForCurrentVersion("WEB", "WEB", "COBWEB")));
        if (!bce.isCancelled()) {
            location.getBlock().setType(Material.valueOf(api.getVersionUtil().getForCurrentVersion("WEB", "WEB", "COBWEB")));
        }
        return null;
    }, "vipfeatures.spells.web"),
    ZOMBIE(location -> {
        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setHealth(4);
        return zombie;
    }, "vipfeatures.spells.zombie"),
    POISON(location -> {
        Potion potion = new Potion(PotionType.POISON, 1);
        potion.setSplash(true);
        ItemStack iStack = new ItemStack(Material.POTION);
        potion.apply(iStack);
        ThrownPotion thrownPotion = (ThrownPotion) location.getWorld().spawnEntity(location, EntityType.SPLASH_POTION);
        thrownPotion.setItem(iStack);
        return null;
    }, "vipfeatures.spells.poison");

    private final Function<Location, Object> handler;
    private final String permission;

    SpellType(Function<Location, Object> handler, String permission) {
        this.handler = handler;
        this.permission = permission;
    }

    public void execute(Location location, Player spellOwner) {
        if (handler != null) {
            Object o = handler.apply(location);
            if (o != null){
                if (o instanceof Zombie){
                    IVipFeatures api = Bukkit.getServicesManager().getRegistration(IVipFeatures.class).getProvider();
                    Zombie zombie = (Zombie) o;
                    zombie.setMetadata(api.getSpellsUtil().getZombieOwnerMetaKey(), new FixedMetadataValue(api.getVipFeatures(), spellOwner.getName()));
                }
            }
        }
    }

    public String getPermission() {
        return permission;
    }
}

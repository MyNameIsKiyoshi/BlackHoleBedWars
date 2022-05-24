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

package com.yumahisai.blholebw.support.version.common;

import com.yumahisai.blholebw.api.BedWars;
import com.yumahisai.blholebw.api.server.VersionSupport;
import com.yumahisai.blholebw.listeners.Interact_1_13Plus;
import com.yumahisai.blholebw.listeners.ItemDropPickListener;
import com.yumahisai.blholebw.listeners.SwapItem;
import com.yumahisai.blholebw.shop.defaultrestore.ShopItemRestoreListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class VersionCommon {

    public static BedWars api;

    public VersionCommon(VersionSupport versionSupport) {
            //noinspection ConstantConditions
            api = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
            // 9 and newer
            if (versionSupport.getVersion() > 1) {
                registerListeners(versionSupport.getPlugin(), new SwapItem(), new ItemDropPickListener.ArrowCollect());
            }
            // 11 and older
            if (versionSupport.getVersion() < 5){
                registerListeners(versionSupport.getPlugin() /*, new ItemDropPickListener.PlayerDrop()*/, new ItemDropPickListener.PlayerPickup()/*,
                        new ShopItemRestoreListener.PlayerDrop()*/, new ShopItemRestoreListener.PlayerPickup());
            }

            // 13 and newer
            if (versionSupport.getVersion() > 5){
                registerListeners(versionSupport.getPlugin(), new ShopItemRestoreListener.EntityDrop(), new Interact_1_13Plus(), new ItemDropPickListener.EntityDrop());
            }

            // 1.12 and newer
            if (versionSupport.getVersion() > 4){
                registerListeners(versionSupport.getPlugin(), new ItemDropPickListener.EntityPickup(), new ShopItemRestoreListener.EntityPickup());
            }

            // 1.12 drop listeners
            //if (versionSupport.getVersion() == 5){
                // common
                registerListeners(versionSupport.getPlugin(), new ItemDropPickListener.PlayerDrop(), new ShopItemRestoreListener.PlayerDrop());
            //}

            // common
            registerListeners(versionSupport.getPlugin(), new ItemDropPickListener.GeneratorCollect(), new ShopItemRestoreListener.DefaultRestoreInvClose());
    }

    private void registerListeners(Plugin plugin, Listener... listener) {
        for (Listener l : listener) {
            plugin.getServer().getPluginManager().registerEvents(l, plugin);
        }
    }
}

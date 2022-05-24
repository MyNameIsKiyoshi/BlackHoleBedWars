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

package com.yumahisai.blholebw.shop.quickbuy;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.shop.ShopManager;
import com.yumahisai.blholebw.shop.main.CategoryContent;
import com.yumahisai.blholebw.shop.main.ShopCategory;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class QuickBuyTask extends BukkitRunnable {

    private UUID uuid;


    public QuickBuyTask(UUID uuid){
        this.uuid = uuid;
        this.runTaskLaterAsynchronously(BedWars.plugin, 20*7);
    }

    @Override
    public void run() {
        if (Bukkit.getPlayer(uuid) == null){
            cancel();
            return;
        }
        if (Bukkit.getPlayer(uuid).isOnline()){
            PlayerQuickBuyCache cache = PlayerQuickBuyCache.getQuickBuyCache(uuid);
            if (cache == null){
                cancel();
                return;
            }

            if (!BedWars.getRemoteDatabase().hasQuickBuy(uuid)){
                if (BedWars.shop.getYml().get(ConfigPath.SHOP_QUICK_DEFAULTS_PATH) != null){
                    for (String s : BedWars.shop.getYml().getConfigurationSection(ConfigPath.SHOP_QUICK_DEFAULTS_PATH).getKeys(false)) {
                        if (BedWars.shop.getYml().get(ConfigPath.SHOP_QUICK_DEFAULTS_PATH + "." + s + ".path") != null) {
                            if (BedWars.shop.getYml().get(ConfigPath.SHOP_QUICK_DEFAULTS_PATH + "." + s + ".slot") == null){
                                continue;
                            }

                            try {
                                Integer.valueOf(BedWars.shop.getYml().getString(ConfigPath.SHOP_QUICK_DEFAULTS_PATH + "." + s + ".slot"));
                            } catch (Exception ex){
                                BedWars.debug(BedWars.shop.getYml().getString(ConfigPath.SHOP_QUICK_DEFAULTS_PATH + "." + s + ".slot") + " must be an integer!");
                                continue;
                            }

                            for (ShopCategory sc : ShopManager.getShop().getCategoryList()) {
                                for (CategoryContent cc : sc.getCategoryContentList()) {
                                    if (cc.getIdentifier().equals(BedWars.shop.getYml().getString(ConfigPath.SHOP_QUICK_DEFAULTS_PATH + "." + s + ".path"))) {
                                        cache.setElement(Integer.parseInt(BedWars.shop.getYml().getString(ConfigPath.SHOP_QUICK_DEFAULTS_PATH + "." + s + ".slot")), cc);
                                    }
                                }
                            }

                        }
                    }
                }
            } else {
                // slot, identifier
                HashMap<Integer, String> items = BedWars.getRemoteDatabase().getQuickBuySlots(uuid, PlayerQuickBuyCache.quickSlots);
                if (items == null) return;
                if (items.isEmpty()) return;
                for (Map.Entry<Integer, String> entry : items.entrySet()) {
                    if (entry.getValue().isEmpty()) continue;
                    if (entry.getValue().equals(" ")) continue;
                    QuickBuyElement e = new QuickBuyElement(entry.getValue(), entry.getKey());
                    if (e.isLoaded()) {
                        cache.addQuickElement(e);
                    }
                }
            }
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
    }
}

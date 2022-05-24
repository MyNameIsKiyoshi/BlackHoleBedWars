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
import com.yumahisai.blholebw.halloween.shop.PumpkinContent;
import com.yumahisai.blholebw.shop.ShopManager;
import com.yumahisai.blholebw.shop.main.ShopCategory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class HalloweenSpecial {

    private static HalloweenSpecial INSTANCE;

    private HalloweenSpecial() {
        BedWars.plugin.getLogger().info(ChatColor.AQUA + "Loaded Halloween Special <3");
        // pumpkin hats
        Bukkit.getPluginManager().registerEvents(new HalloweenListener(), BedWars.plugin);

        // pumpkin in shop
        ShopCategory blockCategory = ShopManager.getShop().getCategoryList().stream().filter(category -> category.getName().equals("blocks-category")).findFirst().orElse(null);
        if (blockCategory != null) {
            PumpkinContent content = new PumpkinContent(blockCategory);
            if (content.isLoaded()) {
                blockCategory.getCategoryContentList().add(content);
            }
        }
    }

    /**
     * Initialize Halloween Special.
     */
    public static void init() {
        if (INSTANCE == null) {
            if (!checkAvailabilityDate()) return;
            INSTANCE = new HalloweenSpecial();
        }
    }

    protected static boolean checkAvailabilityDate() {
        // check date
        ZoneId zone = ZoneId.of("Europe/Rome");
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(zone).toLocalDate();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        // allowed between October 21 and November 1
        return (month == 10 && day > 21 || month == 11 && day < 2);
    }

    public static HalloweenSpecial getINSTANCE() {
        return INSTANCE;
    }
}

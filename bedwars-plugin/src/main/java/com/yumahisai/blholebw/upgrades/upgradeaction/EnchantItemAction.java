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

package com.yumahisai.blholebw.upgrades.upgradeaction;

import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.upgrades.UpgradeAction;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class EnchantItemAction implements UpgradeAction {

    private final Enchantment enchantment;
    private final int amplifier;
    private final ApplyType type;

    public EnchantItemAction(Enchantment enchantment, int amplifier, ApplyType type){
        this.enchantment = enchantment;
        this.amplifier = amplifier;
        this.type = type;
    }

    @Override
    public void onBuy(Player player, ITeam bwt) {
        if (type == ApplyType.ARMOR){
            bwt.addArmorEnchantment(enchantment, amplifier);
        } else if (type == ApplyType.SWORD){
            bwt.addSwordEnchantment(enchantment, amplifier);
        } else if (type == ApplyType.BOW){
            bwt.addBowEnchantment(enchantment, amplifier);
        }
    }

    public enum ApplyType {
        SWORD,
        ARMOR,
        BOW,
    }
}

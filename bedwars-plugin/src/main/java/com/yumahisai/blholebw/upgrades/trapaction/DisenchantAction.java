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

package com.yumahisai.blholebw.upgrades.trapaction;

import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.upgrades.TrapAction;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DisenchantAction implements TrapAction {

    private Enchantment enchantment;
    private ApplyType type;

    public DisenchantAction(Enchantment enchantment, DisenchantAction.ApplyType type){
        this.enchantment = enchantment;
        this.type = type;
    }

    @Override
    public String getName() {
        return "disenchant-item";
    }

    @Override
    public void onTrigger(@NotNull Player player, ITeam playerTeam, ITeam targetTeam) {
        if (type == ApplyType.SWORD){
            for (ItemStack i : player.getInventory()){
                if (BedWars.nms.isSword(i)){
                    i.removeEnchantment(enchantment);
                }
                player.updateInventory();
            }
        } else if (type == ApplyType.ARMOR){
            for (ItemStack i : player.getInventory()){
                if (BedWars.nms.isArmor(i)){
                    i.removeEnchantment(enchantment);
                }
                player.updateInventory();
            }
            for (ItemStack i : player.getInventory().getArmorContents()){
                if (BedWars.nms.isArmor(i)){
                    i.removeEnchantment(enchantment);
                }
                player.updateInventory();
            }
        } else if (type == ApplyType.BOW){
            for (ItemStack i : player.getInventory()){
                if (BedWars.nms.isBow(i)){
                    i.removeEnchantment(enchantment);
                }
                player.updateInventory();
            }
        }
    }

    public enum ApplyType {
        SWORD,
        ARMOR,
        BOW,
    }
}

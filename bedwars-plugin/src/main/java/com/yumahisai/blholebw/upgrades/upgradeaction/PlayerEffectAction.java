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
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class PlayerEffectAction implements UpgradeAction {

    private final PotionEffectType potionEffectType;
    private final int amplifier;
    private int duration;
    private final ApplyType type;

    public PlayerEffectAction(PotionEffectType potionEffectType, int amplifier, int duration, ApplyType type){
        this.potionEffectType = potionEffectType;
        this.amplifier = amplifier;
        this.type = type;
        this.duration = duration;
        if (duration < 0 ) this.duration *= -1;
        /*if (type == ApplyType.ENEMY_BASE_ENTER && duration <= 0){
            this.duration = 20;
        }*/
        if (duration == 0){
            this.duration = Integer.MAX_VALUE;
        } else {
            this.duration *= 20;
        }
    }


    @Override
    public void onBuy(Player player, ITeam bwt) {
        if (type == ApplyType.BASE){
            bwt.addBaseEffect(potionEffectType, amplifier, duration);
        } else if (type == ApplyType.TEAM){
            bwt.addTeamEffect(potionEffectType, amplifier, duration);
        }/* else if (type == ApplyType.ENEMY_BASE_ENTER){
            bwt.addEnemyBaseEnterEffect(potionEffectType, amplifier, duration);
        }*/
    }

    public enum ApplyType {
        TEAM, BASE
    }
}

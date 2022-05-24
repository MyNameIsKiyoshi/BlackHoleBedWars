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

package com.yumahisai.blholebw.api.levels;

import com.yumahisai.blholebw.api.events.player.PlayerXpGainEvent;
import org.bukkit.entity.Player;

public interface Level {

    /**
     * @return current player level formatted as string.
     */
    String getLevel(Player p);


    /**
     * @return current player level as number.
     */
    int getPlayerLevel(Player p);

    /**
     * Get required xp as string.
     * 2000 - 2k
     *
     * @return required xp for next level.
     */
    String getRequiredXpFormatted(Player p);

    /**
     * @return current progress bar.
     */
    String getProgressBar(Player p);

    /**
     * @return current xp.
     */
    int getCurrentXp(Player p);

    /**
     * @return current xp formatted.
     */
    String getCurrentXpFormatted(Player p);

    /**
     * @return required xp
     */
    int getRequiredXp(Player p);

    /**
     * Add some xp to target player.
     */
    void addXp(Player player, int xp, PlayerXpGainEvent.XpSource source);

    /**
     * Set player xp.
     */
    void setXp(Player player, int currentXp);

    /**
     * Set player level.
     */
    void setLevel(Player player, int level);
}

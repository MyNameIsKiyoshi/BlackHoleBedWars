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

package com.yumahisai.blholebw.api.arena.generator;

import org.bukkit.entity.Player;

public interface IGenHolo {

    /**
     * Set timer hologram display text.
     */
    void setTimerName(String timer);

    /**
     * Set tier hologram display text.
     */
    void setTierName(String tier);

    /**
     * Get language iso associated with this hologram.
     */
    String getIso();

    /**
     * Hide hologram for target player if is using a different language.
     * Add your generator to an arena and it will automatically call this when required.
     *
     * @param p    The player who should not see this hologram.
     * @param lang Player's language.
     */
    void updateForPlayer(Player p, String lang);

    /**
     * Hide hologram for all players using a different language than this hologram.
     */
    void updateForAll();

    /**
     * This must be called when disabling the generator {@link IGenerator#disable()}
     */
    void destroy();
}

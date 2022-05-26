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

import java.util.UUID;

public interface DatabaseAdapter {

    /**
     * Check if database is remove, like MySQL
     *
     * @since 0.1
     */
    boolean isRemote();

    /**
     * Close database connection
     *
     * @since 0.1
     */
    void close();

    /**
     * Create arrow trails table if not exists
     *
     * @since 0.1
     */
    void setupTrailsTable();


    /**
     * Create arrow spells table if not exists
     *
     * @since 0.1
     */
    void setupSpellsTable();

    /**
     * Create player particles table if not exists
     *
     * @since 0.1
     */
    void setupParticlesTable();

    /**
     * Create boosters table if not exists
     *
     * @since 0.1
     */
    void setupBoostersTable();

    /**
     * Get selected trails type
     *
     * @since 0.1
     */
    TrailType getSelectedTrails(UUID uuid);

    /**
     * Get selected spells type
     *
     * @since 0.1
     */
    SpellType getSelectedSpells(UUID uuid);

    /**
     * Get selected particles type
     *
     * @since 0.1
     */
    ParticleType getSelectedParticles(UUID uuid);

    /**
     * Get selected booster type
     *
     * @since 0.1
     */
    BoosterType getSelectedBooster(UUID uuid);

    /**
     * Set selected booster for a player
     * @since 0.1
     */
    void setSelectedBooster(UUID uuid, BoosterType boosterType);

    /**
     * Set selected particles for a player
     * @since 0.1
     */
    void setSelectedParticles(UUID uuid, ParticleType particlesType);

    /**
     * Set selected spells for a player
     * @since 0.1
     */
    void setSelectedSpells(UUID uuid, SpellType spellType);

    /**
     * Set selected trails for a player
     * @since 0.1
     */
    void setSelectedTrails(UUID uuid, TrailType trailType);

}

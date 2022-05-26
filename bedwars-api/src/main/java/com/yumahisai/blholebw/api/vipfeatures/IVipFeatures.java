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

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IVipFeatures {

    /**
     * Register your mini game integration.
     */
    void registerMiniGame(MiniGame miniGame) throws MiniGameAlreadyRegistered;

    /**
     * Give a player the Perks item. It will keep settings from VipFeatures.
     */
    void givePlayerItemStack(Player player);

    /**
     * Get particles util.
     */
    ParticlesUtil getParticlesUtil();

    /**
     * Get version util.
     * Some useful methods that can help you provide support for multiple server versions.
     */
    VersionUtil getVersionUtil();

    /**
     * Player spells util.
     */
    SpellsUtil getSpellsUtil();

    /**
     * Player trails util.
     */
    TrailsUtil getTrailsUtil();

    /**
     * Points boosters util.
     */
    BoostersUtil getBoostersUtil();

    /**
     * Get plugin instance;
     */
    Plugin getVipFeatures();

    /**
     * Change your database management system.
     */
    void setDatabaseAdapter(DatabaseAdapter newAdapter);

    /**
     * Get database adapter.
     */
    DatabaseAdapter getDatabaseAdapter();

    interface ParticlesUtil {
        /**
         * Get a player active particles.
         *
         * @param player target player.
         * @return {@link ParticleType#NONE} if the player do not have active particle effects.
         */
        ParticleType getPlayerParticles(Player player);

        /**
         * Enable/ disable player particles.
         * This will check player permissions as well.
         * This won't update database settings.
         * <p>
         * Use {@link ParticleType#NONE} to remove player particles.
         *
         * @param player       target player.
         * @param particleType particle.
         */
        void togglePlayerParticles(Player player, ParticleType particleType);
    }

    interface SpellsUtil {
        /**
         * Get a player active spell effect.
         *
         * @param player target player.
         * @return {@link SpellType#NONE} if the player do not have any active spell.
         */
        SpellType getPlayerSpells(Player player);

        /**
         * Enable/ disable player spells.
         * This will check player permissions as well.
         * This won't update database settings.
         * <p>
         * Use {@link SpellType#NONE} to remove player spells.
         *
         * @param player target player.
         * @param type   type.
         */
        void togglePlayerSpells(Player player, SpellType type);

        /**
         * Checking this metadata on zombies can help you handle their targets
         * so it will not attack teammates from owner's team.
         *
         * @return the player name who spawned the zombie.
         */
        default String getZombieOwnerMetaKey() {
            return "avf-owner";
        }
    }

    /**
     * Some useful methods that can help you provide support for multiple server versions.
     */
    interface VersionUtil {
        /**
         * Select the right string for the current server version.
         *
         * @return v18 if the server version is 1.8.8.
         * If the server version is between 1.9 and 1.12 included returns v12 and v13 for 1.13 or newer.
         */
        String getForCurrentVersion(String v18, String v12, String v13);
    }

    interface TrailsUtil {
        /**
         * Get a player active trails effect.
         *
         * @param player target player.
         * @return {@link TrailType#NONE} if the player do not have any active spell.
         */
        TrailType getPlayerTrails(Player player);

        /**
         * Enable/ disable player trails.
         * This will check player permissions as well.
         * This won't update database settings.
         * <p>
         * Use {@link TrailType#NONE} to remove player trails.
         *
         * @param player target player.
         * @param type   type.
         */
        void togglePlayerTrails(Player player, TrailType type);
    }

    interface BoostersUtil {
        /**
         * Get a player active booster.
         *
         * @param player target player.
         * @return {@link TrailType#NONE} if the player does not have an active booster.
         */
        BoosterType getPlayerBooster(Player player);

        /**
         * Enable/ disable player boosters.
         * This will check player permissions as well.
         * This won't update database settings.
         * <p>
         * Use {@link BoosterType#NONE} to remove player booster.
         *
         * @param player target player.
         * @param type   type.
         */
        void togglePlayerBooster(Player player, BoosterType type);
    }
}

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

package com.yumahisai.blholebw.api;

import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.shop.IContentTier;
import com.yumahisai.blholebw.api.command.ParentCommand;
import com.yumahisai.blholebw.api.configuration.ConfigManager;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.levels.Level;
import com.yumahisai.blholebw.api.party.Party;
import com.yumahisai.blholebw.api.server.ISetupSession;
import com.yumahisai.blholebw.api.server.RestoreAdapter;
import com.yumahisai.blholebw.api.server.ServerType;
import com.yumahisai.blholebw.api.server.VersionSupport;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.UUID;

@SuppressWarnings("unused")
public interface BedWars {

    /**
     * Get stats utils.
     */
    IStats getStatsUtil();

    interface IStats {
        /**
         * Get player first play date.
         * You get data from the local cache.
         */
        @SuppressWarnings("unused")
        Timestamp getPlayerFirstPlay(UUID p);

        /**
         * Get player last play date.
         * You get data from the local cache.
         */
        @SuppressWarnings("unused")
        Timestamp getPlayerLastPlay(UUID p);

        /**
         * Get player total wins.
         * You get data from the local cache.
         */
        int getPlayerWins(UUID p);

        /**
         * Get player regular kills.
         * You get data from the local cache.
         */
        int getPlayerKills(UUID p);

        /**
         * Get player total kills.
         * Regular kills + final kills.
         * You get data from the local cache.
         */
        int getPlayerTotalKills(UUID p);

        /**
         * Get player total final kills.
         * You get data from the local cache.
         */
        int getPlayerFinalKills(UUID p);

        /**
         * Get player total looses.
         * You get data from the local cache.
         */
        int getPlayerLoses(UUID p);

        /**
         * Get player total deaths.
         * You get data from the local cache.
         */
        int getPlayerDeaths(UUID p);

        /**
         * Get player total final deaths.
         * You get data from the local cache.
         */
        int getPlayerFinalDeaths(UUID p);

        /**
         * Get player beds destroyed.
         * You get data from the local cache.
         */
        int getPlayerBedsDestroyed(UUID p);

        /**
         * Get player games played.
         * You get data from the local cache.
         */
        int getPlayerGamesPlayed(UUID p);
    }


    /**
     * Get afk system methods. It will only work if the game is started.
     */
    AFKUtil getAFKUtil();

    interface AFKUtil {
        /**
         * Check if a player is AFK.
         */
        boolean isPlayerAFK(Player player);

        /**
         * Set a player afk.
         */
        void setPlayerAFK(Player player, boolean value);

        /**
         * Get the seconds since the player is AFK
         */
        @SuppressWarnings("unused")
        int getPlayerTimeAFK(Player player);
    }

    ArenaUtil getArenaUtil();

    interface ArenaUtil {

        /**
         * Check if an arena can be auto-scaled.
         *
         * @return always true if auto-scale is disabled.
         */
        boolean canAutoScale(String arenaName);

        /**
         * Add a custom arena to the enable queue.
         */
        @SuppressWarnings("unused")
        void addToEnableQueue(IArena a);

        /**
         * Remove an arena from the enable queue.
         */
        void removeFromEnableQueue(IArena a);

        /**
         * Check if a player is playing.
         */
        boolean isPlaying(Player p);

        /**
         * Check if a player is spectating.
         */
        boolean isSpectating(Player p);

        /**
         * Load an arena.
         * Add it to the enable queue.
         *
         * @param sender If you want to send feedback. Use null otherwise.
         */
        void loadArena(String worldName, Player sender);

        /**
         * Set how many games to the next serve restart.
         * This is used only if {@link ServerType#BUNGEE}
         */
        void setGamesBeforeRestart(int games);

        /**
         * Get how many games till the next restart.
         * This is used only if {@link ServerType#BUNGEE}
         */
        int getGamesBeforeRestart();

        /**
         * Get an arena by a player. Spectator or Player.
         *
         * @param player Target player
         * @return The arena where the player is in. Can be NULL.
         */
        IArena getArenaByPlayer(Player player);

        /**
         * Set an arena by player if the player is in this arena.
         */
        void setArenaByPlayer(Player p, IArena arena);

        /**
         * Remove
         */
        @SuppressWarnings("unused")
        void removeArenaByPlayer(Player p, IArena a);

        /**
         * Get an arena by world name
         *
         * @param worldName World name
         */
        IArena getArenaByName(String worldName);

        IArena getArenaByIdentifier(String worldName);

        void setArenaByName(IArena arena);

        /**
         * Remove
         */
        @SuppressWarnings("unused")
        void removeArenaByName(String worldName);

        LinkedList<IArena> getArenas();

        /**
         * Check if a player has vip join.
         */
        boolean vipJoin(Player p);

        /**
         * Get players count for a group
         */
        int getPlayers(String group);

        /**
         * Add a player to the most filled arena.
         * Check if is the party owner first.
         *
         * @return true if joined.
         */
        @SuppressWarnings("unused")
        boolean joinRandomArena(Player p);

        /**
         * Add a player to the most filled arena from a group.
         *
         * @return true if added.
         */
        @SuppressWarnings("unused")
        boolean joinRandomFromGroup(Player p, String group);

        /**
         * Arena enable queue.
         */
        LinkedList<IArena> getEnableQueue();

        /**
         * This will give the lobby items to the player.
         * Not used in serverType BUNGEE.
         * This will clear the inventory first.
         */
        void sendLobbyCommandItems(Player p);
    }

    Configs getConfigs();

    interface Configs {
        /**
         * Get plugin main configuration.
         */
        ConfigManager getMainConfig();

        /**
         * Get signs configuration.
         */
        @SuppressWarnings("unused")
        ConfigManager getSignsConfig();

        /**
         * Get generators configuration.
         */
        @SuppressWarnings("unused")
        ConfigManager getGeneratorsConfig();

        /**
         * Get shop configuration.
         */
        ConfigManager getShopConfig();

        /**
         * Get upgrades configuration.
         */
        @SuppressWarnings("unused")
        ConfigManager getUpgradesConfig();
    }

    /**
     * Get shop util.
     */
    ShopUtil getShopUtil();

    interface ShopUtil {

        /**
         * Get player's money amount
         */
        int calculateMoney(Player player, Material currency);

        /**
         * Get currency as material
         *
         * @return {@link Material#AIR} if is vault.
         */
        Material getCurrency(String currency);

        ChatColor getCurrencyColor(Material currency);

        /**
         * Cet currency path
         */
        @SuppressWarnings("unused")
        String getCurrencyMsgPath(IContentTier contentTier);

        /**
         * Get roman number for given int.
         */
        String getRomanNumber(int n);

        /**
         * Take money from player on buy
         */
        void takeMoney(Player player, Material currency, int amount);
    }

    /**
     * Get shop util.
     */
    @SuppressWarnings("unused")
    TeamUpgradesUtil getTeamUpgradesUtil();

    interface TeamUpgradesUtil {
        /**
         * Check if a player is watching the team upgrades menu.
         */
        @SuppressWarnings("unused")
        boolean isWatchingGUI(Player player);

        /**
         * Set a player watching the team upgrades menu.
         */
        void setWatchingGUI(Player player);

        /**
         * Remove from upgrades GUI.
         */
        void removeWatchingUpgrades(UUID uuid);

        /**
         * Get total tiers in team upgrades to be bought in the given arena.
         * Sum of tiers in team upgrades.
         * @param arena arena
         * @return count
         */
        int getTotalUpgradeTiers(IArena arena);
    }

    /**
     * Get levels methods.
     */
    Level getLevelsUtil();

    /**
     * Get party util.
     */
    Party getPartyUtil();

    /**
     * Get active setup session.
     *
     * @param player player uuid.
     * @return null if no session was found.
     */
    ISetupSession getSetupSession(UUID player);

    /**
     * Check if a player is in setup session.
     */
    boolean isInSetupSession(UUID player);

    /**
     * Get server type.
     **/
    ServerType getServerType();

    /**
     * Get a player language iso code
     */
    @SuppressWarnings("unused")
    String getLangIso(Player p);


    /**
     * Get bedWars main command
     */
    ParentCommand getBedWarsCommand();

    /**
     * Get the restore adapter.
     */
    RestoreAdapter getRestoreAdapter();

    /**
     * Change the arena restore adapter.
     *
     * @param restoreAdapter your custom adapter.
     */
    void setRestoreAdapter(RestoreAdapter restoreAdapter) throws IllegalAccessError;

    /**
     * Change the party interface.
     * You man need to unregister /party command yourself.
     */
    void setPartyAdapter(Party partyAdapter);

    /**
     * Get nms operations.
     */
    VersionSupport getVersionSupport();

    /**
     * Get server default language.
     */
    Language getDefaultLang();

    /**
     * Get lobby world name.
     */
    String getLobbyWorld();

    String getForCurrentVersion(String v18, String v12, String v13);

    @SuppressWarnings("unused")
    void setLevelAdapter(Level level);

    boolean isAutoScale();

    /**
     * Get language by iso code.
     */
    @SuppressWarnings("unused")
    Language getLanguageByIso(String isoCode);

    /**
     * Get a player language.
     */
    Language getPlayerLanguage(Player player);

    /**
     * This is my standard location where to store addons configurations.
     * Make sure to create a folder in it with your addon name.
     */
    @SuppressWarnings("unused")
    File getAddonsPath();

    /**
     * Scoreboard options.
     */
    ScoreboardUtil getScoreboardUtil();

    boolean isShuttingDown();

    interface ScoreboardUtil {

        /**
         * Removes bed-wars sidebar for the given player.
         */
        void removePlayerScoreboard(Player player);

        /**
         * Restores user scoreboard based on plugin configuration.
         * @param delay 5 seconds delay. to be used on server join.
         */
        void givePlayerScoreboard(Player player, boolean delay);
    }
}

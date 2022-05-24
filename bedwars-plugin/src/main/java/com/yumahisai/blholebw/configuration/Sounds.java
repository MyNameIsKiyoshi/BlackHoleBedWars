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

package com.yumahisai.blholebw.configuration;


import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.arena.NextEvent;
import com.yumahisai.blholebw.api.configuration.ConfigManager;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

import static com.yumahisai.blholebw.api.configuration.ConfigPath.*;

public class Sounds {

    private static final ConfigManager sounds = new ConfigManager(BedWars.plugin, "sounds", BedWars.plugin.getDataFolder().getPath());

    /**
     * Load sounds configuration
     */
    private Sounds() {
    }

    public static void init() {
        YamlConfiguration yml = sounds.getYml();

        addDefSound("game-end", BedWars.getForCurrentVersion("AMBIENCE_THUNDER", "ENTITY_LIGHTNING_THUNDER", "ITEM_TRIDENT_THUNDER"));
        addDefSound("rejoin-denied", BedWars.getForCurrentVersion("VILLAGER_NO", "ENTITY_VILLAGER_NO", "ENTITY_VILLAGER_NO"));
        addDefSound("rejoin-allowed", BedWars.getForCurrentVersion("SLIME_WALK", "ENTITY_SLIME_JUMP", "ENTITY_SLIME_JUMP"));
        addDefSound("spectate-denied", BedWars.getForCurrentVersion("VILLAGER_NO", "ENTITY_VILLAGER_NO", "ENTITY_VILLAGER_NO"));
        addDefSound("spectate-allowed", BedWars.getForCurrentVersion("SLIME_WALK", "ENTITY_SLIME_JUMP", "ENTITY_SLIME_JUMP"));
        addDefSound("join-denied", BedWars.getForCurrentVersion("VILLAGER_NO", "ENTITY_VILLAGER_NO", "ENTITY_VILLAGER_NO"));
        addDefSound("join-allowed", BedWars.getForCurrentVersion("SLIME_WALK", "ENTITY_SLIME_JUMP", "ENTITY_SLIME_JUMP"));
        addDefSound("spectator-gui-click", BedWars.getForCurrentVersion("SLIME_WALK", "ENTITY_SLIME_JUMP", "ENTITY_SLIME_JUMP"));
        addDefSound(SOUNDS_COUNTDOWN_TICK, BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        addDefSound(SOUNDS_COUNTDOWN_TICK_X + "5", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        addDefSound(SOUNDS_COUNTDOWN_TICK_X + "4", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        addDefSound(SOUNDS_COUNTDOWN_TICK_X + "3", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        addDefSound(SOUNDS_COUNTDOWN_TICK_X + "2", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        addDefSound(SOUNDS_COUNTDOWN_TICK_X + "1", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        addDefSound(SOUND_GAME_START, BedWars.getForCurrentVersion("SLIME_ATTACK", "BLOCK_SLIME_FALL", "BLOCK_SLIME_BLOCK_FALL"));

        addDefSound(SOUNDS_KILL, BedWars.getForCurrentVersion("ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP", "ENTITY_EXPERIENCE_ORB_PICKUP"));

        addDefSound(SOUNDS_BED_DESTROY, BedWars.getForCurrentVersion("ENDERDRAGON_GROWL", "ENTITY_ENDERDRAGON_GROWL", "ENTITY_ENDER_DRAGON_GROWL"));
        addDefSound(SOUNDS_BED_DESTROY_OWN, BedWars.getForCurrentVersion("WITHER_DEATH", "ENTITY_WITHER_DEATH", "ENTITY_WITHER_DEATH"));
        addDefSound(SOUNDS_INSUFF_MONEY, BedWars.getForCurrentVersion("VILLAGER_NO", "ENTITY_VILLAGER_NO", "ENTITY_VILLAGER_NO"));
        addDefSound(SOUNDS_BOUGHT, BedWars.getForCurrentVersion("VILLAGER_YES", "ENTITY_VILLAGER_YES", "ENTITY_VILLAGER_YES"));

        addDefSound(NextEvent.BEDS_DESTROY.getSoundPath(), BedWars.getForCurrentVersion("ENDERDRAGON_GROWL", "ENTITY_ENDERDRAGON_GROWL", "ENTITY_ENDER_DRAGON_GROWL"));
        addDefSound(NextEvent.DIAMOND_GENERATOR_TIER_II.getSoundPath(), BedWars.getForCurrentVersion("LEVEL_UP", "ENTITY_PLAYER_LEVELUP", "ENTITY_PLAYER_LEVELUP"));
        addDefSound(NextEvent.DIAMOND_GENERATOR_TIER_III.getSoundPath(), BedWars.getForCurrentVersion("LEVEL_UP", "ENTITY_PLAYER_LEVELUP", "ENTITY_PLAYER_LEVELUP"));
        addDefSound(NextEvent.EMERALD_GENERATOR_TIER_II.getSoundPath(), BedWars.getForCurrentVersion("GHAST_MOAN", "ENTITY_GHAST_WARN", "ENTITY_GHAST_WARN"));
        addDefSound(NextEvent.EMERALD_GENERATOR_TIER_III.getSoundPath(), BedWars.getForCurrentVersion("GHAST_MOAN", "ENTITY_GHAST_WARN", "ENTITY_GHAST_WARN"));
        addDefSound(NextEvent.ENDER_DRAGON.getSoundPath(), BedWars.getForCurrentVersion("ENDERDRAGON_WINGS", "ENTITY_ENDERDRAGON_FLAP", "ENTITY_ENDER_DRAGON_FLAP"));

        addDefSound("player-re-spawn", BedWars.getForCurrentVersion("SLIME_ATTACK", "BLOCK_SLIME_FALL", "BLOCK_SLIME_BLOCK_FALL"));
        addDefSound("arena-selector-open", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        addDefSound("stats-gui-open", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        addDefSound("trap-sound", BedWars.getForCurrentVersion("ENDERMAN_TELEPORT", "ENDERMAN_TELEPORT", "ENTITY_ENDERMAN_TELEPORT"));
        addDefSound("shop-auto-equip", BedWars.getForCurrentVersion("HORSE_ARMOR", "ITEM_ARMOR_EQUIP_GENERIC", "ITEM_ARMOR_EQUIP_GENERIC"));
        addDefSound("egg-bridge-block", BedWars.getForCurrentVersion("CHICKEN_EGG_POP", "ENTITY_CHICKEN_EGG", "ENTITY_CHICKEN_EGG"));
        addDefSound("ender-pearl-landed", BedWars.getForCurrentVersion("ENDERMAN_TELEPORT", "ENTITY_ENDERMEN_TELEPORT", "ENTITY_ENDERMAN_TELEPORT"));
        yml.options().copyDefaults(true);

        // remove old paths
        yml.set("bought", null);
        yml.set("insufficient-money", null);
        yml.set("player-kill", null);
        yml.set("countdown", null);
        sounds.save();
    }

    private static Sound getSound(String path) {
        try {
            return Sound.valueOf(sounds.getString(path + ".sound"));
        } catch (Exception ex) {
            return Sound.valueOf(BedWars.getForCurrentVersion("AMBIENCE_THUNDER", "ENTITY_LIGHTNING_THUNDER", "ITEM_TRIDENT_THUNDER"));
        }
    }

    public static void playSound(String path, List<Player> players) {
        if(path.equalsIgnoreCase("none")) return;
        final Sound sound = getSound(path);
        int volume = getSounds().getInt(path + ".volume");
        int pitch = getSounds().getInt(path + ".pitch");
        if (sound != null) {
            players.forEach(p -> p.playSound(p.getLocation(), sound, volume, pitch));
        }
    }

    /**
     * @return true if sound is valid and it was played.
     */
    public static boolean playSound(Sound sound, List<Player> players) {
        if (sound == null) return false;
        players.forEach(p -> p.playSound(p.getLocation(), sound, 1f, 1f));
        return true;
    }

    public static void playSound(String path, Player player) {
        final Sound sound = getSound(path);
        float volume = (float) getSounds().getYml().getDouble(path + ".volume");
        float pitch = (float) getSounds().getYml().getDouble(path + ".pitch");
        if (sound != null) player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static ConfigManager getSounds() {
        return sounds;
    }

    private static void addDefSound(String path, String value) {
        // convert old paths
        if (getSounds().getYml().get(path) != null && getSounds().getYml().get(path + ".volume") == null) {
            String temp = getSounds().getYml().getString(path);
            getSounds().getYml().set(path, null);
            getSounds().getYml().set(path + ".sound", temp);
        }
        getSounds().getYml().addDefault(path + ".sound", value);
        getSounds().getYml().addDefault(path + ".volume", 1);
        getSounds().getYml().addDefault(path + ".pitch", 1);
    }
}

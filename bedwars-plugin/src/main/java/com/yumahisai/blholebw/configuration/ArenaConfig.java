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

import com.yumahisai.blholebw.api.configuration.ConfigManager;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class ArenaConfig extends ConfigManager {

    public ArenaConfig(Plugin plugin, String name, String dir) {
        super(plugin, name, dir);

        YamlConfiguration yml = getYml();
        yml.options().header(plugin.getName() + " arena configuration file.\n");
        yml.addDefault("group", "Default");
        yml.addDefault(ConfigPath.ARENA_DISPLAY_NAME, "");
        yml.addDefault("minPlayers", 2);
        yml.addDefault("maxInTeam", 1);
        yml.addDefault("allowSpectate", true);
        yml.addDefault(ConfigPath.ARENA_SPAWN_PROTECTION, 5);
        yml.addDefault(ConfigPath.ARENA_SHOP_PROTECTION, 1);
        yml.addDefault(ConfigPath.ARENA_UPGRADES_PROTECTION, 1);
        yml.addDefault(ConfigPath.ARENA_GENERATOR_PROTECTION, 1);
        yml.addDefault(ConfigPath.ARENA_ISLAND_RADIUS, 17);
        yml.addDefault("worldBorder", 300);
        yml.addDefault(ConfigPath.ARENA_Y_LEVEL_KILL, -1);
        //yml.addDefault("disableGeneratorsOnOrphanIslands", false);
        yml.addDefault(ConfigPath.ARENA_CONFIGURATION_MAX_BUILD_Y, 180);
        yml.addDefault(ConfigPath.ARENA_DISABLE_GENERATOR_FOR_EMPTY_TEAMS, false);
        yml.addDefault(ConfigPath.ARENA_DISABLE_NPCS_FOR_EMPTY_TEAMS, true);
        yml.addDefault(ConfigPath.ARENA_NORMAL_DEATH_DROPS, false);
        yml.addDefault(ConfigPath.ARENA_USE_BED_HOLO, true);
        yml.addDefault(ConfigPath.ARENA_ALLOW_MAP_BREAK, false);
        ArrayList<String> rules = new ArrayList<>();
        rules.add("doDaylightCycle:false");
        rules.add("announceAdvancements:false");
        rules.add("doInsomnia:false");
        rules.add("doImmediateRespawn:true");
        rules.add("doWeatherCycle:false");
        rules.add("doFireTick:false");
        yml.addDefault(ConfigPath.ARENA_GAME_RULES, rules);
        yml.options().copyDefaults(true);
        save();

        //convert old configuration
        if (yml.get("spawnProtection") != null) {
            set(ConfigPath.ARENA_SPAWN_PROTECTION, yml.getInt("spawnProtection"));
            set("spawnProtection", null);
        }
        if (yml.get("shopProtection") != null) {
            set(ConfigPath.ARENA_SHOP_PROTECTION, yml.getInt("shopProtection"));
            set("shopProtection", null);
        }
        if (yml.get("upgradesProtection") != null) {
            set(ConfigPath.ARENA_UPGRADES_PROTECTION, yml.getInt("upgradesProtection"));
            set("upgradesProtection", null);
        }
        if (yml.get("islandRadius") != null) {
            set(ConfigPath.ARENA_ISLAND_RADIUS, yml.getInt("islandRadius"));
        }
        if (yml.get("voidKill") != null){
            set("voidKill", null);
        }
        set(ConfigPath.GENERAL_CONFIGURATION_ENABLE_GEN_SPLIT, null);
    }
}

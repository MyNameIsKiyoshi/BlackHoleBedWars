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
import com.yumahisai.blholebw.api.configuration.ConfigManager;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class SignsConfig extends ConfigManager {

    public SignsConfig(Plugin plugin, String name, String dir) {
        super(plugin, name, dir);
        YamlConfiguration yml = getYml();
        yml.addDefault("format", Arrays.asList("&a[arena]", "", "&2[on]&9/&2[max]", "[status]"));
        yml.addDefault(ConfigPath.SIGNS_STATUS_BLOCK_WAITING_MATERIAL, BedWars.getForCurrentVersion("STAINED_CLAY", "STAINED_CLAY", "GREEN_CONCRETE"));
        yml.addDefault(ConfigPath.SIGNS_STATUS_BLOCK_WAITING_DATA, 5);
        yml.addDefault(ConfigPath.SIGNS_STATUS_BLOCK_STARTING_MATERIAL, BedWars.getForCurrentVersion("STAINED_CLAY", "STAINED_CLAY", "YELLOW_CONCRETE"));
        yml.addDefault(ConfigPath.SIGNS_STATUS_BLOCK_STARTING_DATA, 14);
        yml.addDefault(ConfigPath.SIGNS_STATUS_BLOCK_PLAYING_MATERIAL, BedWars.getForCurrentVersion("STAINED_CLAY", "STAINED_CLAY", "RED_CONCRETE"));
        yml.addDefault(ConfigPath.SIGNS_STATUS_BLOCK_PLAYING_DATA, 4);
        yml.addDefault(ConfigPath.SIGNS_STATUS_BLOCK_RESTARTING_MATERIAL, BedWars.getForCurrentVersion("STAINED_CLAY", "STAINED_CLAY", "RED_CONCRETE"));
        yml.addDefault(ConfigPath.SIGNS_STATUS_BLOCK_RESTARTING_DATA, 4);
        yml.options().copyDefaults(true);
        save();
        if (yml.getStringList("format").size() < 4) {
            set("format", yml.getStringList("format").subList(0, 3));
        }
    }
}

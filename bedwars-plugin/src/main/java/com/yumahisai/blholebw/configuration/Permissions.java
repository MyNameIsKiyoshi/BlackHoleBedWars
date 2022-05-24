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
import org.bukkit.entity.Player;

public class Permissions {
    public static final String PERMISSION_FORCESTART = BedWars.mainCmd+".forcestart";
    public static final String PERMISSION_ALL = BedWars.mainCmd+".*";
    public static final String PERMISSION_COMMAND_BYPASS = BedWars.mainCmd+".cmd.bypass";
    public static final String PERMISSION_SHOUT_COMMAND = BedWars.mainCmd+".shout";

    public static final String PERMISSION_SETUP_ARENA = BedWars.mainCmd+".setup";
    public static final String PERMISSION_ARENA_GROUP = BedWars.mainCmd+".groups";
    public static final String PERMISSION_BUILD = BedWars.mainCmd+".build";
    public static final String PERMISSION_CLONE = BedWars.mainCmd+".clone";
    public static final String PERMISSION_DEL_ARENA = BedWars.mainCmd+".delete";
    public static final String PERMISSION_ARENA_ENABLE = BedWars.mainCmd+".enableRotation";
    public static final String PERMISSION_ARENA_DISABLE = BedWars.mainCmd+".disable";
    public static final String PERMISSION_NPC = BedWars.mainCmd+".npc";
    public static final String PERMISSION_RELOAD = BedWars.mainCmd+".reload";
    public static final String PERMISSION_REJOIN = BedWars.mainCmd+".rejoin";
    public static final String PERMISSION_LEVEL = BedWars.mainCmd+".level";
    public static final String PERMISSION_CHAT_COLOR = BedWars.mainCmd+".chatcolor";
    public static final String PERMISSION_VIP = BedWars.mainCmd+".vip";

    /**
     * Check if player has one of the given permissions.
     */
    public static boolean hasPermission(Player player, String... permissions){
        for (String permission : permissions){
            if (player.hasPermission(permission)){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if player has all given permissions.
     */
    public static boolean hasPermissions(Player player, String... permissions){
        for (String permission : permissions){
            if (!player.hasPermission(permission)){
                return false;
            }
        }
        return true;
    }
}

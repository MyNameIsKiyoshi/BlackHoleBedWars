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

package com.yumahisai.blackholeteamselector;

import com.andrei1058.spigotutils.SpigotUpdater;
import com.yumahisai.blackholeteamselector.api.TeamSelector;
import com.yumahisai.blackholeteamselector.api.TeamSelectorAPI;
import com.yumahisai.blackholeteamselector.configuration.Config;
import com.yumahisai.blackholeteamselector.configuration.Messages;
import com.yumahisai.blackholeteamselector.listeners.ArenaListener;
import com.yumahisai.blackholeteamselector.listeners.InventoryListener;
import com.yumahisai.blackholeteamselector.listeners.PlayerInteractListener;
import com.yumahisai.blackholeteamselector.listeners.SelectorGuiUpdateListener;
import com.yumahisai.blholebw.api.BedWars;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static BedWars bw;
    public static Main plugin;

    /**
     * Register listeners
     */
    private static void registerListeners(Listener... listeners) {
        PluginManager pm = Bukkit.getPluginManager();
        for (Listener l : listeners) {
            pm.registerEvents(l, plugin);
        }
    }

    @Override
    public void onEnable() {
        plugin = this;

        //Disable if pl not found
        if (Bukkit.getPluginManager().getPlugin("s") == null) {
            getLogger().severe("BlackHoleBedWars was not found. Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        bw = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();

        if (bw == null) {
            getLogger().severe("Can't hook into BlackHoleBedWars.");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        Bukkit.getServicesManager().register(TeamSelectorAPI.class, new TeamSelector(), this, ServicePriority.Normal);

        getLogger().info("Hook into BlackHoleBedWars!");

        //Create configuration
        Config.addDefaultConfig();

        //Save default messages
        Messages.setupMessages();

        //Register listeners
        registerListeners(new ArenaListener(), new InventoryListener(), new PlayerInteractListener(), new SelectorGuiUpdateListener());

        // bStats
        Metrics metrics = new Metrics(this, 15299);
        metrics.addCustomChart(new Metrics.SimplePie("selector_slot", () -> String.valueOf(Config.config.getInt(Config.SELECTOR_SLOT))));
        metrics.addCustomChart(new Metrics.SimplePie("allot_team_change", () -> String.valueOf(Config.config.getBoolean(Config.ALLOW_TEAM_CHANGE))));
        metrics.addCustomChart(new Metrics.SimplePie("balance_teams", () -> String.valueOf(Config.config.getBoolean(Config.BALANCE_TEAMS))));
        metrics.addCustomChart(new Metrics.SimplePie("balance_teams", () -> String.valueOf(Config.config.getBoolean(Config.BALANCE_TEAMS))));

        new SpigotUpdater(this, 102200, true).checkUpdate();
    }
}

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

package com.yumahisai.blholebw.api.updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

@SuppressWarnings("WeakerAccess")
public class SpigotUpdater {

    private int resourceID;
    private Plugin plugin;
    private String currentVersion, newVersion = null;
    private boolean updateAvailable = false;
    private boolean updateMessage;

    /**
     * Create a new updater instance.
     * Use {@link #checkUpdate()} to check for updates.
     *
     * @param plugin        - plugin instance.
     * @param resourceID    - spigot resource id.
     * @param updateMessage - send update available message in console and to OPs when they join.
     */
    public SpigotUpdater(Plugin plugin, int resourceID, boolean updateMessage) {
        this.resourceID = resourceID;
        this.plugin = plugin;
        currentVersion = plugin.getDescription().getVersion();
        this.updateMessage = updateMessage;
    }

    /**
     * Check for updates (async).
     * Will not work if used when the plugin is disabled.
     */
    public void checkUpdate() {
        if (!plugin.isEnabled()) {
            plugin.getLogger().log(Level.WARNING, "Could not check for updates. #checkUpdate cannot be used when the plugin is disabled.");
            return;
        }

        Bukkit.getPluginManager().registerEvents(new JoinListener(), plugin);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (plugin.getDescription().getVersion().contains("{project.version}") || plugin.getDescription().getVersion().contains("{gitVer}") || plugin.getDescription().getVersion().contains("{git}")){
                plugin.getLogger().log(Level.INFO, "It looks lie you are using a development build. Update checking disabled!");
                return;
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceID).openConnection();
                conn.setDoOutput(true);

                newVersion = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
                if (!newVersion.equalsIgnoreCase(currentVersion)) {
                    String[] v = newVersion.split("\\.");
                    String[] o = currentVersion.split("\\.");
                    for (int i = 0; i < v.length; i++) {
                        if (i < o.length) {
                            try {
                                if (Integer.parseInt(v[i]) > Integer.parseInt(o[i])) {
                                    updateAvailable = true;
                                } else if (Integer.parseInt(v[i]) != Integer.parseInt(o[i])) {
                                    break;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        } else {
                            updateAvailable = true;
                        }
                    }

                    if (updateAvailable && updateMessage) {
                        plugin.getLogger().log(Level.WARNING, "                                    ");
                        plugin.getLogger().log(Level.WARNING, "------------------------------------");
                        plugin.getLogger().log(Level.WARNING, "           YumaUpdater");
                        plugin.getLogger().log(Level.WARNING, "There is a new version available!");
                        plugin.getLogger().log(Level.WARNING, "New version: " + newVersion);
                        plugin.getLogger().log(Level.WARNING, "You are running: " + currentVersion);
                        plugin.getLogger().log(Level.WARNING, " ");
                        plugin.getLogger().log(Level.WARNING, "https://www.spigotmc.org/resources/" + resourceID);
                        plugin.getLogger().log(Level.WARNING, "------------------------------------");
                        plugin.getLogger().log(Level.WARNING, "                                    ");
                    }
                }
                conn.disconnect();
            } catch (IOException e) {
                plugin.getLogger().log(Level.INFO, "Could not check for updates.");
            }
        });
    }

    /**
     * Check if {@link #checkUpdate()} found a new update.
     * This may not work immediately after running {@link #checkUpdate()} since it runs async.
     *
     * @return true if {@link #checkUpdate()} found a new update.
     */
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    /**
     * Get the new version after using {@link #checkUpdate()}.
     *
     * @return null if no new update was found. Version String otherwise.
     */
    @Nullable
    public String getNewVersion() {
        return newVersion;
    }

    /**
     * Get the plugin's current version.
     *
     * @return plugin current version.
     */
    public String getCurrentVersion() {
        return currentVersion;
    }

    private class JoinListener implements Listener {

        /**
         * Send update message.
         */
        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerJoin(PlayerJoinEvent e) {
            if (e.getPlayer().isOp() && isUpdateAvailable() && updateMessage) {
                e.getPlayer().sendMessage("");
                e.getPlayer().sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + plugin.getName() + ChatColor.GRAY + "]" + ChatColor.WHITE + " there is a new version available: " + ChatColor.GREEN + getNewVersion());
            }
        }
    }
}

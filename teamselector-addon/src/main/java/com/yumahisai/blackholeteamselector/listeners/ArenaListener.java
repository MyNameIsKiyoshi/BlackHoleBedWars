package com.yumahisai.blackholeteamselector.listeners;

import com.yumahisai.blackholeteamselector.Main;
import com.yumahisai.blackholeteamselector.teamselector.ArenaPreferences;
import com.yumahisai.blackholeteamselector.teamselector.TeamManager;
import com.yumahisai.blackholeteamselector.teamselector.TeamSelectorAssigner;
import com.yumahisai.blackholeteamselector.teamselector.TeamSelectorGUI;
import com.yumahisai.blholebw.api.arena.GameState;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.events.gameplay.GameStateChangeEvent;
import com.yumahisai.blholebw.api.events.player.PlayerJoinArenaEvent;
import com.yumahisai.blholebw.api.events.player.PlayerLeaveArenaEvent;
import com.yumahisai.blholebw.api.events.server.ArenaEnableEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ArenaListener implements Listener {

    @EventHandler
    public void onBwArenaJoin(PlayerJoinArenaEvent e) {
        if (e.isCancelled()) return;
        if (e.isSpectator()) return;
        if (e.getArena() == null) return;
        if (e.getArena().getStatus() == GameState.waiting || e.getArena().getStatus() == GameState.starting) {
            Bukkit.getScheduler().runTaskLater(Main.plugin, () -> {
                if (e.getArena().isPlayer(e.getPlayer()) || e.getArena().getStatus() != GameState.playing) {
                    TeamSelectorGUI.giveItem(e.getPlayer(), null);
                }
            }, 30L);
        }
    }

    @EventHandler
    //Remove player from team
    public void onBwArenaLeave(PlayerLeaveArenaEvent e) {
        IArena a = e.getArena();
        if (a.getStatus() == GameState.waiting || a.getStatus() == GameState.starting) {
            TeamManager.getInstance().onQuit(a, e.getPlayer());
        }
    }

    @EventHandler
    public void onStatusChange(GameStateChangeEvent e) {
        if (e.getNewState() == GameState.starting) {

            ArenaPreferences pref = TeamManager.getInstance().getArena(e.getArena());
            if (pref == null) return;

            // do not start with a single team
            int size = e.getArena().getPlayers().size();
            int teams = pref.getTeamsCount();
            int members = pref.getMembersCount();
            if (size - members <= 0 && teams == 1) {
                e.getArena().setStatus(GameState.waiting);
            }
        }
        if (e.getNewState() == GameState.playing || e.getNewState() == GameState.restarting) {
            TeamManager.getInstance().clearArenaCache(e.getArena());
        }
    }

    @EventHandler
    public void onArenaLoad(ArenaEnableEvent event) {
        event.getArena().setTeamAssigner(new TeamSelectorAssigner());
    }
}

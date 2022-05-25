package com.yumahisai.blackholeteamselector.listeners;

import com.yumahisai.blackholeteamselector.api.events.TeamSelectorAbortEvent;
import com.yumahisai.blackholeteamselector.api.events.TeamSelectorChooseEvent;
import com.yumahisai.blackholeteamselector.teamselector.TeamSelectorGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SelectorGuiUpdateListener implements Listener {

    @EventHandler
    public void onTeamJoin(TeamSelectorChooseEvent e) {
        if (e.isCancelled()) return;
        TeamSelectorGUI.updateGUIs();
    }

    @EventHandler
    public void onAbort(TeamSelectorAbortEvent e) {
        TeamSelectorGUI.updateGUIs();
    }
}

package com.yumahisai.blackholeteamselector.api;

import com.yumahisai.blholebw.api.arena.team.ITeam;
import org.bukkit.entity.Player;

public interface TeamSelectorAPI {

    /**
     * Get player's selected team
     */
    ITeam getSelectedTeam(Player player);


    /**
     * Get api version
     */
    int getApiVersion();
}

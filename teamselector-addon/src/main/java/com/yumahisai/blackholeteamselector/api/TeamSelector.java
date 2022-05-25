package com.yumahisai.blackholeteamselector.api;


import com.yumahisai.blackholeteamselector.Main;
import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import org.bukkit.entity.Player;

public class TeamSelector implements TeamSelectorAPI {

    @Override
    public ITeam getSelectedTeam(Player player) {
        IArena a = Main.bw.getArenaUtil().getArenaByPlayer(player);
        return a == null ? null : a.getTeam(player);
    }

    @Override
    public int getApiVersion() {
        return 2;
    }
}

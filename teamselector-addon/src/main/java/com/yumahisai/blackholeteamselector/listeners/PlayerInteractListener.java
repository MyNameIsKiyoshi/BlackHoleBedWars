package com.yumahisai.blackholeteamselector.listeners;

import com.yumahisai.blackholeteamselector.Main;
import com.yumahisai.blackholeteamselector.teamselector.TeamSelectorGUI;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) return;
        ItemStack i = Main.bw.getVersionSupport().getItemInHand(e.getPlayer());
        if (i == null) return;
        if (i.getType() == Material.AIR) return;
        if (!Main.bw.getVersionSupport().isCustomBedWarsItem(i)) return;
        if (Main.bw.getVersionSupport().getCustomData(i).equals(TeamSelectorGUI.TEAM_SELECTOR_IDENTIFIER)) {
            e.setCancelled(true);
            TeamSelectorGUI.openGUI(e.getPlayer(), false);
        }
    }
}

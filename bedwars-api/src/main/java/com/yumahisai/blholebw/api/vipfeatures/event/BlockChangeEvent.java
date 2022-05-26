package com.yumahisai.blholebw.api.vipfeatures.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BlockChangeEvent extends Event {

    private static HandlerList handlerList = new HandlerList();

    private Location location;
    private Material oldMaterial, newMaterial;
    private boolean cancelled = false;

    /**
     * Triggered when a block is modified by VipFeatures integrations.
     */
    public BlockChangeEvent(Location location, Material oldMaterial, Material newMaterial) {
        this.location = location;
        this.oldMaterial = oldMaterial;
        this.newMaterial = newMaterial;
    }

    /**
     * Get location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Get old material.
     */
    public Material getOldMaterial() {
        return oldMaterial;
    }

    /**
     * Get new material.
     */
    public Material getNewMaterial() {
        return newMaterial;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}

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

package com.yumahisai.blholebw.api.entity;

import com.yumahisai.blholebw.api.BedWars;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.events.player.PlayerKillEvent;
import com.yumahisai.blholebw.api.language.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class Despawnable {

    private LivingEntity e;
    private ITeam team;
    private int despawn = 250;
    private String namePath;
    private PlayerKillEvent.PlayerKillCause deathRegularCause, deathFinalCause;
    private UUID uuid;

    private static BedWars api;

    public Despawnable(LivingEntity e, ITeam team, int despawn, String namePath, PlayerKillEvent.PlayerKillCause deathFinalCause, PlayerKillEvent.PlayerKillCause deathRegularCause) {
        this.e = e;
        if (e == null) return;
        this.uuid = e.getUniqueId();
        this.team = team;
        this.deathFinalCause = deathFinalCause;
        this.deathRegularCause = deathRegularCause;
        if (despawn != 0) {
            this.despawn = despawn;
        }
        this.namePath = namePath;
        if (api == null) api = Bukkit.getServer().getServicesManager().getRegistration(BedWars.class).getProvider();
        api.getVersionSupport().getDespawnablesList().put(uuid, this);
        this.setName();
    }

    public void refresh() {
        if (e.isDead() || e == null || team == null || team.getArena() == null) {
            api.getVersionSupport().getDespawnablesList().remove(uuid);
            if (team.getArena() == null){
                e.damage(e.getHealth()+100);
            }
            return;
        }
        setName();
        despawn--;
        if (despawn == 0) {
            e.damage(e.getHealth()+100);
            api.getVersionSupport().getDespawnablesList().remove(e.getUniqueId());
        }
    }

    private void setName() {
        int percentuale = (int) ((e.getHealth() * 100) / e.getMaxHealth() / 10);
        String name = api.getDefaultLang().m(namePath).replace("{despawn}", String.valueOf(despawn)).replace("{health}",
                new String(new char[percentuale]).replace("\0", api.getDefaultLang()
                        .m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH)) + new String(new char[10 - percentuale]).replace("\0", "§7" + api.getDefaultLang()
                        .m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH)));
        if (team != null) {
            name = name.replace("{TeamColor}", team.getColor().chat().toString()).replace("{TeamName}", team.getDisplayName(api.getDefaultLang()));
        }
        e.setCustomName(name);
    }

    public LivingEntity getEntity() {
        return e;
    }

    public ITeam getTeam() {
        return team;
    }

    public int getDespawn() {
        return despawn;
    }

    public PlayerKillEvent.PlayerKillCause getDeathFinalCause() {
        return deathFinalCause;
    }

    public PlayerKillEvent.PlayerKillCause getDeathRegularCause() {
        return deathRegularCause;
    }

    public void destroy(){
        if (getEntity() != null){
            getEntity().damage(Integer.MAX_VALUE);
        }
        team = null;
        api.getVersionSupport().getDespawnablesList().remove(uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LivingEntity) return ((LivingEntity) obj).getUniqueId().equals(e.getUniqueId());
        return false;
    }
}

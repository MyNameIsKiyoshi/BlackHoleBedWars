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

package com.yumahisai.blholebw.support.version.v1_18_R1;

import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.support.version.common.VersionCommon;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.animal.EntityIronGolem;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_18_R1.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class IGolem extends EntityIronGolem {
    private ITeam team;

    private IGolem(EntityTypes<? extends EntityIronGolem> entitytypes, World world, ITeam bedWarsTeam) {
        super(entitytypes, world);
        this.team = bedWarsTeam;
    }

    public IGolem(EntityTypes entityTypes, World world) {
        super(entityTypes, world);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void u() {
        this.bR.a(1, new PathfinderGoalFloat(this));
        this.bR.a(2, new PathfinderGoalMeleeAttack(this, 1.5D, false));
        this.bS.a(3, new PathfinderGoalHurtByTarget(this));
        this.bR.a(4, new PathfinderGoalRandomStroll(this, 1D));
        this.bR.a(5, new PathfinderGoalRandomLookaround(this));
        this.bS.a(6, new PathfinderGoalNearestAttackableTarget(
                this, EntityHuman.class, 20, true, false,
                player -> !((EntityHuman)player).getBukkitEntity().isDead() &&
                        !team.wasMember(((EntityHuman)player).getBukkitEntity().getUniqueId()) &&
                        !team.getArena().isReSpawning(((EntityHuman)player).getBukkitEntity().getUniqueId())
                && !team.getArena().isSpectator(((EntityHuman)player).getBukkitEntity().getUniqueId()))
        );
        this.bS.a(7, new PathfinderGoalNearestAttackableTarget(
                this, IGolem.class, 20, true, false,
                golem -> ((IGolem)golem).getTeam() != team)
        );
        this.bS.a(8, new PathfinderGoalNearestAttackableTarget(
                this, Silverfish.class, 20, true, false,
                sf -> ((Silverfish)sf).getTeam() != team)
        );
    }

    public ITeam getTeam() {
        return team;
    }

    public static LivingEntity spawn(Location loc, ITeam bedWarsTeam, double speed, double health, int despawn) {
        WorldServer mcWorld = ((CraftWorld) Objects.requireNonNull(loc.getWorld())).getHandle();
        IGolem customEnt = new IGolem(EntityTypes.P, mcWorld, bedWarsTeam);
        customEnt.a(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) customEnt.getBukkitEntity()).setRemoveWhenFarAway(false);
        Objects.requireNonNull(customEnt.a(GenericAttributes.a)).a(health);
        Objects.requireNonNull(customEnt.a(GenericAttributes.d)).a(speed);

        if (!CraftEventFactory.doEntityAddEventCalling(mcWorld, customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM)){
            mcWorld.P.a(customEnt);
        }

        mcWorld.a(customEnt);
        customEnt.getBukkitEntity().setPersistent(true);
        customEnt.getBukkitEntity().setCustomNameVisible(true);
        customEnt.getBukkitEntity().setCustomName(Language.getDefaultLanguage().m(Messages.SHOP_UTILITY_NPC_IRON_GOLEM_NAME)
                .replace("{despawn}", String.valueOf(despawn)
                        .replace("{health}", StringUtils.repeat(Language.getDefaultLanguage().m(Messages.FORMATTING_DESPAWNABLE_UTILITY_NPC_HEALTH) + " ", 10))
                        .replace("{TeamColor}", bedWarsTeam.getColor().chat().toString())));
        return (LivingEntity) customEnt.getBukkitEntity();
    }

    @Override
    public void a(DamageSource damagesource) {
        super.a(damagesource);
        team = null;
        VersionCommon.api.getVersionSupport().getDespawnablesList().remove(this.getBukkitEntity().getUniqueId());
    }

    @Override
    public boolean d_() {
        return super.d_();
    }
}

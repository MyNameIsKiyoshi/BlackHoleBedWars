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

package com.yumahisai.blholebw.api.arena.team;

import com.yumahisai.blholebw.api.arena.IArena;
import com.yumahisai.blholebw.api.arena.generator.IGenerator;
import com.yumahisai.blholebw.api.language.Language;
import com.yumahisai.blholebw.api.upgrades.EnemyBaseEnterTrap;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface ITeam {

    /**
     * Get team color.
     */
    TeamColor getColor();

    /**
     * Get team name.
     */
    String getName();

    /**
     * Get team display name.
     *
     * @param language get the display name in target language.
     */
    String getDisplayName(Language language);

    /**
     * Check if is member.
     *
     * @param player target player.
     */
    boolean isMember(Player player);

    /**
     * Get the team arena.
     */
    IArena getArena();

    /**
     * Get alive team members.
     */
    List<Player> getMembers();

    /**
     * Restore lost default sword.
     *
     * @param p     target player.
     * @param value true to give the sword.
     */
    void defaultSword(Player p, boolean value);

    /**
     * Get bed location.
     */
    Location getBed();

    /**
     * Get list of team upgrades.
     * Upgrade identifier, tier.
     */
    ConcurrentHashMap<String, Integer> getTeamUpgradeTiers();

    /**
     * Get enchantments to be applied on bows.
     */
    List<TeamEnchant> getBowsEnchantments();

    /**
     * Get enchantments to be applied on swords.
     */
    List<TeamEnchant> getSwordsEnchantments();

    /**
     * Get enchantments to be applied on armors.
     */
    List<TeamEnchant> getArmorsEnchantments();


    /**
     * Get team current size.
     */
    int getSize();

    /**
     * Add a new member to the team.
     * Be careful! This will cache players on this team until the game is finished
     * so you can retrieve who played here with {@link #wasMember(UUID)}.
     *
     * @param players players to be added.
     */
    void addPlayers(Player... players);

    /**
     * Spawn a player for the first spawn.
     *
     * @param p target player.
     */
    void firstSpawn(Player p);

    /**
     * Spawn shopkeepers for target team (if enabled).
     */
    void spawnNPCs();

    /**
     * Rejoin a team.
     *
     * @param p target player.
     */
    void reJoin(Player p);

    /**
     * Rejoin a team.
     *
     * @param p target player.
     * @param respawnTime the time until the player should respawn.
     */
    void reJoin(Player p, int respawnTime);

    /**
     * Gives the start inventory
     *
     * @param p     target player.
     * @param clear true to clear inventory.
     */
    void sendDefaultInventory(Player p, boolean clear);

    /**
     * Respawn a member. This is after respawn countdown.
     *
     * @param p target player.
     */
    void respawnMember(Player p);

    /**
     * Equip a player with default armor.
     *
     * @param p target player.
     */
    void sendArmor(Player p);

    /**
     * Used when someone buys a new potion effect with apply == members
     *
     * @param pef      effect.
     * @param amp      amplifier.
     * @param duration duration.
     */
    void addTeamEffect(PotionEffectType pef, int amp, int duration);

    /**
     * Used when someone buys a new potion effect with apply == base
     *
     * @param pef      effect.
     * @param amp      amplifier.
     * @param duration duration.
     */
    void addBaseEffect(PotionEffectType pef, int amp, int duration);

    /**
     * Get list of effects that you gen when you enter the base.
     */
    List<PotionEffect> getBaseEffects();

    /**
     * Used when someone buys a bew enchantment with apply == bow.
     *
     * @param e enchant.
     * @param a amplifier.
     */
    void addBowEnchantment(Enchantment e, int a);

    /**
     * Used when someone buys a new enchantment with apply == sword.
     *
     * @param e enchant.
     * @param a amplifier.
     */
    void addSwordEnchantment(Enchantment e, int a);

    /**
     * Used when someone buys a new enchantment with apply == armor.
     *
     * @param e enchant.
     * @param a amplifier.
     */
    void addArmorEnchantment(Enchantment e, int a);

    /**
     * Check if target has played in this match.
     *
     * @param u player uuid.
     */
    boolean wasMember(UUID u);

    /**
     * Check if team's bed was destroyed.
     *
     * @return true if players are no longer able to respawn.
     */
    boolean isBedDestroyed();

    /**
     * Get team spawn location.
     *
     * @return spawn point.
     */
    Location getSpawn();

    /**
     * Get shop keeper location.
     *
     * @return shop point.
     */
    Location getShop();

    /**
     * Get team upgrades location.
     *
     * @return upgrades point.
     */
    Location getTeamUpgrades();

    /**
     * This will also disable team generators if true.
     *
     * @param bedDestroyed team members will no longer be able to respawn if you set this to true.
     */
    void setBedDestroyed(boolean bedDestroyed);

    /**
     * Get team iron generator.
     *
     * @return generator.
     */
    @Deprecated
    IGenerator getIronGenerator();

    /**
     * Get team gold generator.
     *
     * @return generator.
     */
    @Deprecated
    IGenerator getGoldGenerator();

    /**
     * Get team emerald generator.
     *
     * @return NULL if team does not have an emerald generator yet.
     */
    @Deprecated
    IGenerator getEmeraldGenerator();

    /**
     * Set a team emerald generator.
     *
     * @param emeraldGenerator generator.
     */
    @Deprecated
    void setEmeraldGenerator(IGenerator emeraldGenerator);

    /**
     * Get team generators.
     *
     * @return team generators.
     */
    List<IGenerator> getGenerators();

    /**
     * Get team dragons amount for the sudden death phase.
     */
    int getDragons();

    /**
     * Set a team dragons amount for the sudden death phase.
     */
    void setDragons(int amount);

    @Deprecated
    List<Player> getMembersCache();

    /**
     * Destroy team data when the arena restarts.
     * This must be called by BlackHoleBedWars only.
     */
    void destroyData();

    /**
     * Destroy bed hologram for player
     *
     * @param player target player.
     */
    @Deprecated
    void destroyBedHolo(Player player);

    /**
     * Get queued traps for a team.
     *
     * @return active traps list.
     */
    LinkedList<EnemyBaseEnterTrap> getActiveTraps();

    /**
     * Get the location where enemy items are dropped after you kill him.
     *
     * @return x, y, z.
     */
    Vector getKillDropsLocation();

    /**
     * Change the location where to drop items after you kill an enemy.
     *
     * @param location x,y,z.
     */
    void setKillDropsLocation(Vector location);
}

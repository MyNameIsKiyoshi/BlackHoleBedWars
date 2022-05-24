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

package com.yumahisai.blholebw.upgrades.upgradeaction;

import com.yumahisai.blholebw.api.arena.generator.GeneratorType;
import com.yumahisai.blholebw.api.arena.generator.IGenerator;
import com.yumahisai.blholebw.api.arena.team.ITeam;
import com.yumahisai.blholebw.api.upgrades.UpgradeAction;
import com.yumahisai.blholebw.arena.OreGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneratorEditAction implements UpgradeAction {

    private final int amount;
    private final int delay;
    private final int limit;
    private final ApplyType type;

    public GeneratorEditAction(ApplyType type, int amount, int delay, int limit) {
        this.type = type;
        this.amount = amount;
        this.delay = delay;
        this.limit = limit;
    }

    @Override
    public void onBuy(Player player, ITeam bwt) {
        List<IGenerator> generator = new ArrayList<>();
        if (type == ApplyType.IRON) {
            generator = bwt.getGenerators().stream().filter(g -> g.getType() == GeneratorType.IRON).collect(Collectors.toList());
        } else if (type == ApplyType.GOLD) {
            generator = bwt.getGenerators().stream().filter(g -> g.getType() == GeneratorType.GOLD).collect(Collectors.toList());
        } else if (type == ApplyType.EMERALD) {
            if (!bwt.getArena().getConfig().getArenaLocations("Team." + bwt.getName() + ".Emerald").isEmpty()) {
                for (Location l : bwt.getArena().getConfig().getArenaLocations("Team." + bwt.getName() + ".Emerald")) {
                    IGenerator gen = new OreGenerator(l, bwt.getArena(), GeneratorType.CUSTOM, bwt);
                    gen.setOre(new ItemStack(Material.EMERALD));
                    gen.setType(GeneratorType.EMERALD);
                    bwt.getGenerators().add(gen);
                    //bwt.getArena().getOreGenerators().add(gen);
                    generator.add(gen);
                }
            } else {
                IGenerator gen = new OreGenerator(bwt.getGenerators().get(0).getLocation().clone(), bwt.getArena(), GeneratorType.CUSTOM, bwt);
                gen.setOre(new ItemStack(Material.EMERALD));
                gen.setType(GeneratorType.EMERALD);
                bwt.getGenerators().add(gen);
                //bwt.getArena().getOreGenerators().add(gen);
                generator.add(gen);
            }
        }
        for (IGenerator g : generator){
            g.setAmount(amount);
            g.setDelay(delay);
            g.setSpawnLimit(limit);
        }
    }


    public enum ApplyType {
        IRON, GOLD, EMERALD
    }
}

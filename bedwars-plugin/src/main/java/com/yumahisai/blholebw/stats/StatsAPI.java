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

package com.yumahisai.blholebw.stats;

import com.yumahisai.blholebw.api.BedWars;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class StatsAPI implements BedWars.IStats {

    private static StatsAPI instance;

    public static StatsAPI getInstance() {
        if (instance == null) {
            instance = new StatsAPI();
        }
        return instance;
    }

    private StatsAPI() {
    }

    private PlayerStats getData(UUID uuid) {
        PlayerStats stats = com.yumahisai.blholebw.BedWars.getStatsManager().getUnsafe(uuid);
        if (stats == null) {
            stats = com.yumahisai.blholebw.BedWars.getRemoteDatabase().fetchStats(uuid);
        }
        return stats;
    }

    @Override
    public Timestamp getPlayerFirstPlay(UUID p) {
        Instant firstPlay = getData(p).getFirstPlay();
        if (firstPlay == null) {
            return null;
        }
        return Timestamp.from(firstPlay);
    }

    @Override
    public Timestamp getPlayerLastPlay(UUID p) {
        Instant lastPlay = getData(p).getLastPlay();
        if (lastPlay == null) {
            return null;
        }
        return Timestamp.from(lastPlay);
    }

    @Override
    public int getPlayerWins(UUID p) {
        return getData(p).getWins();
    }

    @Override
    public int getPlayerKills(UUID p) {
        return getData(p).getKills();
    }

    @Override
    public int getPlayerTotalKills(UUID p) {
        return getData(p).getTotalKills();
    }

    @Override
    public int getPlayerFinalKills(UUID p) {
        return getData(p).getFinalKills();
    }

    @Override
    public int getPlayerLoses(UUID p) {
        return getData(p).getLosses();
    }

    @Override
    public int getPlayerDeaths(UUID p) {
        return getData(p).getDeaths();
    }

    @Override
    public int getPlayerFinalDeaths(UUID p) {
        return getData(p).getFinalDeaths();
    }

    @Override
    public int getPlayerBedsDestroyed(UUID p) {
        return getData(p).getBedsDestroyed();
    }

    @Override
    public int getPlayerGamesPlayed(UUID p) {
        return getData(p).getGamesPlayed();
    }
}

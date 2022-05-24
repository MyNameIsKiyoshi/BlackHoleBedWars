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

package com.yumahisai.blholebw.support.party;

import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.yumahisai.blholebw.BedWars;
import com.yumahisai.blholebw.api.configuration.ConfigPath;
import com.yumahisai.blholebw.api.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PartiesAdapter implements Party {

    //Support for Parties by AlessioDP
    private final PartiesAPI api = com.alessiodp.parties.api.Parties.getApi();
    private static final int requiredRankToSelect = BedWars.config.getInt(ConfigPath.GENERAL_ALESSIODP_PARTIES_RANK);

    @Override
    public boolean hasParty(Player p) {
        PartyPlayer pp = api.getPartyPlayer(p.getUniqueId());
        return pp != null && pp.isInParty();
    }

    @Override
    public int partySize(Player p) {
        if (hasParty(p)) {
            PartyPlayer partyPlayer = api.getPartyPlayer(p.getUniqueId());
            if (partyPlayer != null) {
                if (partyPlayer.getPartyId() != null) {
                    com.alessiodp.parties.api.interfaces.Party party = api.getParty(partyPlayer.getPartyId());
                    if (null != party) {
                        return party.getOnlineMembers().size();
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public boolean isOwner(Player p) {
        PartyPlayer pp = api.getPartyPlayer(p.getUniqueId());
        if (pp == null || pp.getPartyId() == null) {
            return false;
        }
        return pp.getRank() >= requiredRankToSelect;
    }

    @Override
    public List<Player> getMembers(Player p) {
        ArrayList<Player> players = new ArrayList<>();
        if (hasParty(p)) {
            PartyPlayer pp = api.getPartyPlayer(p.getUniqueId());
            if (null != pp) {
                if (pp.getPartyId() != null) {
                    com.alessiodp.parties.api.interfaces.Party party = api.getParty(pp.getPartyId());
                    for (PartyPlayer member : party.getOnlineMembers()) {
                        players.add(Bukkit.getPlayer(member.getPlayerUUID()));
                    }
                }
            }
        }
        return players;
    }

    @Override
    public void createParty(Player owner, Player... members) {
        //party creation handled on bungee side
        if (!api.isBungeeCordEnabled()) {
            boolean created = api.createParty(null, api.getPartyPlayer(owner.getUniqueId()));
            if (created) {
                com.alessiodp.parties.api.interfaces.Party party = api.getParty(owner.getUniqueId());
                if (null != party) {
                    for (Player player1 : members) {
                        PartyPlayer partyPlayer = api.getPartyPlayer(player1.getUniqueId());
                        if (null != partyPlayer) {
                            party.addMember(partyPlayer);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addMember(Player owner, Player member) {
        //party operations handled on bungee server side
        if (!api.isBungeeCordEnabled()) {
            PartyPlayer partyPlayer = api.getPartyPlayer(owner.getUniqueId());
            if (null != partyPlayer) {
                if (null != partyPlayer.getPartyId()) {
                    com.alessiodp.parties.api.interfaces.Party party = api.getParty(partyPlayer.getPartyId());
                    if (null != party) {
                        PartyPlayer partyMember = api.getPartyPlayer(member.getUniqueId());
                        if (null != partyMember) {
                            party.addMember(partyMember);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removeFromParty(Player member) {
        PartyPlayer partyMember = api.getPartyPlayer(member.getUniqueId());
        if (null != partyMember) {
            if (null != partyMember.getPartyId()) {
                com.alessiodp.parties.api.interfaces.Party party = api.getParty(partyMember.getPartyId());
                if (null != party) {
                    party.removeMember(partyMember);
                }
            }
        }
    }

    @Override
    public void disband(Player owner) {
        PartyPlayer partyMember = api.getPartyPlayer(owner.getUniqueId());
        if (null != partyMember) {
            if (null != partyMember.getPartyId()) {
                com.alessiodp.parties.api.interfaces.Party party = api.getParty(partyMember.getPartyId());
                if (null != party) {
                    party.delete();
                }
            }
        }
    }

    @Override
    public boolean isMember(Player owner, Player check) {
        if (!hasParty(owner) || !hasParty(check)) {
            return false;
        } else {
            return api.areInTheSameParty(owner.getUniqueId(), check.getUniqueId());
        }
    }

    @Override
    public void removePlayer(Player owner, Player target) {
        PartyPlayer player = api.getPartyPlayer(owner.getUniqueId());
        if (null != player) {
            if (null != player.getPartyId()) {
                com.alessiodp.parties.api.interfaces.Party party = api.getParty(player.getPartyId());
                if (null != party) {
                    PartyPlayer targetPlayer = api.getPartyPlayer(target.getUniqueId());
                    if (null != targetPlayer) {
                        party.removeMember(targetPlayer);
                    }
                }
            }
        }
    }

    @Override
    public boolean isInternal() {
        return false;
    }
}

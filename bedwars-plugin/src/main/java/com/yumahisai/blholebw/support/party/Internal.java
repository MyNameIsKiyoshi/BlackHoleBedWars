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

import com.yumahisai.blholebw.api.language.Messages;
import com.yumahisai.blholebw.api.party.Party;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yumahisai.blholebw.api.language.Language.getMsg;

@SuppressWarnings("WeakerAccess")
public class Internal implements Party {
    private static List<Internal.Party> parites = new ArrayList<>();

    @Override
    public boolean hasParty(Player p) {
        for (Party party : getParites()) {
            if (party.members.contains(p)) return true;
        }
        return false;
    }

    @Override
    public int partySize(Player p) {
        for (Party party : getParites()) {
            if (party.members.contains(p)) {
                return party.members.size();
            }
        }
        return 0;
    }

    @Override
    public boolean isOwner(Player p) {
        for (Party party : getParites()) {
            if (party.members.contains(p)) {
                if (party.owner == p) return true;
            }
        }
        return false;
    }

    @Override
    public List<Player> getMembers(Player owner) {
        for (Party party : getParites()) {
            if (party.members.contains(owner)) {
                return party.members;
            }
        }
        return null;
    }

    @Override
    public void createParty(Player owner, Player... members) {
        Party p = new Party(owner);
        p.addMember(owner);
        for (Player mem : members) {
            p.addMember(mem);
        }
    }

    @Override
    public void addMember(Player owner, Player member) {
        if (owner == null || member == null) return;
        Internal.Party p = getParty(owner);
        if (p == null) return;
        p.addMember(member);
    }

    @Override
    public void removeFromParty(Player member) {
        for (Party p : new ArrayList<>(getParites())) {
            if (p.owner == member) {
                disband(member);
            } else if (p.members.contains(member)) {
                for (Player mem : p.members) {
                    mem.sendMessage(getMsg(mem, Messages.COMMAND_PARTY_LEAVE_SUCCESS).replace("{playername}", member.getName()).replace("{player}", member.getDisplayName()));
                }
                p.members.remove(member);
                if (p.members.isEmpty() || p.members.size() == 1) {
                    disband(p.owner);
                    parites.remove(p);
                }
                return;
            }
        }
    }

    @Override
    public void disband(Player owner) {
        Internal.Party pa = getParty(owner);
        if (pa == null) return;
        for (Player p : pa.members) {
            p.sendMessage(getMsg(p, Messages.COMMAND_PARTY_DISBAND_SUCCESS));
        }
        pa.members.clear();
        Internal.parites.remove(pa);
    }

    @Override
    public boolean isMember(Player owner, Player check) {
        for (Party p : parites) {
            if (p.owner == owner) {
                if (p.members.contains(check)) return true;
            }
        }
        return false;
    }

    @Override
    public void removePlayer(Player owner, Player target) {
        Party p = getParty(owner);
        if (p != null) {
            if (p.members.contains(target)) {
                for (Player mem : p.members) {
                    mem.sendMessage(getMsg(mem, Messages.COMMAND_PARTY_REMOVE_SUCCESS).replace("{player}", target.getName()));
                }
                p.members.remove(owner);
                if (p.members.isEmpty() || p.members.size() == 1) {
                    disband(p.owner);
                    parites.remove(p);
                }
            }
        }
    }

    @Override
    public boolean isInternal() {
        return true;
    }

    @Nullable
    private Party getParty(Player owner) {
        for (Party p : getParites()) {
            if (p.getOwner() == owner) return p;
        }
        return null;
    }

    @NotNull
    @Contract(pure = true)
    public static List<Party> getParites() {
        return Collections.unmodifiableList(parites);
    }

    class Party {

        private List<Player> members = new ArrayList<>();
        private Player owner;

        public Party(Player p) {
            owner = p;
            Internal.parites.add(this);
        }

        public Player getOwner() {
            return owner;
        }

        void addMember(Player p) {
            members.add(p);
        }
    }
}

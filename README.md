<p align="center">
     <a href="https://www.spigotmc.org/resources/blackholebedwarsapi.102200/"><img height="100em" src="https://i.imgur.com/zWbiT1n.png" alt="#"/></a>
</p>
<p align="center">
     <a href="#"><img height="100em" src="https://i.imgur.com/TthlxxC.png" alt="#"/></a>
</p>


#### Player Level
Get the player level. FORMATTED.
Placeholder: %blholebw_player_level%

#### Player Level Raw
Get the player level NUMBER.
Can be used for tops.
Placeholder: %blholebw_player_level_raw%

#### Progress Bar (Level)
Get the player progress bar (formatted).
Placeholder: %blholebw_player_progress%

#### Player Xp (Formatted)
Get the player current xp.
Large numbers are shortened: 1000 -> 1k
Placeholder: %blholebw_player_xp_formatted%

#### Player Xp
Get the player current xp.
Placeholder: %blholebw_player_xp%

#### Required Xp for Rankup (Formatted)
Get the player required xp for rankup.
Large numbers are shortened: 1000 -> 1k
Placeholder: %blholebw_player_rerq_xp_formatted%

#### Required Xp for Rankup
Get the player required xp for rankup.
Placeholder: %blholebw_player_rerq_xp%

#### First Play
Get first play date using the player's language date-format.
Placeholder: %blholebw_stats_firstplay%

#### Last Play
Get last play date using the player's language date-format.
Placeholder: %blholebw_stats_lastplay%

#### Regular Kills
Get a player's regular kills count.
Placeholder: %blholebw_stats_kills%

#### Total Kills
Get a player's kills. (regular + final).
Placeholder: %blholebw_stats_total_kills%;

#### Total Wins
Get a player's total wins count.
Placeholder: %blholebw_stats_wins%

#### Final Kills
Get a player's total final kills count.
Placeholder: %blholebw_stats_finalkills%

#### Total Deaths
Get a player's total deaths count.
Placeholder: %blholebw_stats_deaths%

#### Total Losses
Get a player's total losses count.
Placeholder: %blholebw_stats_losses%

#### Final Deaths
Get a player's total final deaths count.
Placeholder: %blholebw_stats_finaldeaths%

#### Beds Destroyed
Get a player's total beds destroyed count.
Placeholder: %blholebw_stats_bedsdestroyed%

#### Games Played
Get a player's total games played count.
Placeholder: %blholebw_stats_gamesplayed%

#### Players In Game
Get current players (playing) count.
Placeholder: %blholebw_current_playing%

#### Arenas Count
Get total arenas count.
Placeholder: %blholebw_current_arenas%

#### Player Team
This returns the player's team if a he is a player. [SPECTATOR] if he is a spectator or an empty string if he is not in an arena. [SHOUT] if is a /shout or !message. 
Placeholder: %blholebw_player_team%

#### Arena Status
This returns arena display status based on the server's default language.
Will return "Restarting" if the arena is offline or if it does not exist.
Placeholder: %blholebw_arena_status_[arenaName]%

#### Arena Player Count
This returns arena player count. Replace [arenaName] with the arena name.
It also supports + operator: %blholebw_arena_count_arena1+arena2%
Placeholder: %blholebw_arena_count_[arenaName]%

#### Arena Group Count
Get the total players amount in a group. Replace [groupName] with its name.
It also supports + operator: %blholebw_group_count_group1+group2%
Placeholder: %blholebw_group_count_[groupName]%

#### Get Current Arena Group
Get the group of the arena you're playing on.
Placeholder: %blholebw_current_arena_group%

#### Get an Arena Group
Get the group of the given arena.
Placeholder: %blholebw_arena_group_[arena]%

#### Elapsed Time
Get game elapsed time.
Placeholder: %blholebw_elapsed_time%
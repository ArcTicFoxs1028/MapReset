/*
 * Copyright 2013 Maxim Salikhov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package li.maxsa.java.mapreset;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class WorldResetCommand implements CommandExecutor {

    public WorldResetCommand() {

    }

    @Override
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        TranslationConfig tc = new TranslationConfig();
        if (cs.hasPermission("mapreset.reset")) {
            cs.sendMessage(ChatColor.GREEN + tc.getTranslation("resetting"));
            Player[] players = Bukkit.getServer().getOnlinePlayers();
            for (Player pl : players) {
                if (pl.getBedSpawnLocation() != null) {
                    pl.teleport(pl.getBedSpawnLocation());
                } else {
                    Location loc = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
                    pl.teleport(loc);
                }
            }
            TempWorld temp = MapReset.getCurrentWorld();
            temp.unload();
            new TempWorld();
            cs.sendMessage(ChatColor.GREEN + tc.getTranslation("reset"));
            return true;
        } else {
            cs.sendMessage(ChatColor.RED + tc.getTranslation("perms"));
            return false;
        }
    }
}
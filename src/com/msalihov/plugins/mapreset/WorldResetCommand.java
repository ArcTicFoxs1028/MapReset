/**
 * This file is part of Map Reset.
 *
 * Map Reset is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map Reset is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Map Reset. If not, see <http://www.gnu.org/licenses/>.
 */
package com.msalihov.plugins.mapreset;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldResetCommand implements CommandExecutor {

    private MapReset m;

    public WorldResetCommand(MapReset mr) {
        m = mr;
    }

    @Override
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cs.hasPermission("mapreset.reset")) {
            cs.sendMessage(ChatColor.GREEN + "Resetting world... (This may take a long time, depending on your server's performance!)");
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
            new TempWorld(m);
            cs.sendMessage(ChatColor.GREEN + "Map reset successfully!");
            return true;
        } else {
            cs.sendMessage(ChatColor.RED + "You do not have the necessary permission!");
            return false;
        }
    }
}
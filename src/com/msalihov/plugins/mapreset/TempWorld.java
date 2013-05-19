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

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class TempWorld {

    private MapReset m;
    private int id;

    public TempWorld(MapReset mr) {
        m = mr;
        id = MapReset.nextWorldId();
        File source = new File(m.getConfig().getString("world-to-reset"));
        File target = new File("tempworld-" + id);
        try {
            FileUtils.copyFolder(source, target);
        } catch (IOException ex) {
            MapReset.log.severe("Could not copy world to be reset into a temporary folder!");
        }
        Bukkit.getServer().createWorld(new WorldCreator("tempworld-" + id));
        Player[] players = Bukkit.getServer().getOnlinePlayers();
        for (Player pl : players) {
            pl.teleport(Bukkit.getServer().getWorld("tempworld-" + id).getSpawnLocation());
        }
        MapReset.addWorld(id, this);
        MapReset.log.info("Loaded temporary world successfully!");
    }

    public void teleport(Player pl) {
        pl.teleport(Bukkit.getServer().getWorld("tempworld-" + id).getSpawnLocation());
    }

    public void unload() {
        Bukkit.getServer().unloadWorld("tempworld-" + id, true);
    }
}
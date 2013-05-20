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
    private TranslationConfig tc;
    private int id;
    
    public TempWorld(MapReset mr) {
        m = mr;
        id = MapReset.nextWorldId();
        tc = new TranslationConfig(mr);
        File source = new File(m.getConfig().getString("world-to-reset"));
        File target = new File("tempworld-" + id);
        try {
            FileUtils.copyFolder(source, target);
        } catch (IOException ex) {
            MapReset.log.severe(tc.getTranslation("temp-copy"));
        }
        Bukkit.getServer().createWorld(new WorldCreator("tempworld-" + id));
        Player[] players = Bukkit.getServer().getOnlinePlayers();
        for (Player pl : players) {
            pl.teleport(Bukkit.getServer().getWorld("tempworld-" + id).getSpawnLocation());
        }
        MapReset.addWorld(id, this);
        MapReset.log.info(tc.getTranslation("temp-load"));
    }
    
    public void teleport(Player pl) {
        pl.teleport(Bukkit.getServer().getWorld("tempworld-" + id).getSpawnLocation());
    }
    
    public void unload() {
        Bukkit.getServer().unloadWorld("tempworld-" + id, true);
    }
}
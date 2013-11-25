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
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

class TempWorld {

    private final int id;
    private final World world;

    public TempWorld() {
        id = MapReset.nextWorldId();
        TranslationConfig tc = new TranslationConfig();
        File source = new File(MapReset.get().getConfig().getString("world-to-reset"));
        File target = new File("tempworld-" + id);
        try {
            FileUtil.copyFolder(source, target);
        } catch (IOException ex) {
            MapReset.get().getLogger().severe(tc.getTranslation("temp-copy"));
        }
        WorldCreator wc = new WorldCreator("tempworld-" + id);
        world = wc.createWorld();
        Player[] players = Bukkit.getServer().getOnlinePlayers();
        for (Player pl : players) {
            pl.teleport(world.getSpawnLocation());
        }
        MapReset.addWorld(id, this);
        MapReset.get().getLogger().info(tc.getTranslation("temp-load"));
    }

    public void teleport(Player pl) {
        pl.teleport(world.getSpawnLocation());
    }

    public void unload() {
        Bukkit.getServer().unloadWorld("tempworld-" + id, true);
    }
}
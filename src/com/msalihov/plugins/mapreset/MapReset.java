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
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MapReset extends JavaPlugin {
    
    private static HashMap<Integer, TempWorld> tempworlds = new HashMap<>();
    private TranslationConfig tc;
    private File resetworld;
    public static final Logger log = Bukkit.getLogger();
    
    @Override
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void onEnable() {
        FileUtils.clean();
        saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        if (getConfig().getBoolean("reset-no-people")) {
            Bukkit.getServer().getPluginManager().registerEvents(new QuitListener(), this);
        }
        getCommand("resetworld").setExecutor(new WorldResetCommand(this));
        tc = new TranslationConfig(this);
        if (getConfig().getBoolean("reload-translations")) {
            tc.reloadConfig();
        } else {
            tc.saveDefault();
        }
        resetworld = new File(getConfig().getString("world-to-reset"));
        if (!resetworld.exists()) {
            log.severe(tc.getTranslation("world"));
        }
        new TempWorld(this);
    }
    
    @Override
    public void onDisable() {
        FileUtils.clean();
    }
    
    public static int nextWorldId() {
        return tempworlds.size() + 1;
    }
    
    public static void addWorld(Integer id, TempWorld world) {
        tempworlds.put(id, world);
    }
    
    public static TempWorld getCurrentWorld() {
        return tempworlds.get(tempworlds.size());
    }
}
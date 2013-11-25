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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author msalihov (Maxim Salikhov)
 */
public class MapReset extends JavaPlugin {

    private static MapReset instance;
    private static Map<Integer, TempWorld> worlds;

    @Override
    public void onEnable() {
        FileUtil.clean();
        saveDefaultConfig();
        instance = this;
        worlds = new HashMap<Integer, TempWorld>();
        registerListeners();
        registerCommands();
        TranslationConfig tc = new TranslationConfig();
        if (getConfig().getBoolean("reload-translations")) {
            tc.reloadConfig();
        } else {
            tc.saveDefault();
        }
        File resetworld = new File(getConfig().getString("world-to-reset"));
        if (!resetworld.exists() || getConfig().getString("world-to-reset").equals(Bukkit.getWorlds().get(0).getName
                ())) {
            getLogger().severe("###################################################################");
            getLogger().severe("###################################################################");
            getLogger().severe(tc.getTranslation("world"));
            getLogger().severe("###################################################################");
            getLogger().severe("###################################################################");
            Bukkit.getServer().shutdown();
            return;
        }
        new TempWorld();
    }

    @Override
    public void onDisable() {
        FileUtil.clean();
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new JoinListener(), instance);
        pm.registerEvents(new QuitListener(), instance);
    }

    public void registerCommands() {
        getCommand("resetworld").setExecutor(new WorldResetCommand());
    }

    public static MapReset get() {
        return instance;
    }

    public static Integer nextWorldId() {
        return worlds.size() + 1;
    }

    public static void addWorld(Integer id, TempWorld world) {
        worlds.put(id, world);
    }

    public static TempWorld getCurrentWorld() {
        return worlds.get(worlds.size());
    }

}

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

/**
 * @author msalihov (Maxim Salikhov)
 */

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;

class TranslationConfig {

    private FileConfiguration conf = null;
    private File config = null;

    public String getTranslation(String path) {
        String str = getConfig().getString(path + "." + MapReset.get().getConfig().getString("translation"));
        if (str != null) {
            return str;
        } else {
            String str2 = getConfig().getString(path + ".en");
            if (str2 != null) {
                return str2;
            } else {
                return "\"" + path + "\" translation missing";
            }
        }
    }

    public void reloadConfig() {
        if (config == null) {
            config = new File(MapReset.get().getDataFolder(), "translations.yml");
        }
        conf = YamlConfiguration.loadConfiguration(config);
        InputStream defConfigStream = MapReset.get().getResource("translations.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            conf.setDefaults(defConfig);
            MapReset.get().saveResource("translations.yml", true);
        }
    }

    public void saveDefault() {
        if (config == null || conf == null) {
            config = new File(MapReset.get().getDataFolder(), "translations.yml");
        }
        if (!config.exists()) {
            MapReset.get().saveResource("translations.yml", false);
        }
    }

    FileConfiguration getConfig() {
        if (conf == null) {
            reloadConfig();
        }
        return conf;
    }
}
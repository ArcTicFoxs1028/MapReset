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
import java.io.InputStream;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TranslationConfig {

    private FileConfiguration conf = null;
    private File config = null;
    private MapReset m;

    public TranslationConfig(MapReset mr) {
        m = mr;
    }

    public String getTranslation(String path) {
        String str = getConfig().getString(path + "." + m.getConfig().getString("translation"));
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
            config = new File(m.getDataFolder(), "translations.yml");
        }
        conf = YamlConfiguration.loadConfiguration(config);
        InputStream defConfigStream = m.getResource("translations.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            conf.setDefaults(defConfig);
            m.saveResource("translations.yml", true);
        }
    }

    public void saveDefault() {
        if (config == null || conf == null) {
            config = new File(m.getDataFolder(), "translations.yml");
        }
        if (!config.exists()) {
            m.saveResource("translations.yml", false);
        }
    }

    public FileConfiguration getConfig() {
        if (conf == null) {
            reloadConfig();
        }
        return conf;
    }
}
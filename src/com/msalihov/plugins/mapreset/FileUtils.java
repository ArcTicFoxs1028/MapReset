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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FileUtils {

    private MapReset m;

    public FileUtils(MapReset mr) {
        m = mr;
    }

    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    public static void clean() {
        File dir = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath());
        String[] folders = dir.list();
        for (String folder : folders) {
            if (folder.contains("tempworld-")) {
                Player[] players = Bukkit.getServer().getOnlinePlayers();
                for (Player pl : players) {
                    if (pl.getBedSpawnLocation() != null) {
                        pl.teleport(pl.getBedSpawnLocation());
                    } else {
                        Location loc = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
                        pl.teleport(loc);
                    }
                }
                Bukkit.getServer().unloadWorld(folder, true);
                File folderfile = new File(folder);
                deleteDirectory(folderfile);
            }
        }
    }

    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            String files[] = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);
            }
        } else {
            OutputStream out;
            try (InputStream in = new FileInputStream(src)) {
                out = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
            out.close();
        }
    }
}
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
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class FileUtil {

    private static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
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
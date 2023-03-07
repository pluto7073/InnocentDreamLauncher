package io.innocentdream.launcher.version;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.innocentdream.launcher.LauncherApplication;
import io.innocentdream.launcher.OS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class VersionManager {

    private static final String VERSIONS = "https://innocent-dream.web.app/cdn/versions/versions.json";
    private static boolean LOADED = false;
    private static String currentVersion;
    private static final ArrayList<Version> versions = new ArrayList<>();
    public static final Gson GSON = new Gson();

    public static boolean isLoaded() {
        return LOADED;
    }

    public static void loadVersions() {
        if (!LauncherApplication.CONNECTED) {
            LOADED = true;
            loadLocalVersions();
            return;
        }
        try {
            InputStream stream = new URL(VERSIONS).openStream();
            JsonObject object = GSON.fromJson(GSON.newJsonReader(new InputStreamReader(stream)), TypeToken.get(JsonObject.class));
            for (String key : object.keySet()) {
                if (key.equals("current")) {
                    currentVersion = object.get("current").getAsString();
                } else {
                    versions.add(new Version(key, object.get(key).getAsString(), true, true));
                }
            }
            loadLocalVersions();
            LOADED = true;
        } catch (IOException e) {
            System.err.println("Could not load internet versions");
            e.printStackTrace();
            LOADED = false;
            LauncherApplication.CONNECTED = LauncherApplication.testConnection();
        }
    }

    private static void loadLocalVersions() {
        File versionsDir = new File(OS.getPath(), "versions");
        if (!versionsDir.exists()) {
            versionsDir.mkdirs();
            return;
        }
        File[] versionsFiles = versionsDir.listFiles();
        if (versionsFiles == null) {
            return;
        }
        for (File f : versionsFiles) {
            String name = f.getName();
            Version version = new Version(name, new File(f, name + ".json").getAbsolutePath(), false, false);
            if (!isVersionLoaded(version.getName())) {
                versions.add(version);
            }
        }
    }

    public static Set<Version> getVersions() {
        return Set.of(versions.toArray(new Version[0]));
    }

    public static Version getVersion(String name) {
        return versions.stream().filter(v -> v.getName().equals(name)).toList().get(0);
    }

    private static boolean isVersionLoaded(String name) {
        return versions.stream().filter(v -> v.getName().equals(name)).toList().size() > 0;
    }

    public static String latest() {
        return currentVersion;
    }

}

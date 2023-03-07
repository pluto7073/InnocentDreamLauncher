package io.innocentdream.launcher;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

public class VersionManager {

    private static final String VERSIONS = "https://innocent-dream.web.app/cdn/versions/versions.json";
    private static boolean LOADED = false;
    private static String currentVersion;
    private static final HashMap<String, String> versions = new HashMap<>();
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
                    versions.put(key, object.get(key).getAsString());
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
        //TODO load versions on computer
    }

    public static Set<String> getVersions() {
        return versions.keySet();
    }

    public static String getVersionJson(String version) {
        return versions.get(version);
    }

    public static String latest() {
        return currentVersion;
    }

}

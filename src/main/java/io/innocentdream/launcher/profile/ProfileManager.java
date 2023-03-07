package io.innocentdream.launcher.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.innocentdream.launcher.LauncherWindow;
import io.innocentdream.launcher.OS;
import io.innocentdream.launcher.version.VersionManager;

import javax.swing.*;
import java.io.*;
import java.util.*;

public final class ProfileManager {

    private static final ArrayList<Profile> PROFILES;
    public static final Profile DEFAULT_PROFILE;

    public static Profile CURRENT_PROFILE;

    private ProfileManager() { throw new UnsupportedOperationException(); }

    public static void initProfiles() {}

    public static void saveProfiles() {
        System.out.println("Saving profiles");
        File file = new File(OS.getPath(), "launcherProfiles.json");
        JsonObject data = new JsonObject();
        data.add("current", new JsonPrimitive(CURRENT_PROFILE.name()));
        JsonArray array = new JsonArray();
        for (Profile p : PROFILES) {
            if (p.name().equals("Latest Version")) continue;
            JsonObject object = new JsonObject();
            object.add("name", new JsonPrimitive(p.name()));
            object.add("version", new JsonPrimitive(p.version()));
            JsonArray args = new JsonArray();
            p.jvmArgs().forEach(arg -> args.add(new JsonPrimitive(arg)));
            object.add("jvmArgs", args);
            if (p.customRuntime().isPresent()) object.add("customRuntime", new JsonPrimitive(p.customRuntime().get()));
            if (p.customDir().isPresent()) object.add("runDir", new JsonPrimitive(p.customDir().get()));
            object.add("showLog", new JsonPrimitive(p.showLog()));
            object.add("keepOpen", new JsonPrimitive(p.keepOpen()));
            array.add(object);
        }
        data.add("profiles", array);
        try (FileWriter writer = new FileWriter(file)) {
            Streams.write(data, new JsonWriter(writer));
            writer.flush();
        } catch (IOException e) {
            System.err.println("Could not save profiles");
            e.printStackTrace();
            return;
        }
        System.out.println("Profiles Saved");
    }

    public static void deleteProfile(String name) {
        Profile p = findProfile(name);
        PROFILES.remove(p);
    }

    public static void newProfile(String name) {
        newProfile(DEFAULT_PROFILE.copy().setName(name));
    }

    public static void newProfile(Profile profile) {
        PROFILES.add(profile);
        saveProfiles();
    }

    public static Set<String> getProfiles() {
        ArrayList<String> profiles = new ArrayList<>();
        for (Profile p : PROFILES) {
            profiles.add(p.name());
        }
        return Set.of(profiles.toArray(new String[0]));
    }

    public static void editProfile(Profile originalProfile, Profile newProfile) {
        for (int i = 0; i < PROFILES.size(); i++) {
            Profile profile = PROFILES.get(i);
            if (profile.name().equals(originalProfile.name())) {
                PROFILES.set(i, newProfile);
                return;
            }
        }
        newProfile(newProfile);
    }

    public static Profile findProfile(String name) {
        for (Profile p : PROFILES) {
            if (name.equals(p.name())) {
                return p;
            }
        }
        System.out.println("Could not find profile: " + name + ", Returning Default Profile");
        return DEFAULT_PROFILE;
    }

    static {
        if (!VersionManager.isLoaded()) VersionManager.loadVersions();
        List<String> defaultArgs = new ArrayList<>(List.of(new String[]{"-XX:+UnlockExperimentalVMOptions", "-Xmx2G",
                "-XX:+UseG1GC", "-XX:G1NewSizePercent=20", "-XX:G1ReservePercent=20", "-XX:MaxGCPauseMillis=50", "-XX:G1HeapRegionSize=32M"}));
        DEFAULT_PROFILE = new Profile(VersionManager.latest(), "Latest Version", defaultArgs,
                Optional.empty(), Optional.empty(), false, 1);
        PROFILES = new ArrayList<>();
        PROFILES.add(DEFAULT_PROFILE);
        File file = new File(OS.getPath(), "launcherProfiles.json");
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                JsonObject data = Streams.parse(new JsonReader(new InputStreamReader(fis))).getAsJsonObject();
                JsonArray array = data.get("profiles").getAsJsonArray();
                for (JsonElement e : array) {
                    if (!(e instanceof JsonObject object)) {
                        continue;
                    }
                    String name = object.get("name").getAsString();
                    String version = object.get("version").getAsString();
                    JsonArray args = object.getAsJsonArray("jvmArgs");
                    List<String> argsList = new ArrayList<>();
                    if (args != null) {
                        for (JsonElement element : args) {
                            argsList.add(element.getAsString());
                        }
                    }
                    Optional<String> customRuntime = object.keySet().contains("customRuntime") ?
                            Optional.of(object.get("customRuntime").getAsString()) : Optional.empty();
                    Optional<String> customRunDir = object.keySet().contains("runDir") ?
                            Optional.of(object.get("runDir").getAsString()) : Optional.empty();
                    boolean showLog = object.get("showLog").getAsBoolean();
                    int keepOpen = object.get("keepOpen").getAsInt();
                    PROFILES.add(new Profile(version, name, argsList, customRuntime, customRunDir, showLog, keepOpen));
                }
                String current = data.get("current").getAsString();
                CURRENT_PROFILE = findProfile(current);
            } catch (IOException e) {
                System.err.println("Could not load profiles");
                e.printStackTrace();
            }
        } else {
            CURRENT_PROFILE = DEFAULT_PROFILE;
        }
    }

}

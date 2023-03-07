package io.innocentdream.launcher.version;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import io.innocentdream.launcher.OS;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class Version {

    private final String name;
    private final String file;
    private final boolean url;

    private boolean loaded;
    private String jarFile;
    private long jarFileSize;
    private ArrayList<JsonObject> jarLibraries;
    private ArrayList<String> runArgs;
    private String log4jXML;
    private String mainClass;

    public Version(String name, String file, boolean url, boolean loadLater) {
        this.name = name;
        this.file = file;
        this.url = url;
        if (!loadLater) {
            load();
        } else {
            loaded = false;
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> runArgs() {
        if (!this.loaded) {
            load();
        }
        return this.runArgs;
    }

    public String mainClass() {
        if (!this.loaded) {
            load();
        }
        return this.mainClass;
    }

    public String jarFile() {
        if (!this.loaded) {
            load();
        }
        return this.jarFile;
    }

    public long jarFileSize() {
        if (!this.loaded) {
            load();
        }
        return this.jarFileSize;
    }

    public ArrayList<JsonObject> jarLibraries() {
        if (!this.loaded) {
            load();
        }
        return this.jarLibraries;
    }

    public String log4jXML() {
        if (!this.loaded) {
            load();
        }
        return this.log4jXML;
    }

    private void load() {
        try {
            String file = this.file;
            if (url) {
                InputStream is = new URL(file).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder content = new StringBuilder(reader.readLine());
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    content.append('\n').append(line);
                }
                File versionDir = new File(OS.getPath(), "versions/" + name);
                versionDir.mkdirs();
                File versionJson = new File(versionDir, name + ".json");
                FileWriter writer = new FileWriter(versionJson);
                writer.write(content.toString());
                writer.flush();
                writer.close();
                file = versionJson.getAbsolutePath();
            }
            File info = new File(file);
            InputStream stream = new FileInputStream(info);
            JsonObject object = Streams.parse(new JsonReader(new InputStreamReader(stream))).getAsJsonObject();

            //Jar Info
            JsonObject jarData = object.getAsJsonObject("jar");
            if (jarData == null) {
                jarFile = "";
                jarFileSize = -1;
            } else {
                jarFile = jarData.keySet().contains("link") ? jarData.get("link").getAsString() : "";
                jarFileSize = jarData.keySet().contains("size") ? jarData.get("size").getAsLong() : -1;
            }

            //Log4j
            log4jXML = object.keySet().contains("log4jConfig") ? object.get("log4jConfig").getAsString() : "";

            //RunArgs
            runArgs = new ArrayList<>();
            JsonArray args = object.getAsJsonArray("runArgs");
            if (args != null) {
                for (JsonElement e : args) {
                    runArgs.add(e.getAsString());
                }
            }

            //Jar Libs
            jarLibraries = new ArrayList<>();
            JsonObject libsData = object.getAsJsonObject("libraries");
            JsonArray jars = libsData.getAsJsonArray("jars");
            for (JsonElement e : jars) {
                if (e instanceof JsonObject o) {
                    jarLibraries.add(o);
                }
            }

            //Main Class
            mainClass = object.get("mainClass").getAsString();

            loaded = true;
        } catch (IOException e) {
            System.err.println("Could not load version");
            e.printStackTrace();
            loaded = false;
        }
    }

}

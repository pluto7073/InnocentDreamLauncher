package io.innocentdream.launcher.launch;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.innocentdream.launcher.LauncherApplication;
import io.innocentdream.launcher.LauncherWindow;
import io.innocentdream.launcher.OS;
import io.innocentdream.launcher.profile.Profile;
import io.innocentdream.launcher.version.Version;
import io.innocentdream.launcher.version.VersionManager;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.jar.JarEntry;

public class Launcher {

    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss");

    private final Profile profile;
    private boolean initialized;

    public Launcher(Profile profile) {
        this.profile = profile;
        this.initialized = false;
    }

    public void initRunDir() {
        File runDir;
        if (profile.customDir().isPresent()) {
            runDir = new File(profile.customDir().get());
        } else {
            runDir = new File(OS.getPath());
        }
        if (!runDir.exists()) {
            runDir.mkdirs();
        }
        initialized = true;
    }
    //"C:\Program Files\Eclipse Adoptium\jdk-17.0.3.7-hotspot\bin\java.exe" -Dfile.encoding=UTF-8 -classpath C:\Users\jdv20\dev\java\InnocentDreamExampleMod\build\classes\java\main;C:\Users\jdv20\dev\java\InnocentDreamExampleMod\build\resources\main;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\io.innocentdream\innocentdream\a2023.0.1\e83a665bfb6c7141f333f3a0b8fd49ebb92ffeb1\innocentdream-a2023.0.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.lwjgl\lwjgl-assimp\3.3.1\312072f451794f25670aebb0160ce6ec88aff842\lwjgl-assimp-3.3.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.lwjgl\lwjgl-bgfx\3.3.1\4243b7d255d885980f10c36edfb678729493685e\lwjgl-bgfx-3.3.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.lwjgl\lwjgl-glfw\3.3.1\cbac1b8d30cb4795149c1ef540f912671a8616d0\lwjgl-glfw-3.3.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.lwjgl\lwjgl-nanovg\3.3.1\2f3b4d8f9b8de598517f105ce7e68560f25f11c6\lwjgl-nanovg-3.3.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.lwjgl\lwjgl-nuklear\3.3.1\628ccf80db3d623f1f559978e4246a23b2d1eeb1\lwjgl-nuklear-3.3.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.lwjgl\lwjgl-openal\3.3.1\2623a6b8ae1dfcd880738656a9f0243d2e6840bd\lwjgl-openal-3.3.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.lwjgl\lwjgl-opengl\3.3.1\831a5533a21a5f4f81bbc51bb13e9899319b5411\lwjgl-opengl-3.3.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.lwjgl\lwjgl-par\3.3.1\5615580f6064af74d976a38274c7a07b44237996\lwjgl-par-3.3.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.lwjgl\lwjgl-stb\3.3.1\b119297cf8ed01f247abe8685857f8e7fcf5980f\lwjgl-stb-3.3.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.lwjgl\lwjgl-vulkan\3.3.1\4af1ebb27699d743aba74797b7ac4567f3f1fd4a\lwjgl-vulkan-3.3.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.lwjgl\lwjgl\3.3.1\ae58664f88e18a9bb2c77b063833ca7aaec484cb\lwjgl-3.3.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\com.google.code.gson\gson\2.10.1\b3add478d4382b78ea20b1671390a858002feb6c\gson-2.10.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.joml\joml\1.10.5\22566d58af70ad3d72308bab63b8339906deb649\joml-1.10.5.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\net.lingala.zip4j\zip4j\2.11.4\4f4479dd5e23846f4141b2fcbc805393bce7a90c\zip4j-2.11.4.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\com.github.Querz\NBT\6.1\e8aab87aed2ffa31ad5a2285a2d1438971476bf7\NBT-6.1.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.apache.logging.log4j\log4j-core\2.20.0\eb2a9a47b1396e00b5eee1264296729a70565cc0\log4j-core-2.20.0.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.apache.logging.log4j\log4j-1.2-api\2.20.0\689151374756cb809cb029f2501015bdc7733179\log4j-1.2-api-2.20.0.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\org.apache.logging.log4j\log4j-api\2.20.0\1fe6082e660daf07c689a89c94dc0f49c26b44bb\log4j-api-2.20.0.jar;C:\Users\jdv20\.gradle\caches\modules-2\files-2.1\com.github.JnCrMx\discord-game-sdk4j\v0.5.2\b0c9605aa601d19a571ec3f60c857b5d8c32a5c9\discord-game-sdk4j-v0.5.2.jar io.innocentdream.InnocentDream
    public void launch() {
        if (!initialized) initRunDir();
        Version version = VersionManager.getVersion(profile.version());
        String runDir = profile.customDir().isPresent() ? profile.customDir().get() : OS.getPath();
        HashMap<String, String> env = new HashMap<>(System.getenv());
        env.put("innocentdream.runDir", runDir);
        PipedWriter writer = new PipedWriter();
        if (profile.showLog()) {
            LauncherWindow.mainWindow.logWindow = new LogWindow(profile.version());
            try {
                LogWindow.out = new BufferedReader(new PipedReader(writer));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                new PipedReader(writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            writer.write(print("ID Home: " + runDir, "INFO"));
            writer.write(print("Java Home: " + LauncherApplication.JAVA_HOME, "INFO"));
            //Log4j XML
            File log4jConfig = new File(runDir, "log4j.xml");
            try {
                InputStream is = new URL(version.log4jXML()).openStream();
                Files.copy(is, log4jConfig.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                writer.write(print("Couldn't get log4j.xml", "ERROR"));
            }
            env.put("log4j2.configurationFile", log4jConfig.getAbsolutePath());

            //Args
            List<String> args = new ArrayList<>();
            for (int i = 0; i < version.runArgs().size(); i++) {
                String arg = version.runArgs().get(i).replace("${runDir}", runDir).replace("${configFile}", log4jConfig.getAbsolutePath());
                args.add(arg);
            }
            args.addAll(profile.jvmArgs());
            StringBuilder argsBuilder = new StringBuilder(args.get(0));
            for (int i = 1; i < args.size(); i++) {
                argsBuilder.append(" ").append(args.get(i));
            }
            writer.write(print("Run arguments: " + argsBuilder, "INFO"));

            //Jar
            long jarFileSize = version.jarFileSize();
            String jarDownload = version.jarFile();
            File versionJar = new File(OS.getPath(), "versions/" + profile.version() + "/" + profile.version() + ".jar");
            boolean downloadJar = false;
            if (versionJar.exists()) {
                if (jarFileSize < 0) {
                    writer.write(print("Could not verify game's jar file", "WARN"));
                } else if (jarFileSize != versionJar.length()) {
                    writer.write(print("Jar file sizes don't match, expected" + jarFileSize + ", got " + versionJar.length(), "WARN"));
                    downloadJar = true;
                }
            } else {
                downloadJar = true;
            }
            if (downloadJar) {
                try {
                    InputStream is = new URL(jarDownload).openStream();
                    Files.copy(is, versionJar.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    writer.write(print("Downloaded " + versionJar.getName(), "INFO"));
                } catch (IOException e) {
                    writer.write(print("Failed to download " + versionJar.getName() + " from " + jarDownload, "ERROR"));
                    StackTraceElement[] trace = e.getStackTrace();
                    for (StackTraceElement el : trace) {
                        writer.write(printBasic("\t" + el.toString()));
                    }
                    return;
                }
            }
            StringBuilder classpathBuilder = new StringBuilder(versionJar.getAbsolutePath());

            //Jar Libs
            File jarsFolder = new File(runDir, "libs/jars");
            jarsFolder.mkdirs();
            for (JsonObject object : version.jarLibraries()) {
                String download = object.get("download").getAsString();
                String name = object.get("name").getAsString();
                long size = object.keySet().contains("size") ? object.get("size").getAsLong() : -1;
                File jarFile = new File(jarsFolder, name);
                boolean needsDownload = false;
                if (jarFile.exists()) {
                    if (size >= 0L && jarFile.length() == size) {
                        //writer.write(print("Jar Library: " + name + " exists and is correct size", "INFO"));
                    } else if (size == -1) {
                        //writer.write(print("Jar Library: " + name + " exists, however size is not specified and is assumed to work", "WARN"));
                    } else if (size >= 0L && jarFile.length() != size) {
                        //writer.write(print("Jar Library: " + name + "exists, but is wrong size", "WARN"));
                        needsDownload = true;
                    }
                } else {
                    needsDownload = true;
                }
                if (needsDownload) {
                    try {
                        InputStream is = new URL(download).openStream();
                        Files.copy(is, jarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        writer.write(print("Failed to download " + name + " from " + download, "ERROR"));
                        StackTraceElement[] trace = e.getStackTrace();
                        for (StackTraceElement el : trace) {
                            writer.write(printBasic("\t" + el.toString()));
                        }
                        return;
                    }
                }
                classpathBuilder.append(File.pathSeparatorChar).append(jarFile.getAbsolutePath());
            }
            ArrayList<String> envList = new ArrayList<>();
            for (Map.Entry<String, String> e : env.entrySet()) {
                envList.add(e.getKey() + "=" + e.getValue());
            }

            writer.write(print("Successfully verified Jar Libraries", "INFO"));
            File java = new File(profile.customRuntime().isPresent() ? profile.customRuntime().get() : LauncherApplication.JAVA_HOME,
                    "bin/javaw" + (OS.isWindows() ? ".exe" : ""));
            String runCommand = "\"" + java.getAbsolutePath() + "\" " + argsBuilder + " -classpath " + classpathBuilder + " " + version.mainClass();
            writer.flush();
            Process process = Runtime.getRuntime().exec(runCommand, envList.toArray(new String[0]), new File(runDir));
            LogWindow.out = process.inputReader();
            LogWindow.err = process.errorReader();
            if (profile.keepOpen() < 2 && profile.keepOpen() >= 0) {
                LauncherWindow.mainWindow.setVisible(false);
                try {
                    System.out.println("Exit Code: " + process.waitFor());
                } catch (InterruptedException e) {
                    System.err.println("Error");
                    e.printStackTrace();
                }
                if (profile.keepOpen() == 0) {
                    LauncherWindow.mainWindow.dispose();
                } else if (profile.keepOpen() == 1) {
                    LauncherWindow.mainWindow.setVisible(true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String printBasic(String message) {
        JsonObject data = new JsonObject();
        data.add("basic", new JsonPrimitive(true));
        data.add("message", new JsonPrimitive(message));
        return data + "\n";
    }

    private static String print(String message, String level) {
        JsonObject data = new JsonObject();
        data.add("basic", new JsonPrimitive(false));
        data.add("time", new JsonPrimitive(FORMATTER.format(new Date())));
        data.add("thread", new JsonPrimitive("LaunchThread"));
        data.add("level", new JsonPrimitive(level));
        data.add("logger", new JsonPrimitive("ID Launcher"));
        data.add("message", new JsonPrimitive(message));
        return data + "\n";
    }

}

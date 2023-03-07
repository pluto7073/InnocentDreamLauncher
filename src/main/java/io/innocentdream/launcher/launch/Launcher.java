package io.innocentdream.launcher;

import com.google.gson.JsonObject;
import io.innocentdream.launcher.profile.Profile;
import io.innocentdream.launcher.version.Version;
import io.innocentdream.launcher.version.VersionManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

public class Launcher {

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
        //Log4j XML
        File log4jConfig = new File(runDir, "log4j.xml");
        try {
            InputStream is = new URL(version.log4jXML()).openStream();
            Files.copy(is, log4jConfig.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Couldn't load log4j.xml");
        }

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
        System.out.println("Run Arguments: " + argsBuilder);

        //Jar
        StringBuilder builder = new StringBuilder();

        //Jar Libs
        File jarsFolder = new File(runDir, "libs/jars");
        jarsFolder.mkdirs();
        for (JsonObject object : version.jarLibraries()) {
            String download = object.get("download").getAsString();
            String name = object.get("name").getAsString();
        }
    }

}

package io.innocentdream.launcher.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class Profile {

    private String version, name;
    private Optional<String> customRuntime, customDir;
    private boolean showLog;
    private int keepOpen;
    private Collection<String> jvmArgs;

    public Profile(String version, String name, Collection<String> jvmArgs, Optional<String> customRuntime,
                   Optional<String> customDir, boolean showLog, int keepOpen) {
        this.version = version;
        this.name = name;
        this.jvmArgs = jvmArgs;
        this.customRuntime = customRuntime;
        this.customDir = customDir;
        this.showLog = showLog;
        this.keepOpen = keepOpen;
    }

    public String version() {
        return this.version;
    }

    public String name() {
        return this.name;
    }

    public Optional<String> customRuntime() {
        return this.customRuntime;
    }

    public Optional<String> customDir() {
        return this.customDir;
    }

    public boolean showLog() {
        return this.showLog;
    }

    public int keepOpen() {
        return this.keepOpen;
    }

    public Collection<String> jvmArgs() {
        return this.jvmArgs;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Profile setName(String name) {
        this.name = name;
        return this;
    }

    public void setCustomRuntime(Optional<String> customRuntime) {
        this.customRuntime = customRuntime;
    }

    public void setCustomDir(Optional<String> customDir) {
        this.customDir = customDir;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public void setKeepOpen(int keepOpen) {
        this.keepOpen = keepOpen;
    }

    public void setJvmArgs(Collection<String> jvmArgs) {
        this.jvmArgs = jvmArgs;
    }

    public Profile copy() {
        String version = this.version;
        String name = this.name;
        Optional<String> cusRunT = this.customRuntime;
        Optional<String> cusRunD = this.customDir;
        boolean showLog = this.showLog;
        int keepOpen = this.keepOpen;
        Collection<String> jvmArgs = new ArrayList<>(this.jvmArgs);
        return new Profile(version, name, jvmArgs, cusRunT, cusRunD, showLog, keepOpen);
    }

}

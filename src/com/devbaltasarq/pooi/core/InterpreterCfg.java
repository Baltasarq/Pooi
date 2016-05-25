package com.devbaltasarq.pooi.core;

import java.io.File;

/**
 * Represents the configuration of the interpreter
 * Created by baltasarq on 24/05/16.
 */
public class InterpreterCfg {
    public boolean hasGui() {
        return this.hasGui;
    }

    public void setHasGui(boolean hasGui) {
        this.hasGui = hasGui;
    }

    public boolean isVerbose() {
        return this.isVerbose;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }

    public File getScript() {
        return script;
    }

    public void setScript(File script) {
        this.script = script;
    }

    private boolean hasGui     = true;
    private boolean isVerbose  = true;
    private File script        = null;
}

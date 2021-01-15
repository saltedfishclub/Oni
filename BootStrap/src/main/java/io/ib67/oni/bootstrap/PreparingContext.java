package io.ib67.oni.bootstrap;

import io.ib67.oni.BootstrappedPlugin;
import io.ib67.oni.internal.OniSetting;

import java.util.List;

public final class PreparingContext {
    public PreparingContext(List<IEnvChecker> handlers, OniSetting settings, BootstrappedPlugin module) {
        this.handlers = handlers;
        this.settings = settings;
        this.module = module;
    }

    private LaunchState state = LaunchState.SUCCESS;
    private OniSetting settings;
    private BootstrappedPlugin module;
    private List<IEnvChecker> handlers;

    public LaunchState getState() {
        return state;
    }

    public void setState(LaunchState state) {
        this.state = state;
    }

    public List<IEnvChecker> getHandlers() {
        return handlers;
    }

    public OniSetting getSettings() {
        return settings;
    }

    public BootstrappedPlugin getModule() {
        return module;
    }
}

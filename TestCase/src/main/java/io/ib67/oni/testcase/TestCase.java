package io.ib67.oni.testcase;

import io.ib67.oni.BootstrappedPlugin;
import io.ib67.oni.Oni;

public final class TestCase extends BootstrappedPlugin {

    /**
     * Executed before oniBootstrap actually runs.
     *
     * @since 3.0
     */
    @Override
    public void preEnable() {
        getLogger().info("Pre-Enabled!");
    }

    @Override
    public void beforeEnable() {
        getLogger().info("beforeEnable!");
    }

    @Override
    public void onStart() {
        getLogger().info("Started!");
        Oni.getGlobalLogger().info("HI");
    }

    @Override
    public void onStop() {
        getLogger().info("Stopped!");
    }
}

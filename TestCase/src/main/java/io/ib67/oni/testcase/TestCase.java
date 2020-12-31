package io.ib67.oni.testcase;

import io.ib67.oni.OniModule;

public final class TestCase extends OniModule {

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
        getLogger().info(getOni().toString());
    }

    @Override
    public void onStop() {
        getLogger().info("Stopped!");
    }
}

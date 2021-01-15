package io.ib67.oni.bootstrap;

public interface IEnvChecker {
    void apply(PreparingContext ctx);

    LaunchStage getLaunchStage();
}

package io.ib67.oni;

public abstract class OniModule extends BootstrappedPlugin {
    private Oni oniCore;

    protected Oni getOni() {
        if (oniCore == null) {
            oniCore = Oni.of(this);
        }
        return oniCore;
    }
}

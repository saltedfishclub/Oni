package io.ib67.oni;

import com.google.gson.Gson;
import io.ib67.oni.bootstrap.IEnvChecker;
import io.ib67.oni.bootstrap.LaunchState;
import io.ib67.oni.bootstrap.PreparingContext;
import io.ib67.oni.internal.OniSetting;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class BootstrappedPlugin extends JavaPlugin {
    private OniSetting oniSetting;
    private static final Gson gson = new Gson();
    private final List<IEnvChecker> preEnableCheckers = new ArrayList<>();
    private final List<IEnvChecker> enableCheckers = new ArrayList<>();
    private final List<IEnvChecker> disableCheckers = new ArrayList<>();
    private PreparingContext ctx;

    @Override
    public final void onLoad() {
        beforeEnable();
    }

    @Override
    public final void onDisable() {
        for (IEnvChecker preEnableChecker : disableCheckers) {
            preEnableChecker.apply(ctx);
            if (ctx.getState() == LaunchState.FAIL) {
                if (oniSetting.verbose) {
                    getLogger().info("[Verbose] Checker cancelled.");
                }
                this.setEnabled(false);
                return;
            }
        }
        onStop();
    }

    @Override
    public final void onEnable() {
        Reader textRes = getTextResource("oni.setting.json");
        Validate.notNull(textRes, "Broken plugin (Missing oni.setting.json)");
        oniSetting = gson.fromJson(textRes, OniSetting.class);
//todo load checkers
        List<IEnvChecker> checkerList = new ArrayList<>();
        for (String s : oniSetting.checkerList) {
            getLogger().info("[VERBOSE] Loading... " + s);
            try {
                Class<?> clazz = Class.forName(s);

                checkerList.add((IEnvChecker) clazz.getDeclaredConstructor().newInstance());
                if (oniSetting.verbose) {
                    getLogger().info("[VERBOSE] Loaded...");
                }

            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
                ex.printStackTrace();
                getLogger().warning("Couldn't load checker " + s);
                this.setEnabled(true);
                return;
            }
        }
        ctx = new PreparingContext(checkerList, oniSetting, this);
        checkerList.forEach(e -> {
            switch (e.getLaunchStage()) {
                case PRE_ENABLE:
                    preEnableCheckers.add(e);
                    break;
                case ENABLE:
                    enableCheckers.add(e);
                    break;
                case DISABLE:
                    disableCheckers.add(e);
                    break;
            }
        });
        for (IEnvChecker preEnableChecker : preEnableCheckers) {
            preEnableChecker.apply(ctx);
            if (ctx.getState() == LaunchState.FAIL) {
                if (oniSetting.verbose) {
                    getLogger().info("[Verbose] Checker cancelled. -> preEnable");
                }
                this.setEnabled(false);
                return;
            }
        }

        preEnable();
        for (IEnvChecker preEnableChecker : enableCheckers) {
            preEnableChecker.apply(ctx);
            if (ctx.getState() == LaunchState.FAIL) {
                if (oniSetting.verbose) {
                    getLogger().info("[Verbose] Checker cancelled. -> Enabling");
                }
                this.setEnabled(false);
                return;
            }
        }
        onStart();
    }

    /**
     * plugin.onLoad , previous than preEnable.
     *
     * @since 1.0
     */
    public void beforeEnable() {
    }

    /**
     * Executed before oniBootstrap actually runs.
     * **MENTION** getOni == null !!
     * Only use it for Dependency Inject preparation.
     *
     * @since 3.0
     */
    public void preEnable() {
    }

    /**
     * Executed when bootstrap finishes its work.
     *
     * @since 1.0
     */
    public abstract void onStart();

    /**
     * Executed when server shutting down.
     *
     * @since 1.0
     */
    public abstract void onStop();
}

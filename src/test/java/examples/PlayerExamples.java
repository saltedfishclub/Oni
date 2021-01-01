package examples;

import io.ib67.oni.Oni;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

public class PlayerExamples implements Listener {
    private Oni oni;
    private Logger logger;

    public void testPermission() {
        oni.playerOnionOf("playerName").ifPresent(po -> { //PlayerOnion
            po.oni.addTempPermission("foo.bar");
            po.oni.addTempPermission("foo.bar"); // twice
            po.performCommand("xx"); // have permission todo
            po.performCommand("xx"); // have permission todo
            po.performCommand("xx"); // not now.
        });
        //or
        //@LowLevelAPI
        //PlayerOnion.of()
    }
}

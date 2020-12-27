package io.ib67.oni.exception.player;

/**
 * Throws when player offline but tried to do something with it.
 *
 * @since 1.0
 */
public class PlayerOfflineException extends Exception{
    public PlayerOfflineException(String msg){
        super(msg);
    }
}

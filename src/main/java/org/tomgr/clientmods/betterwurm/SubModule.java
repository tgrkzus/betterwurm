package org.tomgr.clientmods.betterwurm;

import java.util.Properties;

public abstract class SubModule {
    public boolean active = false;

    /**
     * Configure state from properties file
     */
    public abstract void configure(Properties properties);

    /**
     * Activates any functionality of this submodule
     */
    public abstract void activate();

    /**
     * Deactivates any functionality of this submodule
     */
    public abstract void deactivate();

    /**
     * Initialize any required hooks
     */
    public abstract void init();

    /**
     * Handles a console message
     * @return if the message was handled by this handler
     */
    public boolean handleConsole(String string, Boolean aBoolean) {
        return false;
    }
}

package org.tomgr.clientmods.betterwurm.betterui;

import com.wurmonline.client.renderer.gui.*;
import com.wurmonline.client.settings.SavePosManager;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.tomgr.clientmods.betterwurm.SubModule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class BetterUI extends SubModule {
    private static Logger logger = Logger.getLogger(BetterUI.class.getName());

    /**
     * Configure state from properties file
     *
     * @param properties
     */
    @Override
    public void configure(Properties properties) {

    }

    /**
     * Activates any functionality of this submodule
     */
    @Override
    public void activate() {
        active = true;
    }

    /**
     * Deactivates any functionality of this submodule
     */
    @Override
    public void deactivate() {
        active = false;
    }

    /**
     * Initialize any required hooks
     */
    @Override
    public void init() {

        if (active) {
            activate();
        }
    }
}

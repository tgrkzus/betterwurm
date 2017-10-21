package org.tomgr.clientmods.betterwurm.bettercrafting;

import com.wurmonline.client.game.World;
import com.wurmonline.client.renderer.RenderVector;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.tomgr.clientmods.betterwurm.SubModule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Logger;

public class BetterCrafting extends SubModule {
    private static Logger logger = Logger.getLogger(BetterCrafting.class.getName());

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

        World world = ReflectionUtil.getPrivateField(
                proxy, ReflectionUtil.getField(proxy.getClass(), "world"));

        if (active) {
            activate();
        }
    }
}

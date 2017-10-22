package org.tomgr.clientmods.betterwurm.bettercrafting;

import com.wurmonline.client.game.World;
import com.wurmonline.client.renderer.RenderVector;
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

public class BetterCrafting extends SubModule {
    private static Logger logger = Logger.getLogger(BetterCrafting.class.getName());
    private HeadsUpDisplay hud = null;
    private CreationListWindow creationListWindow = null;
    private BetterCraftingWindow betterCraftingWindow = null;
    private boolean loaded = false;

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


        HookManager.getInstance().registerHook("com.wurmonline.client.renderer.WorldRender",
                "render", null,
                new InvocationHandlerFactory() {
                    @Override
                    public InvocationHandler createInvocationHandler() {
                        return new InvocationHandler() {

                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                 if (active) {
                                    if (!loaded) {
                                        hud = ReflectionUtil.getPrivateField(
                                                WurmComponent.class, ReflectionUtil.getField(WurmComponent.class, "hud"));

                                        creationListWindow = ReflectionUtil.getPrivateField(
                                                hud, ReflectionUtil.getField(hud.getClass(), "creationListWindow"));

                                        // Force load the crafting items (TODO reloadability?)
                                        hud.getWorld().getServerConnection().sendRequestFullCreateItemList();

                                        betterCraftingWindow = new BetterCraftingWindow(hud.getInventoryWindow());
                                        hud.getComponents().add(betterCraftingWindow);

                                        MainMenu mainMenu = ReflectionUtil.getPrivateField(hud, ReflectionUtil.getField(hud.getClass(), "mainMenu"));
                                        mainMenu.registerComponent("Better Crafting Window", betterCraftingWindow);

                                        SavePosManager savePosManager = ReflectionUtil.getPrivateField(hud, ReflectionUtil.getField(hud.getClass(), "savePosManager"));
                                        savePosManager.registerAndRefresh(betterCraftingWindow, "bettercraftingwindow");

                                        loaded = true;
                                    }

                                    if (betterCraftingWindow != null && !betterCraftingWindow.hasData()) {
                                        List<CreationListItem> createItemList = ReflectionUtil.getPrivateField(
                                                creationListWindow, ReflectionUtil.getField(creationListWindow.getClass(),
                                                        "createItemList"));

                                        Map<Short, CreationListItem> categoryMap = ReflectionUtil.getPrivateField(
                                                creationListWindow, ReflectionUtil.getField(creationListWindow.getClass(),
                                                        "categoryMap"));

                                        if (categoryMap != null && !categoryMap.isEmpty()
                                                && createItemList != null && !createItemList.isEmpty()) {
                                            betterCraftingWindow.populate(createItemList, categoryMap);
                                        }
                                    }
                                }


                                return method.invoke(proxy, args);
                            }
                        };
                    }
                });

        if (active) {
            activate();
        }
    }
}

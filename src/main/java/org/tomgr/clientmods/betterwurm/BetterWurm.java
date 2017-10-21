package org.tomgr.clientmods.betterwurm;

import com.wurmonline.client.game.World;
import com.wurmonline.client.renderer.RenderVector;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmClientMod;
import org.gotti.wurmunlimited.modsupport.console.ConsoleListener;
import org.gotti.wurmunlimited.modsupport.console.ModConsole;
import org.tomgr.clientmods.betterwurm.bettercrafting.BetterCrafting;
import org.tomgr.clientmods.betterwurm.thirdperson.ThirdPerson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BetterWurm implements WurmClientMod, Initable, ConsoleListener, Configurable {

    private static Logger logger = Logger.getLogger(BetterWurm.class.getName());

    private ThirdPerson thirdPerson = new ThirdPerson();
    private BetterCrafting betterCrafting = new BetterCrafting();

    @Override
    public void configure(Properties properties) {
        if (Boolean.valueOf(properties.getProperty("thirdperson", "false"))) {
            thirdPerson.active = true;
        }
        logger.log(Level.INFO, "thirdperson: " + thirdPerson);

        if (Boolean.valueOf(properties.getProperty("bettercrafting", "false"))) {
            betterCrafting.active = true;
        }
        logger.log(Level.INFO, "bettercrafting: " + betterCrafting);
    }

    @Override
    public void init() {
        thirdPerson.init();
        betterCrafting.init();

        ModConsole.addConsoleListener(this);
    }

    @Override
    public boolean handleInput(String string, Boolean aBoolean) {
        // Submodule toggles
        if (string != null && string.startsWith("toggle tp")) {
            if (thirdPerson.active) {
                thirdPerson.deactivate();
            }
            else {
                thirdPerson.activate();
            }
            return true;
        }

        if (string != null && string.startsWith("toggle bettercrafting")) {
            if (betterCrafting.active) {
                betterCrafting.deactivate();
            }
            else {
                betterCrafting.activate();
            }
            return true;
        }

        // Submodule processing
        if (thirdPerson.active && thirdPerson.handleConsole(string, aBoolean)) {
            return true;
        }

        if (betterCrafting.active && betterCrafting.handleConsole(string, aBoolean)) {
            return true;
        }

        return false;
    }
}

package org.tomgr.clientmods.betterwurm.thirdperson;

import com.wurmonline.client.game.World;
import com.wurmonline.client.renderer.RenderVector;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmClientMod;
import org.gotti.wurmunlimited.modsupport.console.ConsoleListener;
import org.gotti.wurmunlimited.modsupport.console.ModConsole;
import org.tomgr.clientmods.betterwurm.SubModule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Logger;

public class ThirdPerson extends SubModule {
    private static Logger logger = Logger.getLogger(ThirdPerson.class.getName());
    private float dist = 4.0f;
    private float pitch = 45.0f;
    private float zoomFactor = 1.0f;

    private static final float ZOOM_MAX = 2.5f;
    private static final float ZOOM_MIN = 0.0f;

    private static final float PITCH_MAX = 89.9f;
    private static final float PITCH_MIN = 30.0f;

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
        // TODO no way to disable a hook?

        HookManager.getInstance().registerHook("com.wurmonline.client.renderer.WorldRender",
                "getCameraX", null,
                new InvocationHandlerFactory() {
                    @Override
                    public InvocationHandler createInvocationHandler() {
                        return new InvocationHandler() {

                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                if (active) {
                                    RenderVector camera = ReflectionUtil.getPrivateField(
                                            proxy, ReflectionUtil.getField(proxy.getClass(), "cameraOffset"));
                                    World world = ReflectionUtil.getPrivateField(
                                            proxy, ReflectionUtil.getField(proxy.getClass(), "world"));

                                    float x = camera.getX();
                                    float y = camera.getY();
                                    float z = camera.getZ();
                                    double yaw = Math.atan2(z, x);
                                    setPitch(world.getPlayerRotY());
                                    return zoomFactor * -dist * Math.cos(yaw) * Math.cos(Math.toRadians(pitch)) + (float) method.invoke(proxy, args);
                                }
                                else {
                                    return method.invoke(proxy, args);
                                }
                            }
                        };
                    }
                });

        HookManager.getInstance().registerHook("com.wurmonline.client.renderer.WorldRender",
                "getCameraY", null,
                new InvocationHandlerFactory() {
                    @Override
                    public InvocationHandler createInvocationHandler() {
                        return new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                if (active) {
                                    RenderVector camera = ReflectionUtil.getPrivateField(
                                            proxy, ReflectionUtil.getField(proxy.getClass(), "cameraOffset"));
                                    World world = ReflectionUtil.getPrivateField(
                                            proxy, ReflectionUtil.getField(proxy.getClass(), "world"));

                                    float x = camera.getX();
                                    float y = camera.getY();
                                    float z = camera.getZ();
                                    setPitch(world.getPlayerRotY());

                                    return zoomFactor * Math.sin(Math.toRadians(pitch)) + (float) method.invoke(proxy, args);
                                }
                                else {
                                    return method.invoke(proxy, args);
                                }
                            }
                        };
                    }
                });

        HookManager.getInstance().registerHook("com.wurmonline.client.renderer.WorldRender",
                "getCameraZ", null,
                new InvocationHandlerFactory() {
                    @Override
                    public InvocationHandler createInvocationHandler() {
                        return new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                if (active) {
                                    RenderVector camera = ReflectionUtil.getPrivateField(
                                            proxy, ReflectionUtil.getField(proxy.getClass(), "cameraOffset"));
                                    World world = ReflectionUtil.getPrivateField(
                                            proxy, ReflectionUtil.getField(proxy.getClass(), "world"));

                                    float x = camera.getX();
                                    float y = camera.getY();
                                    float z = camera.getZ();
                                    double yaw = Math.atan2(z, x);
                                    setPitch(world.getPlayerRotY());
                                    return zoomFactor * -dist * Math.sin(yaw) * Math.cos(Math.toRadians(pitch)) + (float) method.invoke(proxy, args);
                                }
                                else {
                                    return method.invoke(proxy, args);
                                }
                            }
                        };
                    }
                });


        if (active) {
            activate();
        }
    }

    public void addZoom(float factor) {
        setZoom(zoomFactor + factor);
    }

    public void setZoom(float factor) {
        zoomFactor = factor;

        if (zoomFactor < ZOOM_MIN) {
            zoomFactor = ZOOM_MIN;
        }

        if (zoomFactor > ZOOM_MAX) {
            zoomFactor = ZOOM_MAX;
        }
    }

    public void addPitch(float factor) {
        setPitch(pitch + factor);
    }

    public void setPitch(float factor) {
        pitch = factor;

        if (pitch < PITCH_MIN) {
            pitch = PITCH_MIN;
        }

        if (pitch > PITCH_MAX) {
            pitch = PITCH_MAX;
        }
    }

    /**
     * Handles a console message
     * @return if the message was handled by this handler
     */
    @Override
    public boolean handleConsole(String string, Boolean aBoolean) {
        if (string != null && string.startsWith("tp zoom-in")) {
            addZoom(-0.1f);
            return true;
        }

        if (string != null && string.startsWith("tp zoom-out")) {
            addZoom(0.1f);
            return true;
        }
        return false;
    }
}

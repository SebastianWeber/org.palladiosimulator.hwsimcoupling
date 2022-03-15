package org.palladiosimulator.hwsimcoupling;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.palladiosimulator.hwsimcoupling.configuration.ExtensionCache;
import org.palladiosimulator.hwsimcoupling.configuration.ProfileCache;
import org.palladiosimulator.hwsimcoupling.util.DemandCache;
import org.palladiosimulator.hwsimcoupling.util.impl.DemandCacheImpl;

public class CacheInitializer extends AbstractUIPlugin {

    // Singleton

    private static CacheInitializer instance;

    public void start(BundleContext context) throws Exception {
        super.start(context);
        instance = this;
    }

    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }

    public static CacheInitializer getInstance() {
        return instance;
    }

    // Caches

    private DemandCache demandCache;
    private ProfileCache profileCache;
    private ExtensionCache extensionCache;

    public CacheInitializer() {
        profileCache = new ProfileCache();
        extensionCache = new ExtensionCache();
        demandCache = new DemandCacheImpl(profileCache);
    }

    public DemandCache getDemandCache() {
        return demandCache;
    }

    public ProfileCache getProfileCache() {
        return profileCache;
    }

    public ExtensionCache getExtensionCache() {
        return extensionCache;
    }

    // Saving

    private boolean saveDemandsActive = false;

    public void initSaveDemands() {
        if (!saveDemandsActive) {
            ILaunch simuLizarLaunchOrNull = null;
            ILaunchManager manager = DebugPlugin.getDefault()
                .getLaunchManager();
            for (ILaunch launch : manager.getLaunches()) {
                try {
                    if (launch.getLaunchConfiguration() == null) {
                        return;
                    }
                    if (launch.getLaunchConfiguration()
                        .getType()
                        .getIdentifier()
                        .equals("de.upb.pcm.interpreter.SimuLizarLaunching")) {
                        simuLizarLaunchOrNull = launch;
                    }
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
            if (simuLizarLaunchOrNull != null) {
                final ILaunch simuLizarLaunch = simuLizarLaunchOrNull;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (!simuLizarLaunch.isTerminated()) {
                                Thread.sleep(1000);
                            }
                            demandCache.saveDemands();
                            saveDemandsActive = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                saveDemandsActive = true;
            }
        }
    }

}

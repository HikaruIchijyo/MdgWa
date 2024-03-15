package its.madruga.wpp.xposed;

import java.util.HashSet;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import es.chiteroman.bootloaderspoofer.XSpoofBl;
import io.github.lsposed.disableflagsecure.XDisableFlagSecure;
import its.madruga.wpp.xposed.plugins.core.XDatabases;
import its.madruga.wpp.xposed.plugins.core.XMain;

public class XposedMain implements IXposedHookLoadPackage {
    public static XSharedPreferences getPref() {
        XSharedPreferences pref = new XSharedPreferences("its.madruga.wpp");
        pref.makeWorldReadable();
        pref.reload();
        return pref;
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        var packageName = lpparam.packageName;
        var classLoader = lpparam.classLoader;
        if (packageName.equals("its.madruga.wpp")) {
            XposedChecker.setActiveModule(lpparam.classLoader);
        }

        XposedBridge.log("[•] This package: " + lpparam.packageName);
        XposedBridge.log("[•] Loaded packages: " + getPref().getStringSet("whatsapp_packages", new HashSet<>()));
        if (getPref().getStringSet("whatsapp_packages", new HashSet<>()).contains(lpparam.packageName)) {
            XMain.Initialize(classLoader, getPref());
            XDatabases.Initialize(classLoader, getPref());
        }

        XSpoofBl.doHook(lpparam);
        XDisableFlagSecure.doHook(lpparam);
    }

}
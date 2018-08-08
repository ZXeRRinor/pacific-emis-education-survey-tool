package fm.doe.national;

import android.app.Application;

import com.amitshekhar.DebugDB;
import com.crashlytics.android.Crashlytics;

import fm.doe.national.di.AppComponent;
import fm.doe.national.di.DaggerAppComponent;
import fm.doe.national.di.modules.ContextModule;
import io.fabric.sdk.android.Fabric;

public class MicronesiaApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appComponent = DaggerAppComponent.builder().contextModule(new ContextModule(this)).build();

        DebugDB.getAddressLog();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

}

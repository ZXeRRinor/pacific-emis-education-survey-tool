package fm.doe.national.di.modules;

import android.content.Context;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.converters.SurveyImporter;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;

@Module(includes = {ContextModule.class, GsonModule.class})
public class DatabaseHelperModule {

    @Provides
    @Singleton
    public DatabaseHelper provideDatabaseHelper(Context context, Gson gson) {
        DatabaseHelper helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        helper.setGson(gson);
        return helper;
    }

}

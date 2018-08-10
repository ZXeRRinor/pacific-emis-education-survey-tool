package fm.doe.national.data.data_source.db.dao;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import fm.doe.national.BuildConfig;
import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.converters.GroupStandardContainer;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteAnswer;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteCriteria;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteGroupStandard;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteSchool;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteStandard;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteSubCriteria;
import fm.doe.national.data.data_source.db.models.survey.OrmLiteSurvey;
import fm.doe.national.data.models.survey.Criteria;
import fm.doe.national.data.models.survey.Standard;
import fm.doe.national.data.models.survey.SubCriteria;
import fm.doe.national.di.AppComponent;
import fm.doe.national.utils.StreamUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "NDOE_Data_Collection.db";

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private GroupStandardDao groupStandardDao;
    private StandardDao standardDao;
    private CriteriaDao criteriaDao;
    private SubCriteriaDao subCriteriaDao;
    private AnswerDao answerDao;

    private AssetManager assetManager;
    private Gson gson;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, BuildConfig.DATA_BASE_VERSION);
        assetManager = context.getAssets();
        gson = MicronesiaApplication.getAppComponent().getGson();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        createAllTables(connectionSource);
        fillAllTables();
    }

    private void fillAllTables() {
        try {
            InputStream inputStream = assetManager.open(BuildConfig.SURVEYS_FILE_NAME);
            String data = StreamUtils.asString(inputStream);

            GroupStandardContainer groupStandardContainer = gson.fromJson(data, GroupStandardContainer.class);
            for (OrmLiteGroupStandard groupStandard : groupStandardContainer.getGroupStandards()) {
                getGroupStandardDao().create(groupStandard);

                for (Standard standard : groupStandard.getStandards()) {
                    OrmLiteStandard ormLiteStandard = new OrmLiteStandard(standard.getName(), groupStandard);
                    getStandardDao().create(ormLiteStandard);

                    for (Criteria criteria : standard.getCriterias()) {
                        OrmLiteCriteria ormLiteCriteria = new OrmLiteCriteria(criteria.getName(), ormLiteStandard);
                        getCriteriaDao().create(ormLiteCriteria);

                        for (SubCriteria subCriteria : criteria.getSubCriterias()) {
                            OrmLiteSubCriteria ormLiteSubCriteria = new OrmLiteSubCriteria(subCriteria.getName(), ormLiteCriteria);
                            getSubCriteriaDao().create(ormLiteSubCriteria);
                        }
                    }
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void createAllTables(ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, OrmLiteCriteria.class);
            TableUtils.createTable(connectionSource, OrmLiteGroupStandard.class);
            TableUtils.createTable(connectionSource, OrmLiteSchool.class);
            TableUtils.createTable(connectionSource, OrmLiteStandard.class);
            TableUtils.createTable(connectionSource, OrmLiteSubCriteria.class);
            TableUtils.createTable(connectionSource, OrmLiteSurvey.class);
            TableUtils.createTable(connectionSource, OrmLiteAnswer.class);
        } catch (SQLException exc) {
            Log.e(TAG, "Error create Db " + DATABASE_NAME);
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        // Now we don't need this method
        dropAllTables();
        createAllTables(connectionSource);
        fillAllTables();
    }

    private void dropAllTables() {
        try {
            TableUtils.dropTable(connectionSource, OrmLiteCriteria.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteGroupStandard.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteSchool.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteStandard.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteSubCriteria.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteSurvey.class, true);
            TableUtils.dropTable(connectionSource, OrmLiteAnswer.class, true);
        } catch (SQLException exc) {
            Log.e(TAG, "Error drop Db " + DATABASE_NAME);
            throw new RuntimeException(exc);
        }
    }

    @NonNull
    public SchoolDao getSchoolDao() throws SQLException {
        if (schoolDao == null) {
            schoolDao = new SchoolDao(getConnectionSource(), OrmLiteSchool.class);
        }
        return schoolDao;
    }

    public SurveyDao getSurveyDao() throws SQLException {
        if (surveyDao == null) {
            surveyDao = new SurveyDao(getConnectionSource(), OrmLiteSurvey.class);
        }
        return surveyDao;
    }

    public GroupStandardDao getGroupStandardDao() throws SQLException {
        if (groupStandardDao == null) {
            groupStandardDao = new GroupStandardDao(getConnectionSource(), OrmLiteGroupStandard.class);
        }
        return groupStandardDao;
    }

    public StandardDao getStandardDao() throws SQLException {
        if (standardDao == null) {
            standardDao = new StandardDao(getConnectionSource(), OrmLiteStandard.class);
        }
        return standardDao;
    }

    public CriteriaDao getCriteriaDao() throws SQLException {
        if (criteriaDao == null) {
            criteriaDao = new CriteriaDao(getConnectionSource(), OrmLiteCriteria.class);
        }
        return criteriaDao;
    }

    public SubCriteriaDao getSubCriteriaDao() throws SQLException {
        if (subCriteriaDao == null) {
            subCriteriaDao = new SubCriteriaDao(getConnectionSource(), OrmLiteSubCriteria.class);
        }
        return subCriteriaDao;
    }

    public AnswerDao getAnswerDao() throws SQLException {
        if (answerDao == null) {
            answerDao = new AnswerDao(getConnectionSource(), OrmLiteAnswer.class);
        }
        return answerDao;
    }
}

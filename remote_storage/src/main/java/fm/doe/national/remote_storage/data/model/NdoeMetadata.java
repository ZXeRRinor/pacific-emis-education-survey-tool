package fm.doe.national.remote_storage.data.model;

import androidx.annotation.Nullable;

import com.google.api.services.drive.model.File;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.utils.DateUtils;

public class NdoeMetadata {

    private static final String KEY_SCHOOL_ID = "schoolNo";
    private static final String KEY_COMPLETION = "surveyCompleted";
    private static final String KEY_COMPLETION_DATE = "surveyCompletedDateTime";
    private static final String KEY_SURVEY_DATE = "surveyDate";
    private static final String KEY_CREATION_DATE = "createDateTime";
    private static final String KEY_CREATION_USER = "createUser";
    private static final String KEY_LAST_EDIT_DATE = "lastEditedDateTime";
    private static final String KEY_LAST_EDIT_USER = "lastEditedUser";

    private static final String VALUE_TRUE = "POSITIVE";
    private static final String VALUE_FALSE = "NEGATIVE";

    private String schoolId;
    private String lastEditedUser;
    private boolean isSurveyCompleted;
    private String creator;
    private Date lastEditedDate;
    private Date surveyDate;
    private Date creationDate;

    @Nullable
    private Date completionDate;

    public static NdoeMetadata extract(File file) {
        NdoeMetadata metadata = new NdoeMetadata();
        Map<String, String> appProperties = file.getAppProperties();

        if (appProperties == null) {
            return metadata;
        }

        metadata.schoolId = appProperties.get(KEY_SCHOOL_ID);
        metadata.lastEditedUser = appProperties.get(KEY_LAST_EDIT_USER);
        metadata.isSurveyCompleted = convertMetadataToBoolean(appProperties.get(KEY_COMPLETION));
        metadata.creator = appProperties.get(KEY_CREATION_USER);

        String editDateString = appProperties.get(KEY_LAST_EDIT_DATE);
        if (editDateString != null) {
            metadata.lastEditedDate = DateUtils.parseUtc(editDateString);
        }

        String surveyDateString = appProperties.get(KEY_SURVEY_DATE);
        if (surveyDateString != null) {
            metadata.surveyDate = DateUtils.parseUi(surveyDateString);
        }

        String createDateString = appProperties.get(KEY_CREATION_DATE);
        if (createDateString != null) {
            metadata.creationDate = DateUtils.parseUtc(createDateString);
        }

        String completionDateString = appProperties.get(KEY_COMPLETION_DATE);
        if (completionDateString != null) {
            metadata.completionDate = DateUtils.parseUtc(completionDateString);
        }

        return metadata;
    }

    private NdoeMetadata() {
        // private constructor
    }

    public NdoeMetadata(Survey survey, String user) {
        schoolId = survey.getSchoolId();
        isSurveyCompleted = survey.isCompleted();

        if (isSurveyCompleted) {
            completionDate = survey.getCompleteDate();
        }

        surveyDate = survey.getSurveyDate();
        creationDate = survey.getCreateDate();
        creator = user;
        lastEditedDate = new Date();
        lastEditedUser = user;
    }

    public File applyToDriveFile(File file) {
        File updatedFile = new File();
        updatedFile.setName(file.getName());
        updatedFile.setParents(file.getParents());
        updatedFile.setMimeType(file.getMimeType());

        Map<String, String> properties = file.getAppProperties();

        if (properties == null) {
            properties = new HashMap<>();
        }

        if (!properties.containsKey(KEY_SCHOOL_ID)) {
            properties.put(KEY_SCHOOL_ID, schoolId);
        }

        properties.put(KEY_LAST_EDIT_DATE, DateUtils.formatUtc(lastEditedDate));
        properties.put(KEY_LAST_EDIT_USER, lastEditedUser);

        boolean wasCompleted = convertMetadataToBoolean(properties.get(KEY_COMPLETION));
        if (!wasCompleted) {
            properties.put(KEY_COMPLETION, convertBoolToMetadata(isSurveyCompleted));
            if (isSurveyCompleted) {
                properties.put(KEY_COMPLETION_DATE, DateUtils.formatUtc(completionDate));
            }
        }

        String existingSurveyDate = properties.get(KEY_SURVEY_DATE);
        if (existingSurveyDate == null) {
            properties.put(KEY_SURVEY_DATE, DateUtils.formatUi(surveyDate));
        }

        String existingCreationDate = properties.get(KEY_CREATION_DATE);
        if (existingCreationDate == null) {
            properties.put(KEY_CREATION_DATE, DateUtils.formatUtc(creationDate));
        }

        String existingCreator = properties.get(KEY_CREATION_USER);
        if (existingCreator == null) {
            properties.put(KEY_CREATION_USER, creator);
        }

        updatedFile.setAppProperties(properties);
        return updatedFile;
    }

    private static String convertBoolToMetadata(boolean bool) {
        return bool ? VALUE_TRUE : VALUE_FALSE;
    }

    private static boolean convertMetadataToBoolean(@Nullable String data) {
        return data != null && data.equals(VALUE_TRUE);
    }

    @Override
    public String toString() {
        return "NdoeMetadata{" +
                "schoolId='" + schoolId + '\'' +
                ", lastEditedUser='" + lastEditedUser + '\'' +
                ", isSurveyCompleted=" + isSurveyCompleted +
                ", creator='" + creator + '\'' +
                ", lastEditedDate=" + lastEditedDate +
                ", surveyDate=" + surveyDate +
                ", creationDate=" + creationDate +
                ", completionDate=" + completionDate +
                '}';
    }
}
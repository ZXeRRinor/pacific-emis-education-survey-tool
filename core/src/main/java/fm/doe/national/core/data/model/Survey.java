package fm.doe.national.core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;

public interface Survey extends Progressable, IdentifiedObject {

    int getVersion();

    @NonNull
    SurveyType getSurveyType();

    @Nullable
    Date getCreateDate();

    @Nullable
    Date getSurveyDate();

    @Nullable
    Date getCompleteDate();

    default boolean isCompleted() {
        return getCompleteDate() != null;
    }

    @Nullable
    String getSchoolName();

    @Nullable
    String getSchoolId();

    @NonNull
    AppRegion getAppRegion();

    int getPhotosCount();

}

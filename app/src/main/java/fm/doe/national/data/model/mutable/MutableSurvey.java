package fm.doe.national.data.model.mutable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.model.SurveyType;

public class MutableSurvey extends BaseMutableEntity implements Survey {

    private int version;
    private SurveyType surveyType;
    private Date date;
    private String schoolName;
    private String schoolId;
    private List<MutableCategory> categories;

    public MutableSurvey() {
    }

    public MutableSurvey(@NonNull Survey other) {
        this.id = other.getId();
        this.version = other.getVersion();
        this.surveyType = other.getSurveyType();
        this.date = other.getDate();
        this.schoolName = other.getSchoolName();
        this.schoolId = other.getSchoolId();

        if (other.getCategories() == null) {
            return;
        }

        ArrayList<MutableCategory> categories = new ArrayList<>();
        for (Category category : other.getCategories()) {
            categories.add(new MutableCategory(category));
        }
        this.categories = categories;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @NonNull
    @Override
    public SurveyType getSurveyType() {
        return surveyType;
    }

    @Nullable
    @Override
    public Date getDate() {
        return date;
    }

    @Nullable
    @Override
    public String getSchoolName() {
        return schoolName;
    }

    @Nullable
    @Override
    public String getSchoolId() {
        return schoolId;
    }

    @Nullable
    @Override
    public List<MutableCategory> getCategories() {
        return categories;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setSurveyType(SurveyType surveyType) {
        this.surveyType = surveyType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setCategories(List<MutableCategory> categories) {
        this.categories = categories;
    }
}

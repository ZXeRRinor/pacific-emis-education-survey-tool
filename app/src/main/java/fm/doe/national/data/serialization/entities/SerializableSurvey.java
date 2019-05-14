package fm.doe.national.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.Date;
import java.util.List;

import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.model.SurveyType;
import fm.doe.national.data.serialization.converters.SurveyTypeConverter;
import fm.doe.national.app_support.utils.CollectionUtils;


@Root(name = "survey")
public class SerializableSurvey implements Survey {

    @Element
    @Convert(SurveyTypeConverter.class)
    SurveyType type;

    @Element
    int version;

    @Nullable
    @Element(required = false)
    Date date;

    @Nullable
    @Element(required = false)
    String schoolId;

    @Nullable
    @Element(required = false)
    String schoolName;

    @ElementList(inline = true)
    List<SerializableCategory> categories;

    public SerializableSurvey() {
    }

    public SerializableSurvey(@NonNull Survey other) {
        this.version = other.getVersion();
        this.type = other.getSurveyType();
        this.date = other.getDate();
        this.schoolName = other.getSchoolName();
        this.schoolId = other.getSchoolId();
        this.categories = CollectionUtils.map(other.getCategories(), SerializableCategory::new);
    }

    @Override
    public int getVersion() {
        return version;
    }

    @NonNull
    @Override
    public SurveyType getSurveyType() {
        return type;
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
    public List<? extends Category> getCategories() {
        return categories;
    }

    @Override
    public long getId() {
        return 0;
    }
}

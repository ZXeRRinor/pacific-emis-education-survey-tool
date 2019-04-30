package fm.doe.national.data.persistence.entity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

import fm.doe.national.data.model.Category;
import fm.doe.national.data.model.Standard;

@Entity(foreignKeys = @ForeignKey(entity = PersistenceSurvey.class, parentColumns = "uid", childColumns = "survey_id", onDelete = ForeignKey.CASCADE),
        indices = {@Index("uid"), @Index("survey_id")})
public class PersistenceCategory implements Category {

    @PrimaryKey(autoGenerate = true)
    public long uid;

    public String title;

    @ColumnInfo(name = "survey_id")
    public long surveyId;

    public PersistenceCategory(String title, long surveyId) {
        this.title = title;
        this.surveyId = surveyId;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public List<? extends Standard> getStandards() {
        return null;
    }

    @Override
    public long getId() {
        return uid;
    }

}

package fm.doe.national.data.data_source.models.db;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.SynchronizePlatform;

@DatabaseTable
public class OrmLiteAnswer implements Answer {

    public interface Column {
        String ID = "id";
        String STATE = "state";
        String SYNCHRONIZED_PLATFORMS = "synchronizePlatforms";
        String COMMENT = "comment";
        String PHOTOS = "photos";
        String SURVEY_ITEM = "surveyItem";
        String SURVEY_PASSING = "surveyPassing";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(columnName = Column.STATE, dataType = DataType.ENUM_STRING)
    protected State state;

    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = Column.SYNCHRONIZED_PLATFORMS)
    protected ArrayList<SynchronizePlatform> synchronizePlatforms;

    @Nullable
    @DatabaseField(columnName = Column.COMMENT, canBeNull = true)
    protected String comment;

    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = Column.PHOTOS)
    protected ArrayList<String> photos;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY_ITEM)
    protected OrmLiteSurveyItem surveyItem;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY_PASSING)
    protected OrmLiteSurveyPassing surveyPassing;

    public OrmLiteAnswer() {
    }

    public OrmLiteAnswer(State state, OrmLiteSurveyItem surveyItem, OrmLiteSurveyPassing surveyPassing) {
        this.state = state;
        this.surveyItem = surveyItem;
        this.surveyPassing = surveyPassing;
        this.synchronizePlatforms = new ArrayList<>();
        this.photos = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        if (this.state != state) {
            this.state = state;
            synchronizePlatforms.clear();
        }
    }

    @Override
    public List<SynchronizePlatform> getSynchronizedPlatforms() {
        return synchronizePlatforms;
    }

    @Override
    public void addSynchronizedPlatform(SynchronizePlatform platform) {
        if (!synchronizePlatforms.contains(platform)) {
            synchronizePlatforms.add(platform);
        }
    }

    public OrmLiteSurveyItem getSurveyItem() {
        return surveyItem;
    }

    public OrmLiteSurveyPassing getSurveyPassing() {
        return surveyPassing;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(@NonNull String comment) {
        this.comment = comment;
    }

    @Override
    public List<String> getPhotos() {
        return photos;
    }

    @Override
    public void setPhotos(List<String> photos) {
        this.photos.clear();
        this.photos.addAll(photos);
    }
}

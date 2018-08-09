package fm.doe.national.data.models.survey;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.data.models.SynchronizePlatform;

@DatabaseTable
public class Answer {

    public interface Column {
        String ID = "id";
        String ANSWER = "answer";
        String SYNCHRONIZED_PLATFORMS = "synchronizePlatforms";
        String SUB_CRITERIA = "subCriteria";
        String SURVEY = "survey";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;
    @DatabaseField(columnName = Column.ANSWER)
    protected boolean answer;
    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = Column.SYNCHRONIZED_PLATFORMS)
    protected ArrayList<SynchronizePlatform> synchronizePlatforms;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SUB_CRITERIA)
    protected SubCriteria subCriteria;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY)
    protected Survey survey;

    public Answer() {
    }

    public Answer(boolean answer, SubCriteria subCriteria, Survey survey) {
        this.answer = answer;
        this.subCriteria = subCriteria;
        this.survey = survey;
        this.synchronizePlatforms = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        if (this.answer != answer) {
            this.answer = answer;
            synchronizePlatforms.clear();
        }
    }

    public List<SynchronizePlatform> getSynchronizedPlatforms() {
        return synchronizePlatforms;
    }

    public void addSynchronizedPlatform(SynchronizePlatform platform) {
        if (!synchronizePlatforms.contains(platform)) {
            synchronizePlatforms.add(platform);
        }
    }

}

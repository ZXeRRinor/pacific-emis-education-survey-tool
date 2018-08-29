package fm.doe.national.data.data_source.models;

import java.util.Date;

public interface SurveyPassing extends Identifiable {
    int getYear();
    Date getStartDate();
    Date getEndDate();
}

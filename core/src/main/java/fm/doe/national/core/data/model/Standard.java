package fm.doe.national.core.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public interface Standard extends Progressable, IdentifiedObject {

    @NonNull
    String getTitle();

    @NonNull
    String getSuffix();

    @Nullable
    List<? extends Criteria> getCriterias();
}
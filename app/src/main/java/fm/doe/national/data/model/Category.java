package fm.doe.national.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public interface Category extends IdentifiedObject {

    @NonNull
    String getTitle();

    @Nullable
    List<? extends Standard> getStandards();
}

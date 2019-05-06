package fm.doe.national.data.serialization.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import fm.doe.national.data.model.Criteria;
import fm.doe.national.data.model.Standard;

@Root(name = "standard")
public class SerializableStandard implements Standard {

    @Element
    String name;

    @Nullable
    @ElementList(inline = true, required = false)
    List<SerializableCriteria> criterias;

    @Nullable
    @Element(name = "id", required = false)
    String index;

    @NonNull
    @Override
    public String getTitle() {
        return name;
    }

    @NonNull
    @Override
    public String getSuffix() {
        return index == null ? "" : index;
    }

    @NonNull
    @Override
    public List<? extends Criteria> getCriterias() {
        return criterias;
    }

    @Override
    public long getId() {
        return 0;
    }

}

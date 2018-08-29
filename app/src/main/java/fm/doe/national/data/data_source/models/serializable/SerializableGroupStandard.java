package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;

@Xml(name = "groupStandard")
public class SerializableGroupStandard implements GroupStandard {

    @PropertyElement
    String name;

    @Element
    List<SerializableStandard> standards;

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    public List<? extends Standard> getStandards() {
        return standards;
    }
}

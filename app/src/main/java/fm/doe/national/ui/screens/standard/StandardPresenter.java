package fm.doe.national.ui.screens.standard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.arellomobile.mvp.InjectViewState;
import com.omega_r.libs.omegatypes.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.R;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;
import fm.doe.national.ui.screens.base.BasePresenter;
import fm.doe.national.ui.view_data.CriteriaViewData;
import fm.doe.national.ui.view_data.SubCriteriaViewData;
import io.reactivex.Observable;

@InjectViewState
public class StandardPresenter extends BasePresenter<StandardView> {

    @Inject
    DataSource dataSource;

    private SchoolAccreditationPassing accreditationResult;
    private int standardIndex;
    private int nextIndex = standardIndex + 1;;
    private int previousIndex;
    private List<Standard> standards = new ArrayList<>();
    private List<CriteriaViewData> criteriaViewDataList;

    public StandardPresenter(SchoolAccreditationPassing result) {
        MicronesiaApplication.getAppComponent().inject(this);
        accreditationResult = result;

        // TODO: handle groupStandards properly

        updateUi();
    }

    public void onQuestionStateChanged(@NonNull SubCriteriaViewData subCriteriaViewData,
                                       @NonNull SubCriteria subCriteria,
                                       @Nullable Answer answer, Answer.State newState) {

    }

    public void onNextPressed() {
        previousIndex = standardIndex;
        standardIndex = nextIndex;
        nextIndex = getNextIndex();
        updateUi();
    }

    public void onPreviousPressed() {
        nextIndex = standardIndex;
        standardIndex = previousIndex;
        nextIndex = getPrevIndex();
        updateUi();
    }

    private void updateUi() {
        getViewState().setGlobalInfo(standards.get(standardIndex).getName(), standardIndex);
        getViewState().setPrevStandard(standards.get(previousIndex).getName(), previousIndex);
        getViewState().setNextStandard(standards.get(nextIndex).getName(), nextIndex);
    }

    @NonNull
    private List<CriteriaViewData> convertToViewData(Map<? extends Criteria, Map<SubCriteria, Answer>> raw) {
        List<CriteriaViewData> result = new ArrayList<>();
        for (Map.Entry<? extends Criteria, Map<SubCriteria, Answer>> entryCriteria : raw.entrySet()) {
            CriteriaViewData.Builder viewDataBuilder = new CriteriaViewData.Builder(entryCriteria.getKey());
            for (Map.Entry<SubCriteria, Answer> entrySubCriteria: entryCriteria.getValue().entrySet()) {
                viewDataBuilder.addQuestion(entrySubCriteria.getKey(), entrySubCriteria.getValue());
            }
            result.add(viewDataBuilder.build());
        }
        return result;
    }

    private int getNextIndex() {
        return standardIndex < standards.size() - 1 ? standardIndex + 1 : 0;
    }

    private int getPrevIndex() {
        return standardIndex > 0 ? standardIndex - 1 : standards.size() - 1;
    }

    private int getAnsweredCount() {
        if (criteriaViewDataList == null) return 0;
        int count = 0;
        for (CriteriaViewData criteriaViewData: criteriaViewDataList) {
            count += criteriaViewData.getAnsweredCount();
        }
        return count;
    }
}

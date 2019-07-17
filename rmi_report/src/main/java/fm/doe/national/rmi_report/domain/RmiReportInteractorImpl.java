package fm.doe.national.rmi_report.domain;

import fm.doe.national.accreditation_core.data.model.AccreditationSurvey;
import fm.doe.national.accreditation_core.data.model.AnswerState;
import fm.doe.national.accreditation_core.data.model.Category;
import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.accreditation_core.data.model.Standard;
import fm.doe.national.accreditation_core.data.model.SubCriteria;
import fm.doe.national.report_core.domain.BaseReportInteractor;
import fm.doe.national.report_core.domain.ReportLevel;
import fm.doe.national.report_core.model.Level;
import fm.doe.national.rmi_report.model.SchoolAccreditationTallyLevel;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class RmiReportInteractorImpl extends BaseReportInteractor implements RmiReportInteractor {

    private BehaviorSubject<SchoolAccreditationTallyLevel> levelSubject = BehaviorSubject.create();

    @Override
    public void requestReports(AccreditationSurvey survey) {
        super.requestReports(survey);
        requestLevelReport(survey);
    }

    @Override
    protected void clearSubjectsHistory() {
        super.clearSubjectsHistory();
        levelSubject.onComplete();
        levelSubject = BehaviorSubject.create();
    }

    @Override
    protected Level createLevel(int completed, int total) {
        return ReportLevel.estimateLevel(completed, total);
    }

    @Override
    public Observable<SchoolAccreditationTallyLevel> getLevelObservable() {
        return levelSubject;
    }

    private void requestLevelReport(AccreditationSurvey survey) {
        Schedulers.computation().scheduleDirect(() -> {
            AccreditationSurvey clearedSurvey = getSurveyWithWorstClassroomObservation(survey);
            int[] counts = new int[SchoolAccreditationTallyLevel.MAX_CRITERIA_SUM];
            int tallyScore = 0;
            for (Category category : clearedSurvey.getCategories()) {
                for (Standard standard : category.getStandards()) {
                    for (Criteria criteria : standard.getCriterias()) {
                        int criteriaSum = getPositiveAnswersCount(criteria);
                        if (criteriaSum > 0 && criteriaSum <= SchoolAccreditationTallyLevel.MAX_CRITERIA_SUM) {
                            counts[criteriaSum - 1]++;
                            tallyScore += criteriaSum;
                        }
                    }
                }
            }
            levelSubject.onNext(new SchoolAccreditationTallyLevel(counts, tallyScore));
        });
    }

    private int getPositiveAnswersCount(Criteria criteria) {
        int criteriaSum = 0;
        for (SubCriteria subCriteria : criteria.getSubCriterias()) {
            if (subCriteria.getAnswer().getState() == AnswerState.POSITIVE) {
                criteriaSum++;
            }
        }
        return criteriaSum;
    }
}

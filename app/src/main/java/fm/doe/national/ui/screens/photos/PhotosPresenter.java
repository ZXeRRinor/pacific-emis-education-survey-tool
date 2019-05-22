package fm.doe.national.ui.screens.photos;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.data.cloud.uploader.CloudUploader;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.mutable.MutableAnswer;
import fm.doe.national.core.data.model.mutable.MutablePhoto;
import fm.doe.national.core.interactors.SurveyInteractor;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class PhotosPresenter extends BasePresenter<PhotosView> {

    private final CloudUploader cloudUploader = MicronesiaApplication.getAppComponent().getCloudUploader();
    private final SurveyInteractor interactor = MicronesiaApplication.getAppComponent().getSurveyInteractor();

    private final long categoryId;
    private final long standardId;
    private final long criteriaId;
    private final long subCriteriaId;

    private MutableAnswer answer;

    public PhotosPresenter(long categoryId, long standardId, long criteriaId, long subCriteriaId) {
        this.categoryId = categoryId;
        this.standardId = standardId;
        this.criteriaId = criteriaId;
        this.subCriteriaId = subCriteriaId;
        loadAnswer();
    }

    public void onDeletePhotoClick(Photo photo) {
        MutablePhoto mutablePhoto = MutablePhoto.toMutable(photo);
        answer.getPhotos().remove(mutablePhoto);
        getViewState().showPhotos(new ArrayList<>(answer.getPhotos()));
        addDisposable(interactor.updateAnswer(answer, categoryId, standardId, criteriaId, subCriteriaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> cloudUploader.scheduleUploading(interactor.getCurrentSurvey().getId()), this::handleError));
    }

    private void loadAnswer() {
        addDisposable(interactor.requestCriterias(categoryId, standardId)
                .flatMapObservable(Observable::fromIterable)
                .filter(criteria -> criteria.getId() == criteriaId)
                .firstElement()
                .flatMapObservable(criteria -> Observable.fromIterable(criteria.getSubCriterias()))
                .filter(subCriteria -> subCriteria.getId() == subCriteriaId)
                .firstElement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(getViewState()::hideWaiting)
                .subscribe(subCriteria -> {
                    answer = subCriteria.getAnswer();
                    getViewState().showPhotos(new ArrayList<>(answer.getPhotos()));
                }, this::handleError));
    }
}

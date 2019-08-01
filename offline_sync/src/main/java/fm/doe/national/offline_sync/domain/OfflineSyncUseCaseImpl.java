package fm.doe.national.offline_sync.domain;

import android.app.Activity;

import fm.doe.national.core.data.model.Survey;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.offline_sync.data.accessor.OfflineAccessor;
import fm.doe.national.offline_sync.ui.devices.PairedDevicesActivity;
import io.reactivex.Completable;

public class OfflineSyncUseCaseImpl implements OfflineSyncUseCase, OfflineAccessor.SyncUseCase {

    private final LifecycleListener lifecycleListener;
    private final OfflineAccessor offlineAccessor;

    private Survey targetSurvey;
    private Survey externalSurvey;

    public OfflineSyncUseCaseImpl(LifecycleListener lifecycleListener, OfflineAccessor offlineAccessor) {
        this.lifecycleListener = lifecycleListener;
        this.offlineAccessor = offlineAccessor;
        this.offlineAccessor.setSyncUseCase(this);
    }

    @Override
    public void executeAsInitiator(Survey survey) {
        targetSurvey = survey;
        selectDevice();
    }

    @Override
    public Completable executeAsReceiver() {
        return offlineAccessor.becomeAvailableToConnect();
    }

    private void selectDevice() {
        Activity activity = lifecycleListener.getCurrentActivity();

        if (activity == null) {
            return;
        }

        activity.startActivity(PairedDevicesActivity.createIntent(activity));
    }

    @Override
    public Survey getTargetSurvey() {
        return targetSurvey;
    }

    @Override
    public void setTargetSurvey(Survey targetSurvey) {
        this.targetSurvey = targetSurvey;
    }

    @Override
    public void finish() {
        offlineAccessor.disconnect();
        offlineAccessor.stopDiscoverDevices();
        offlineAccessor.becomeUnavailableToConnect();
    }

    @Override
    public void setExternalSurvey(Survey survey) {
        this.externalSurvey = survey;
    }

    @Override
    public Survey getExternalSurvey() {
        return externalSurvey;
    }
}
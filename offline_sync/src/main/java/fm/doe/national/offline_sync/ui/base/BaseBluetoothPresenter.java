package fm.doe.national.offline_sync.ui.base;

import androidx.annotation.Nullable;

import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.offline_sync.data.accessor.OfflineAccessor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseBluetoothPresenter<T extends BaseBluetoothView> extends BasePresenter<T> {

    protected final OfflineAccessor offlineAccessor;

    @Nullable
    private Action permissionAction;

    @Nullable
    private Action discoverabilityAction;

    public BaseBluetoothPresenter(OfflineAccessor offlineAccessor) {
        this.offlineAccessor = offlineAccessor;

        addDisposable(
                offlineAccessor.getPermissionsRequestSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action -> {
                    permissionAction = action;
                    getViewState().requestBluetoothPermissions();
                }, this::handleError)
        );

        addDisposable(
                offlineAccessor.getDiscoverableRequestSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action -> {
                    discoverabilityAction = action;
                    getViewState().requestBluetoothDiscoverability();
                }, this::handleError)
        );
    }

    public void onBluetoothPermissionsGranted() {
        if (permissionAction != null) {
            try {
                permissionAction.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onBecomeDiscoverable() {
        if (discoverabilityAction != null) {
            try {
                discoverabilityAction.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

package fm.doe.national.data.cloud.uploader;

import android.util.Log;
import android.util.LongSparseArray;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.impl.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class WorkerCloudUploader implements CloudUploader {

    private static final String TAG = WorkerCloudUploader.class.getName();

    private static final long ANSWER_UPDATE_TIMEOUT = 1; // minute

    private final Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();

    private final LongSparseArray<UUID> currentWorkers = new LongSparseArray<>();

    private PublishSubject<Long> publishSubject = PublishSubject.create();

    public WorkerCloudUploader() {
        publishSubject
                .throttleLast(ANSWER_UPDATE_TIMEOUT, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(passingId -> {
                    OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                            .setConstraints(constraints)
                            .setInputData(new Data.Builder()
                                    .putLong(UploadWorker.DATA_PASSING_ID, passingId)
                                    .build())
                            .build();
                    WorkManager.getInstance().enqueue(workRequest);
                    currentWorkers.put(passingId, workRequest.getId());
                }, throwable -> Log.e(TAG, "Publisher error : ", throwable));
    }

    @Override
    public void scheduleUploading(long passingId) {
        UUID foundedUUID = currentWorkers.get(passingId);
        if (foundedUUID != null) {
            WorkManager.getInstance().cancelWorkById(foundedUUID);
        }

        publishSubject.onNext(passingId);
    }
}

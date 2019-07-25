package fm.doe.national.remote_storage.data.storage;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;

import fm.doe.national.remote_storage.data.model.DriveType;
import fm.doe.national.remote_storage.data.model.GoogleDriveFileHolder;
import fm.doe.national.remote_storage.data.model.NdoeMetadata;
import fm.doe.national.remote_storage.utils.DriveQueryBuilder;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class DriveServiceHelper extends TasksRxWrapper {

    private static final String FOLDER_ROOT = "root";
    private static final String SPACE_DRIVE = "drive";
    private static final String FIELDS_TO_QUERY = "files(id, name, mimeType, appProperties)";
    private static final String FIELD_ID = "id";

    private final Drive drive;
    private final Executor executor = Executors.newCachedThreadPool();

    public DriveServiceHelper(Drive driveService) {
        drive = driveService;
    }

    public Single<String> createOrUpdateFile(final String fileName,
                                             final String content,
                                             final NdoeMetadata ndoeMetadata,
                                             @Nullable final String folderId) {
        ByteArrayContent contentStream = ByteArrayContent.fromString(DriveType.XML.getValue(), content);
        List<String> root = Collections.singletonList(folderId == null ? FOLDER_ROOT : folderId);
        String query = new DriveQueryBuilder()
                .parentId(root.get(0))
                .mimeType(DriveType.XML.getValue())
                .name(fileName)
                .build();

        return requestFiles(query)
                .flatMap(existingFiles -> {
                    if (!existingFiles.getFiles().isEmpty()) {
                        File existingFile = existingFiles.getFiles().get(0);
                        String fileId = existingFile.getId();

                        return wrapWithSingleInThreadPool(
                                () -> {
                                    drive.files()
                                            .update(fileId, ndoeMetadata.applyToDriveFile(existingFile), contentStream)
                                            .execute();
                                    return fileId;
                                },
                                fileId
                        );
                    }

                    File metadata = ndoeMetadata.applyToDriveFile(
                            new File()
                                    .setParents(root)
                                    .setMimeType(DriveType.XML.getValue())
                                    .setName(fileName)
                    );

                    return wrapWithSingleInThreadPool(() -> drive.files().create(metadata, contentStream).execute().getId(), "");
                });
    }

    public Single<String> createFolderIfNotExist(final String folderName, @Nullable final String parentFolderId) {
        List<String> root = Collections.singletonList(parentFolderId == null ? FOLDER_ROOT : parentFolderId);

        String query = new DriveQueryBuilder()
                .parentId(root.get(0))
                .mimeType(DriveType.FOLDER.getValue())
                .name(folderName)
                .build();

        return requestFiles(query)
                .flatMap(fileList -> {
                    if (!fileList.getFiles().isEmpty()) {
                        return Single.just(fileList.getFiles().get(0).getId());
                    }

                    File metadata = new File()
                            .setParents(root)
                            .setMimeType(DriveType.FOLDER.getValue())
                            .setName(folderName);

                    return wrapWithSingleInThreadPool(() -> drive.files().create(metadata).execute().getId(), "");
                });
    }

    public Single<String> readFile(final String fileId) {
        return Single.fromCallable(() -> {
            try (InputStream is = drive.files().get(fileId).executeMediaAsInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                return stringBuilder.toString();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Single<List<GoogleDriveFileHolder>> queryFiles(@Nullable final String folderId) {
        List<GoogleDriveFileHolder> googleDriveFileHolderList = new ArrayList<>();
        String parent = folderId == null ? FOLDER_ROOT : folderId;

        String query = new DriveQueryBuilder()
                .parentId(parent)
                .build();

        return requestFiles(query)
                .flatMap(fileList -> Single.fromCallable(() -> {
                    for (File file : fileList.getFiles()) {
                        googleDriveFileHolderList.add(new GoogleDriveFileHolder(file));
                    }

                    return googleDriveFileHolderList;
                }));
    }

    public Completable delete(String fileId) {
        return wrapWithCompletableInThreadPool(() -> {
            if (fileId != null) {
                drive.files().delete(fileId).execute();
            }
        });
    }

    private Single<FileList> requestFiles(String query) {
        return wrapWithSingleInThreadPool(
                () -> drive.files().list()
                        .setQ(query)
                        .setFields(FIELDS_TO_QUERY)
                        .setSpaces(SPACE_DRIVE)
                        .execute(),
                new FileList()
        );
    }

    public Single<File> uploadFileFromSource(Drive service,
                                             java.io.File source,
                                             String sourceMimeType,
                                             String targetMimeType,
                                             String targetName) {
        return Single.fromCallable(() -> {
            File fileMetadata = new File();
            fileMetadata.setName(targetName);
            fileMetadata.setMimeType(targetMimeType);

            FileContent mediaContent = new FileContent(sourceMimeType, source);
            return service.files().create(fileMetadata, mediaContent)
                    .setFields(FIELD_ID)
                    .execute();
        });
    }
}

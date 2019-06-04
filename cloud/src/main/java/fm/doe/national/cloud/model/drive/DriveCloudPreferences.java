package fm.doe.national.cloud.model.drive;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.cloud.model.CloudPreferences;

public class DriveCloudPreferences implements CloudPreferences {
    private static final String PREFS_KEY_DRIVE_FOLDER = "PREFS_KEY_DRIVE_FOLDER";
    private static final String PREFS_KEY_DRIVE_FOLDER_PATH = "PREFS_KEY_DRIVE_FOLDER_PATH";

    private final SharedPreferences sharedPreferences;

    public DriveCloudPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Nullable
    @Override
    public String getExportFolder() {
        return sharedPreferences.getString(PREFS_KEY_DRIVE_FOLDER, null);
    }

    @Nullable
    @Override
    public String getExportFolderPath() {
        return sharedPreferences.getString(PREFS_KEY_DRIVE_FOLDER_PATH, null);
    }

    @Override
    public void setExportFolder(@NonNull String folder) {
        sharedPreferences.edit().putString(PREFS_KEY_DRIVE_FOLDER, folder).apply();
    }

    @Override
    public void setExportFolderPath(@NonNull String folderPath) {
        sharedPreferences.edit().putString(PREFS_KEY_DRIVE_FOLDER_PATH, folderPath).apply();
    }

    @Nullable
    @Override
    public String getAccessToken() {
        return null;
    }

    @Override
    public void setAccessToken(@NonNull String accessToken) {
        // nothing
    }
}
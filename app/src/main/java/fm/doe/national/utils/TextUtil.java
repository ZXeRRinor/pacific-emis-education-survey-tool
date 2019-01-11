package fm.doe.national.utils;


import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import fm.doe.national.data.data_source.models.School;

public class TextUtil {
    private static final int ALPHABET_SIZE = 24;

    @NonNull
    public static String convertIntToCharsIcons(int position) {
        if (position < 0) throw new RuntimeException("Position cannot be less then 0");
        StringBuilder builder = new StringBuilder();
        fillBuilderWithCharNumbers(position, builder);
        return builder.toString();
    }

    private static void fillBuilderWithCharNumbers(int number, StringBuilder builder) {
        int offset = 1;
        int div = Math.round(number / ALPHABET_SIZE);
        int mod = number % ALPHABET_SIZE;

        if (div > 0) {
            fillBuilderWithCharNumbers(div - offset, builder);
        }

        builder.append((char)('a' + mod));
    }

    @NonNull
    public static String createSurveyFileName(School school, Date date) {
        return school.getName() + '-' + school.getId() + '-' + DateUtils.format(date) + ".xml";
    }

    @Nullable
    public static String fixLineSeparators(@Nullable String brokenString) {
        if (brokenString == null) return null;
        return brokenString.replace("\\n", "\n");
    }
}

package fm.doe.national.data.persistence;

import androidx.room.TypeConverter;

import java.util.Date;

import fm.doe.national.data.model.AnswerState;
import fm.doe.national.data.model.SurveyType;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static SurveyType fromRawSurveyType(String value) {
        return SurveyType.valueOf(value);
    }

    @TypeConverter
    public static String surveyTypeToRaw(SurveyType surveyType) {
        return surveyType.name();
    }

    @TypeConverter
    public static AnswerState fromRawAnswerState(String value) {
        return AnswerState.valueOf(value);
    }

    @TypeConverter
    public static String answerStateToRaw(AnswerState surveyType) {
        return surveyType.name();
    }
}
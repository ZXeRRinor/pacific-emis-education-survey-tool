package fm.doe.national.di.modules;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.data.model.School;
import fm.doe.national.data.model.Survey;
import fm.doe.national.data.serialization.parsers.CsvSchoolParser;
import fm.doe.national.data.serialization.parsers.Parser;
import fm.doe.national.data.serialization.parsers.XmlSurveyParser;

@Module
public class ParsersModule {

    @Provides
    @Singleton
    public Parser<Survey> provideSchoolAccreditationParser() {
        return new XmlSurveyParser();
    }

    @Provides
    @Singleton
    public Parser<List<School>> provideSchoolParser() {
        return new CsvSchoolParser();
    }

    @Provides
    @Singleton
    public Persister providePersister() {
        return new Persister(new AnnotationStrategy());
    }
}

import 'package:flutter/material.dart';
import 'package:pacific_dashboards/src/blocs/ExamsBloc.dart';
import 'package:pacific_dashboards/src/blocs/SchoolAccreditationBloc.dart';
import 'package:pacific_dashboards/src/blocs/SchoolsBloc.dart';
import 'package:pacific_dashboards/src/blocs/TeachersBloc.dart';
import 'package:pacific_dashboards/src/resources/local/FileProviderImpl.dart';
import 'package:pacific_dashboards/src/resources/Repository.dart';
import 'package:pacific_dashboards/src/resources/RepositoryImpl.dart';
import 'package:pacific_dashboards/src/resources/remote/ServerBackendProvider.dart';
import 'package:pacific_dashboards/src/resources/GlobalSettings.dart';
import 'package:shared_preferences/shared_preferences.dart';

// ignore: must_be_immutable
class InjectorWidget extends InheritedWidget {
  TeachersBloc _teachersBloc;
  SchoolsBloc _schoolsBloc;
  ExamsBloc _examsBloc;
  SchoolAccreditationBloc _schoolAccreditationBloc;
  Repository _repository;
  SharedPreferences _sharedPreferences;
  GlobalSettings _globalSettings;

  InjectorWidget({
    Key key,
    @required Widget child,
  })  : assert(child != null),
        super(key: key, child: child);

  static InjectorWidget of(BuildContext context) {
    return context.inheritFromWidgetOfExactType(InjectorWidget)
        as InjectorWidget;
  }

  @override
  bool updateShouldNotify(InjectorWidget old) => false;

  init() async {
    if (_sharedPreferences != null) {
      throw Exception("InjectorWidget::init sould be called only once");
    }

    _sharedPreferences = await SharedPreferences.getInstance();
    // var currentDataVersion = await ServerBackendProvider().fetchCurrentVersion();
    // _repository = RepositoryImpl(
    //     ServerBackendProvider(_sharedPreferences), FileProviderImpl(_sharedPreferences));
    _globalSettings = GlobalSettings(_sharedPreferences);
    // _globalSettings.currentDataVersion = currentDataVersion;

    _repository = RepositoryImpl(ServerBackendProvider(_globalSettings),
        FileProviderImpl(_sharedPreferences, _globalSettings), _sharedPreferences);
    // fetch current version
  }

  TeachersBloc get teachersBloc {
    if (_teachersBloc == null) {
      _teachersBloc = TeachersBloc(repository: _repository);
    }

    return _teachersBloc;
  }

  SchoolsBloc get schoolsBloc {
    if (_schoolsBloc == null) {
      _schoolsBloc = SchoolsBloc(repository: _repository);
    }

    return _schoolsBloc;
  }

  ExamsBloc get examsBloc {
    if (_examsBloc == null) {
      _examsBloc = ExamsBloc(repository: _repository);
    }

    return _examsBloc;
  }

  SchoolAccreditationBloc get schoolAccreditationsBloc {
    if (_schoolAccreditationBloc == null) {
      _schoolAccreditationBloc =
          SchoolAccreditationBloc(repository: _repository);
    }

    return _schoolAccreditationBloc;
  }

  SharedPreferences get sharedPreferences => _sharedPreferences;

  GlobalSettings get globalSettings => _globalSettings;
}

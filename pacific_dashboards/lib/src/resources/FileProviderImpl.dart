import 'dart:io';
import 'dart:convert';
import 'package:path_provider/path_provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../resources/FileProvider.dart';
import '../models/SchoolsModel.dart';
import '../models/TeachersModel.dart';

class FileProviderImpl extends FileProvider {
  static const _KEY_SCHOOLS = "schools";
  static const _KEY_TEACHERS = "teachers";

  SharedPreferences _sharedPreferences;

  FileProviderImpl(SharedPreferences sharedPreferences) {
    _sharedPreferences = sharedPreferences;
  }

  Future<String> get _localPath async {
    final directory = await getApplicationDocumentsDirectory();
    return directory.path;
  }

  Future<File> _createFile(String key) async {
    final path = await _localPath;
    return File('$path/$key.txt');
  }

  Future<String> _readFile(String key) async {
    try {
      final file = await _createFile(key);
      String contents = await file.readAsString();
      print(contents);
      return contents;
    } catch (e) {
      return "";
    }
  }

  Future<File> _writeFile(String key, String str) async {
    final file = await _createFile(key);
    _saveTime(key);
    return file.writeAsString(str);
  }

  Future<bool> _saveTime(String key) async {
    final todayDate = DateTime.now();
    return _sharedPreferences.setString(key + 'time', todayDate.toString());
  }

  @override
  Future<SchoolsModel> fetchSchoolsModel() async {
    return SchoolsModel.fromJson(json.decode(await _readFile(_KEY_SCHOOLS)));
  }

  @override
  Future<TeachersModel> fetchTeachersModel() async {
    return TeachersModel.fromJson(json.decode(await _readFile(_KEY_TEACHERS)));
  }

  @override
  Future<bool> saveSchoolsModel(SchoolsModel model) async {
    await _writeFile(_KEY_SCHOOLS, "");
    return true;
  }

  @override
  Future<bool> saveTeachersModel(TeachersModel model) async {
    await _writeFile(_KEY_TEACHERS, json.encode(model.toJson()));
    return true;
  }
}
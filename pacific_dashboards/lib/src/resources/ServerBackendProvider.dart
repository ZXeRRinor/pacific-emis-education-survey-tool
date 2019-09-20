import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' show Client;
import 'package:pacific_dashboards/src/models/ExamsModel.dart';
import 'package:pacific_dashboards/src/models/LookupsModel.dart';
import 'package:pacific_dashboards/src/models/SchoolsModel.dart';
import 'package:pacific_dashboards/src/models/TeachersModel.dart';
import 'package:pacific_dashboards/src/utils/Exceptions/DataNotLoadedException.dart';
import 'package:pacific_dashboards/src/resources/Provider.dart';

class ServerBackendProvider implements Provider {
  static const String BASE_URL = "https://fedemis.doe.fm";
  static const String TEACHERS_API_KEY = "warehouse/teachercount";
  static const String SCHOOLS_API_KEY = "warehouse/tableenrol";
  static const String EXAMS_API_KEY = "warehouse/examsdistrictresults";
  static const String LOOKUPS_API_KEY = "lookups/collection/core";

  Client _client = Client();

  Future<String> _request(String path) async {
    final webResponse = await _client
        .get("$BASE_URL/api/$path")
        .timeout(const Duration(minutes: 1));
    debugPrint(webResponse.body.toString());

    if (webResponse.statusCode == 200) {
      return webResponse.body;
    } else {
      throw DataNotLoadedException(path);
    }
  }

  @override
  Future<TeachersModel> fetchTeachersModel() async {
    final responseData = await _request(TEACHERS_API_KEY);
    return TeachersModel.fromJson(json.decode(responseData.toString()));
  }

  @override
  Future<SchoolsModel> fetchSchoolsModel() async {
    final responseData = await _request(SCHOOLS_API_KEY);
    return SchoolsModel.fromJson(json.decode(responseData.toString()));
  }

  @override
  Future<ExamsModel> fetchExamsModel() async {
    final responseData = await _request(EXAMS_API_KEY);
    return ExamsModel.fromJson(json.decode(responseData.toString()));
  }

  @override
  Future<LookupsModel> fetchLookupsModel() async {
    final responseData = await _request(LOOKUPS_API_KEY);
    return LookupsModel.fromJson(jsonDecode(responseData.toString()));
  }
}
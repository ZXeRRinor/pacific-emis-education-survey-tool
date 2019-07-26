class TeacherModel {
  final String schNo;
  final int surveyYear;
  final String ageGroup;
  final String districtCode;
  final String authorityCode;
  final String authorityGovt;
  final String schoolTypeCode;
  final String sector;
  final String iSCEDSubClass;
  final int numTeachersM;
  final int numTeachersF;
  final int certifiedM;
  final int certifiedF;
  final int qualifiedM;
  final int qualifiedF;
  final int certQualM;
  final int certQualF;

  TeacherModel({
    this.schNo,
    this.surveyYear,
    this.ageGroup,
    this.districtCode,
    this.authorityCode,
    this.authorityGovt,
    this.schoolTypeCode,
    this.sector,
    this.iSCEDSubClass,
    this.numTeachersM,
    this.numTeachersF,
    this.certifiedM,
    this.certifiedF,
    this.qualifiedM,
    this.qualifiedF,
    this.certQualM,
    this.certQualF,
  });

  factory TeacherModel.fromJson(Map parsedJson) {
    return TeacherModel(
      schNo: parsedJson['SchNo'],
      surveyYear: parsedJson['SurveyYear'],
      ageGroup: parsedJson['AgeGroup'],
      districtCode: parsedJson['DistrictCode'],
      authorityCode: parsedJson['AuthorityCode'],
      authorityGovt: parsedJson['AuthorityGovt'],
      schoolTypeCode: parsedJson['SchoolTypeCode'],
      sector: parsedJson['Sector'],
      iSCEDSubClass: parsedJson['ISCEDSubClass'],
      numTeachersM: parsedJson['NumTeachersM'] ?? 0,
      numTeachersF: parsedJson['NumTeachersF'] ?? 0,
      certifiedM: parsedJson['CertifiedM'] ?? 0,
      certifiedF: parsedJson['CertifiedF'] ?? 0,
      qualifiedM: parsedJson['QualifiedM'] ?? 0,
      qualifiedF: parsedJson['QualifiedF'] ?? 0,
      certQualM: parsedJson['CertQualM'] ?? 0,
      certQualF: parsedJson['CertQualF'] ?? 0,
    );
  }

  Map<String, dynamic> toJson() => {
        'SchNo': schNo,
        'SurveyYear': surveyYear,
        'AgeGroup': ageGroup,
        'DistrictCode': districtCode,
        'AuthorityCode': authorityCode,
        'AuthorityGovt': authorityGovt,
        'SchoolTypeCode': schoolTypeCode,
        'Sector': sector,
        'ISCEDSubClass': iSCEDSubClass,
        'NumTeachersM': numTeachersM,
        'NumTeachersF': numTeachersF,
        'CertifiedM': certifiedM,
        'CertifiedF': certifiedF,
        'QualifiedM': qualifiedM,
        'QualifiedF': qualifiedF,
        'CertQualM': certQualM,
        'CertQualF': certQualF,
      };
}
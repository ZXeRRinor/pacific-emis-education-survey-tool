import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:pacific_dashboards/models/emis.dart';
import 'package:pacific_dashboards/pages/home/section.dart';

class SectionsGrid extends StatelessWidget {
  SectionsGrid({@required Emis emis})
      : assert(emis != null),
        _emis = emis;

  final Emis _emis;

  @override
  Widget build(BuildContext context) {
    return GridView.count(
      physics: NeverScrollableScrollPhysics(),
      crossAxisCount: 2,
      crossAxisSpacing: 24,
      mainAxisSpacing: 24,
      padding: const EdgeInsets.fromLTRB(16, 16, 16, 32),
      childAspectRatio: 1.0,
      shrinkWrap: true,
      children: _sectionsOfEmis(_emis)
          .map((section) => _Section(section: section))
          .toList(),
    );
  }

  List<Section> _sectionsOfEmis(Emis emis) {
    switch (emis) {
      case Emis.miemis:
        return [
          Section.schools,
          Section.teachers,
          Section.exams,
          Section.schoolAccreditations
        ];
      case Emis.fedemis:
        return [
          Section.schools,
          Section.teachers,
          Section.exams,
          Section.schoolAccreditations
        ];
      case Emis.kemis:
        return [Section.schools, Section.teachers, Section.exams];
    }
    throw FallThroughError();
  }
}

class _Section extends StatelessWidget {
  final Section _section;

  const _Section({Key key, @required Section section})
      : assert(section != null),
        _section = section,
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(left: 5.0),
      decoration: BoxDecoration(
        boxShadow: <BoxShadow>[
          BoxShadow(
            color: const Color.fromRGBO(8, 36, 73, 0.4),
            blurRadius: 16.0,
            offset: const Offset(0.0, 16.0),
          ),
        ],
      ),
      child: Card(
        shape: const RoundedRectangleBorder(
          borderRadius: BorderRadius.all(const Radius.circular(16.0)),
        ),
        child: InkWell(
          splashColor: Colors.blue.withAlpha(30),
          onTap: () {
            Navigator.pushNamed(context, _section.routeName);
          },
          child: ClipRect(
            clipBehavior: Clip.hardEdge,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: <Widget>[
                Expanded(
                  flex: 5,
                  child: Container(
                    margin: const EdgeInsets.all(20.0),
                    child: SvgPicture.asset(_section.logoPath,
                        fit: BoxFit.fitHeight),
                  ),
                ),
                Expanded(
                  flex: 2,
                  child: Container(
                    margin: const EdgeInsets.only(left: 5.0, right: 5.0),
                    child: Text(
                      _section.name,
                      textAlign: TextAlign.center,
                      softWrap: true,
                      style: TextStyle(
                          fontFamily: "NotoSans",
                          fontSize: 14,
                          fontWeight: FontWeight.bold,
                          color: const Color.fromRGBO(99, 105, 109, 1.0)),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

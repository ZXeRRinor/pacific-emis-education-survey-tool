import 'package:flutter/material.dart';

import 'src/App.dart';
import 'src/ui/InjectorWidget.dart';

void main() async {
  var injector = InjectorWidget(child : App());
  await injector.init();
  runApp(injector);
}
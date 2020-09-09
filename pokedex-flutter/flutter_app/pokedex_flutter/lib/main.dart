import 'package:flutter/material.dart';
import 'package:hive/hive.dart';
import 'package:pokedexflutter/screens/home_screen.dart';
import 'package:pokedexflutter/screens/splash_screen.dart';

void main() {
  runApp(Application());
}

class Application extends StatefulWidget {
  @override
  _ApplicationState createState() => _ApplicationState();
}

class _ApplicationState extends State<Application> {
  @override
  void dispose() {
    super.dispose();
    Hive.close();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      initialRoute: SplashScreen.id,
      routes: {
        SplashScreen.id: (context) => SplashScreen(),
        HomeScreen.id: (context) => HomeScreen(),
      },
    );
  }
}

//Pokédex

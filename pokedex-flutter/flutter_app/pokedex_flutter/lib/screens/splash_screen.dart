import 'package:flutter/material.dart';
import 'package:pokedexflutter/repository.dart';
import 'package:pokedexflutter/screens/home_screen.dart';

class SplashScreen extends StatefulWidget {
  static const String id = 'splash_screen';
  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  bool doneLoading = false;

  @override
  void initState() {
    super.initState();
    start();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: !doneLoading ? CircularProgressIndicator() : Container(),
      ),
    );
  }

  void start() async {
    await Repository.instance.setupDatabase(context);
    setState(() {
      doneLoading = true;
    });
    Navigator.of(context).pushReplacementNamed(HomeScreen.id);
  }
}

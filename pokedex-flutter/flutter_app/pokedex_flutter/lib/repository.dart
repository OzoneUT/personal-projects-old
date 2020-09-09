import 'dart:convert';
import 'package:path_provider/path_provider.dart' as path_provider;
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:hive/hive.dart';
import 'package:pokedexflutter/models/species_form.dart';
import 'package:pokedexflutter/models/species_profile.dart';
import 'package:pokedexflutter/models/type.dart';

import 'utils/constants.dart';

class Repository {
  // singleton pattern setup
  Repository._privateConstructor();
  static final Repository _instance = Repository._privateConstructor();
  static Repository get instance => _instance;

  // instance fields
  Box _dataBox;
  List<String> _namesCache;
  List<String> _searchResults;

  Future setupDatabase(BuildContext context) async {
    var jsonData = jsonDecode(
      await DefaultAssetBundle.of(context).loadString('assets/list_data.json'),
    );
    String pathString =
        (await path_provider.getApplicationDocumentsDirectory()).path;
    Map<String, dynamic> params = {
      'data': jsonData,
      'path': pathString,
    };
    _namesCache = await compute(_initializeData, params);
    print(_namesCache.length);
    _initializeHive(pathString);
    _dataBox = await Hive.openBox(kDataBox);
  }

  Future asyncSearch(String searchTerm) async {
    List<String> searchResults = List<String>();
    try {
      int searchNumber = int.parse(searchTerm);
      if (searchNumber > 0 && searchNumber <= kNumSpecies) {
        searchResults.add(_namesCache[searchNumber]);
      }
    } catch (formatException) {
      for (String name in _namesCache) {
        if (name.toLowerCase().startsWith(searchTerm.toLowerCase())) {
          searchResults.add(name);
        }
      }
    }
    this._searchResults = searchResults;
  }

  // safe getters
  Box get dataBox => _dataBox;
  List<String> get namesCache => _namesCache;
}

void _initializeHive(String pathStr) {
  Hive.init(pathStr);
  Hive.registerAdapter(SpeciesProfileAdapter());
  Hive.registerAdapter(SpeciesFormAdapter());
  Hive.registerAdapter(TypeDataAdapter());
}

// code to be completed in another isolate must be in a top level function
Future<List<String>> _initializeData(params) async {
  var jsonData = params['data'];
  _initializeHive(params['path']);
  Box dataBox = await Hive.openBox(kDataBox);
  List<String> namesCache = new List();

  for (int i = 0; i < kNumSpecies; i++) {
    String pokemonID = (i + 1).toString();
    var dataFromJson = jsonData[pokemonID];
    SpeciesProfile profile = SpeciesProfile.fromJson(pokemonID, dataFromJson);
    namesCache.add(profile.baseName);
    if (dataBox.get(pokemonID) == null) {
      await dataBox.put(pokemonID, profile);
    }
  }
  await Hive.close();
  return namesCache.toList(growable: false);
}

import 'dart:collection';
import 'package:hive/hive.dart';

part 'type.g.dart';

class Type {
  static final HashMap<String, TypeData> _type = HashMap.of({
    'bug': TypeData(
      name: 'Bug',
      iconPath: '',
      color: 0xFFA8B736,
    ),
    'dark': TypeData(
      name: 'Dark',
      iconPath: '',
      color: 0xFF5E4C4C,
    ),
    'dragon': TypeData(
      name: 'Dragon',
      iconPath: '',
      color: 0xFF5559CF,
    ),
    'electric': TypeData(
      name: 'Electric',
      iconPath: '',
      color: 0xFFFAD33E,
    ),
    'fairy': TypeData(
      name: 'Fairy',
      iconPath: '',
      color: 0xFFF9A8EF,
    ),
    'fighting': TypeData(
      name: 'Fighting',
      iconPath: '',
      color: 0xFF9B4840,
    ),
    'fire': TypeData(
      name: 'Fire',
      iconPath: '',
      color: 0xFFEC8C3E,
    ),
    'flying': TypeData(
      name: 'Flying',
      iconPath: '',
      color: 0xFF94B7F4,
    ),
    'ghost': TypeData(
      name: 'Ghost',
      iconPath: '',
      color: 0xFF7D66AE,
    ),
    'grass': TypeData(
      name: 'Grass',
      iconPath: '',
      color: 0xFF75CA55,
    ),
    'ground': TypeData(
      name: 'Ground',
      iconPath: '',
      color: 0xFFC8AB63,
    ),
    'ice': TypeData(
      name: 'Ice',
      iconPath: '',
      color: 0xFF94E3F1,
    ),
    'normal': TypeData(
      name: 'Normal',
      iconPath: '',
      color: 0xFFC2B8A0,
    ),
    'poison': TypeData(
      name: 'Poison',
      iconPath: '',
      color: 0xFFA15AB1,
    ),
    'psychic': TypeData(
      name: 'Psychic',
      iconPath: '',
      color: 0xFFF5779E,
    ),
    'rock': TypeData(
      name: 'Rock',
      iconPath: '',
      color: 0xFFA8865D,
    ),
    'steel': TypeData(
      name: 'Steel',
      iconPath: '',
      color: 0xFFABA9BE,
    ),
    'water': TypeData(
      name: 'Water',
      iconPath: '',
      color: 0xFF46A9ED,
    ),
  });

  static TypeData getType(String typeStr) {
    TypeData result = _type[typeStr.toLowerCase()];
    if (result != null) {
      return result;
    }
    return null;
  }
}

@HiveType(typeId: 2)
class TypeData {
  @HiveField(0)
  final String name;

  @HiveField(1)
  final String iconPath;

  @HiveField(2)
  final int color;

  TypeData({this.name, this.iconPath, this.color});
}

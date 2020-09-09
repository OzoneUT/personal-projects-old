import 'package:hive/hive.dart';
import 'package:pokedexflutter/models/type.dart';

part 'species_form.g.dart';

@HiveType(typeId: 1)
class SpeciesForm {
  @HiveField(0)
  final String formName;

  @HiveField(1)
  final String imageUrl;

  @HiveField(2)
  final List<TypeData> types;

  SpeciesForm({this.formName, this.imageUrl, this.types});

  static List<SpeciesForm> fromJson(dynamic jsonList) {
    return (jsonList as List<dynamic>)
        .map(
          (dynamic form) => SpeciesForm(
            formName: form['form_name'],
            imageUrl: form['img_url'],
            types: (form['type'] as List<dynamic>)
                .map(
                  (dynamic typeStr) => Type.getType(typeStr as String),
                )
                .toList(),
          ),
        )
        .toList();
  }
}

import 'package:hive/hive.dart';
import 'package:pokedexflutter/models/species_form.dart';

part 'species_profile.g.dart';

@HiveType(typeId: 0)
class SpeciesProfile {
  @HiveField(0)
  final String id;

  @HiveField(1)
  final String baseName;

  @HiveField(2)
  final List<SpeciesForm> forms;

  SpeciesProfile({this.id, this.baseName, this.forms});

  factory SpeciesProfile.fromJson(String id, dynamic json) {
    return SpeciesProfile(
      id: json[id],
      baseName: json['base_name'],
      forms: SpeciesForm.fromJson(json['forms']),
    );
  }
}

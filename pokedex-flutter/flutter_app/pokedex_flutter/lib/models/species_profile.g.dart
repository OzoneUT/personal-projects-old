// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'species_profile.dart';

// **************************************************************************
// TypeAdapterGenerator
// **************************************************************************

class SpeciesProfileAdapter extends TypeAdapter<SpeciesProfile> {
  @override
  final typeId = 0;

  @override
  SpeciesProfile read(BinaryReader reader) {
    var numOfFields = reader.readByte();
    var fields = <int, dynamic>{
      for (var i = 0; i < numOfFields; i++) reader.readByte(): reader.read(),
    };
    return SpeciesProfile(
      id: fields[0] as String,
      baseName: fields[1] as String,
      forms: (fields[2] as List)?.cast<SpeciesForm>(),
    );
  }

  @override
  void write(BinaryWriter writer, SpeciesProfile obj) {
    writer
      ..writeByte(3)
      ..writeByte(0)
      ..write(obj.id)
      ..writeByte(1)
      ..write(obj.baseName)
      ..writeByte(2)
      ..write(obj.forms);
  }
}

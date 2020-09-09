// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'species_form.dart';

// **************************************************************************
// TypeAdapterGenerator
// **************************************************************************

class SpeciesFormAdapter extends TypeAdapter<SpeciesForm> {
  @override
  final typeId = 1;

  @override
  SpeciesForm read(BinaryReader reader) {
    var numOfFields = reader.readByte();
    var fields = <int, dynamic>{
      for (var i = 0; i < numOfFields; i++) reader.readByte(): reader.read(),
    };
    return SpeciesForm(
      formName: fields[0] as String,
      imageUrl: fields[1] as String,
      types: (fields[2] as List)?.cast<TypeData>(),
    );
  }

  @override
  void write(BinaryWriter writer, SpeciesForm obj) {
    writer
      ..writeByte(3)
      ..writeByte(0)
      ..write(obj.formName)
      ..writeByte(1)
      ..write(obj.imageUrl)
      ..writeByte(2)
      ..write(obj.types);
  }
}

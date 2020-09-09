// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'type.dart';

// **************************************************************************
// TypeAdapterGenerator
// **************************************************************************

class TypeDataAdapter extends TypeAdapter<TypeData> {
  @override
  final typeId = 2;

  @override
  TypeData read(BinaryReader reader) {
    var numOfFields = reader.readByte();
    var fields = <int, dynamic>{
      for (var i = 0; i < numOfFields; i++) reader.readByte(): reader.read(),
    };
    return TypeData(
      name: fields[0] as String,
      iconPath: fields[1] as String,
      color: fields[2] as int,
    );
  }

  @override
  void write(BinaryWriter writer, TypeData obj) {
    writer
      ..writeByte(3)
      ..writeByte(0)
      ..write(obj.name)
      ..writeByte(1)
      ..write(obj.iconPath)
      ..writeByte(2)
      ..write(obj.color);
  }
}

import 'dart:async';
import 'package:flutter/material.dart';
import 'package:pokedexflutter/repository.dart';

class SearchField extends StatefulWidget {
  @override
  _SearchFieldState createState() => _SearchFieldState();
}

class _SearchFieldState extends State<SearchField> {
  TextEditingController searchController = TextEditingController();
  Timer onStoppedTypingTimer;
  bool isEmpty = true;

  void _search(value) {
    Repository.instance.asyncSearch(value);
  }

  void _onChangedHandler(value) {
    setState(() {
      isEmpty = value.isEmpty;
    });
    if (!isEmpty) {
      const duration = Duration(milliseconds: 400);
      if (onStoppedTypingTimer != null) {
        setState(() => onStoppedTypingTimer.cancel());
      }
      setState(
          () => onStoppedTypingTimer = Timer(duration, () => _search(value)));
    }
  }

  @override
  void dispose() {
    super.dispose();
    searchController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      alignment: Alignment.centerRight,
      children: <Widget>[
        TextField(
          controller: searchController,
          onChanged: _onChangedHandler,
          decoration: InputDecoration(
            contentPadding: isEmpty
                ? EdgeInsets.fromLTRB(20, 20, 10, 20)
                : EdgeInsets.all(20),
            focusColor: Colors.black,
            hintText: "What Pokémon are you looking for?",
            filled: true,
            enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: BorderSide(color: Colors.transparent),
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: BorderSide(color: Colors.transparent),
            ),
            prefixIcon: Icon(
              Icons.search,
              color: Colors.black,
            ),
          ),
        ),
        !isEmpty
            ? Positioned(
                child: IconButton(
                  onPressed: () {
                    searchController.clear();
                    _onChangedHandler(searchController.text);
                    FocusScope.of(context).unfocus(); // dismiss keyboard
                  },
                  icon: Icon(
                    Icons.cancel,
                    color: Colors.black45,
                  ),
                ),
              )
            : SizedBox(),
      ],
    );
  }
}

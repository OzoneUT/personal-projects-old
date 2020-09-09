import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:pokedexflutter/components/list_item.dart';
import 'package:pokedexflutter/components/search_field.dart';
import 'package:pokedexflutter/utils/constants.dart';

class HomeScreen extends StatelessWidget {
  static const String id = 'home_screen';
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: CustomScrollView(
          physics: BouncingScrollPhysics(),
          slivers: <Widget>[
            SliverAppBar(
              elevation: 0,
              // backgroundColor: kWhiteOpacity95, // if wanted, must use pin/float
              backgroundColor: Colors.transparent,
              expandedHeight: 120.0,
              flexibleSpace: FlexibleSpaceBar(
                titlePadding: EdgeInsets.only(left: 30.0, bottom: 15.0),
                title: Text(
                  'Pokédex',
                  style: TextStyle(
                    color: Colors.black,
                    fontWeight: FontWeight.bold,
                    fontSize: 20.0,
                  ),
                ),
              ),
              iconTheme: IconThemeData(color: Colors.black),
              actions: <Widget>[
                Container(
                  margin: EdgeInsets.only(right: 20.0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: <Widget>[
                      IconButton(
                        tooltip: 'Settings',
                        onPressed: () {},
                        icon: SvgPicture.asset(
                          'assets/ui_icons/settings.svg',
                          height: 21.0,
                          width: 21.0,
                        ),
                      ),
                      IconButton(
                        tooltip: 'Sort',
                        onPressed: () {},
                        icon: Icon(Icons.format_line_spacing),
                      ),
                      IconButton(
                        tooltip: 'Filter',
                        onPressed: () {},
                        icon: SvgPicture.asset(
                          'assets/ui_icons/sliders.svg',
                          height: 21.0,
                          width: 21.0,
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
            SliverToBoxAdapter(
              child: Padding(
                padding: EdgeInsets.symmetric(horizontal: 30.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: <Widget>[
                    Text(
                      'Search for Pokémon by name or by using the National Pokédex number.',
                      style: TextStyle(color: Colors.black54, fontSize: 15.0),
                    ),
                    SizedBox(height: 20.0),
                    SearchField(),
                    SizedBox(
                      height: 20.0,
                    ),
                  ],
                ),
              ),
            ),
            SliverList(
              delegate: SliverChildBuilderDelegate(
                  (context, index) => ListItem(pokemonId: index + 1),
                  childCount: kNumSpecies),
            ),
          ],
        ),
      ),
    );
  }
}

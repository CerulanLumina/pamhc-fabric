#!venv/bin/python

import json
from pathlib import Path

tags = {}


def register_ore(tag: str, unmodifiedname: str):
    if tag not in tags:
        tags[tag] = []
    tags[tag].append(f'harvestcraft:{unmodifiedname.lower()}item')


def do_register_ores():
    # heavily regex-replaced pam's oredict register file
    register_ore('greenveggie', 'ASPARAGUS')
    register_ore('grain', 'BARLEY')
    register_ore('greenveggie', 'BROCCOLI')
    register_ore('greenveggie', 'CELERY')
    register_ore('berry', 'CRANBERRY')
    register_ore('fruit', 'CRANBERRY')
    register_ore('herb', 'GARLIC')
    register_ore('spice', 'GINGER')
    register_ore('greenveggie', 'LETTUCE')
    register_ore('grain', 'OATS')
    register_ore('rootveggie', 'PARSNIP')
    register_ore('nut', 'PEANUT')
    register_ore('fruit', 'PINEAPPLE')
    register_ore('rootveggie', 'RADISH')
    register_ore('rootveggie', 'RUTABAGA')
    register_ore('grain', 'RYE')
    register_ore('greenveggie', 'SPICELEAF')
    register_ore('spice', 'SPICELEAF')
    register_ore('rootveggie', 'SWEETPOTATO')
    register_ore('rootveggie', 'TURNIP')
    register_ore('mushroom', 'WHITEMUSHROOM')
    register_ore('greenveggie', 'ARTICHOKE')
    register_ore('pepper', 'BELLPEPPER')
    register_ore('berry', 'BLACKBERRY')
    register_ore('fruit', 'BLACKBERRY')
    register_ore('berry', 'BLUEBERRY')
    register_ore('fruit', 'BLUEBERRY')
    register_ore('greenveggie', 'BRUSSELSPROUT')
    register_ore('greenveggie', 'CABBAGE')
    register_ore('fruit', 'CACTUSFRUIT')
    register_ore('fruit', 'CANTALOUPE')
    register_ore('pepper', 'CHILIPEPPER')
    register_ore('greenveggie', 'CUCUMBER')
    register_ore('fruit', 'GRAPE')
    register_ore('fruit', 'KIWI')
    register_ore('spice', 'MUSTARD')
    register_ore('greenveggie', 'OKRA')
    register_ore('greenveggie', 'PEAS')
    register_ore('berry', 'RASPBERRY')
    register_ore('fruit', 'RASPBERRY')
    register_ore('greenveggie', 'SEAWEED')
    register_ore('fruit', 'STRAWBERRY')
    register_ore('berry', 'STRAWBERRY')
    register_ore('greenveggie', 'ZUCCHINI')
    register_ore('greenveggie', 'SPINACH')
    register_ore('spice', 'currypowder')
    register_ore('greenveggie', 'KALE')
    register_ore('fruit', 'AGAVE')
    register_ore('grain', 'AMARANTH')
    register_ore('fruit', 'ELDERBERRY')
    register_ore('berry', 'ELDERBERRY')
    register_ore('fiber', 'FLAX')
    register_ore('fruit', 'GREENGRAPE')
    register_ore('fruit', 'HUCKLEBERRY')
    register_ore('berry', 'HUCKLEBERRY')
    register_ore('fiber', 'JUTE')
    register_ore('fiber', 'KENAF')
    register_ore('grain', 'MILLET')
    register_ore('fruit', 'MULBERRY')
    register_ore('berry', 'MULBERRY')
    register_ore('grain', 'QUINOA')
    register_ore('fiber', 'SISAL')
    register_ore('fruit', 'JUNIPERBERRY')
    register_ore('berry', 'JUNIPERBERRY')
    register_ore('fruit', 'BANANA')
    register_ore('fruit', 'CHERRY')
    register_ore('fruit', 'DRAGONFRUIT')
    register_ore('fruit', 'LEMON')
    register_ore('citrus', 'LEMON')
    register_ore('fruit', 'LIME')
    register_ore('citrus', 'LIME')
    register_ore('fruit', 'MANGO')
    register_ore('fruit', 'ORANGE')
    register_ore('citrus', 'ORANGE')
    register_ore('fruit', 'PAPAYA')
    register_ore('fruit', 'PEACH')
    register_ore('fruit', 'PEAR')
    register_ore('fruit', 'PLUM')
    register_ore('fruit', 'POMEGRANATE')
    register_ore('fruit', 'STARFRUIT')
    register_ore('nut', 'WALNUT')
    register_ore('berry', 'GOOSEBERRY')
    register_ore('fruit', 'GOOSEBERRY')
    register_ore('nut', 'ALMOND')
    register_ore('fruit', 'APRICOT')
    register_ore('nut', 'CASHEW')
    register_ore('nut', 'CHESTNUT')
    register_ore('fruit', 'DATE')
    register_ore('fruit', 'FIG')
    register_ore('fruit', 'GRAPEFRUIT')
    register_ore('citrus', 'GRAPEFRUIT')
    register_ore('nut', 'PECAN')
    register_ore('fruit', 'PERSIMMON')
    register_ore('nut', 'PISTACHIO')
    register_ore('smoothie', 'melonsmoothie')
    register_ore('smoothie', 'strawberrysmoothie')
    register_ore('nutbutter', 'peanutbutter')
    register_ore('cookie', 'peanutbuttercookies')
    register_ore('cookie', 'raisincookies')
    register_ore('smoothie', 'lemonsmoothie')
    register_ore('smoothie', 'blueberrysmoothie')
    register_ore('smoothie', 'cherrysmoothie')
    register_ore('smoothie', 'papayasmoothie')
    register_ore('smoothie', 'starfruitsmoothie')
    register_ore('smoothie', 'bananasmoothie')
    register_ore('smoothie', 'orangesmoothie')
    register_ore('smoothie', 'peachsmoothie')
    register_ore('smoothie', 'limesmoothie')
    register_ore('smoothie', 'mangosmoothie')
    register_ore('smoothie', 'pomegranatesmoothie')
    register_ore('cookie', 'creamcookie')
    register_ore('smoothie', 'blackberrysmoothie')
    register_ore('smoothie', 'raspberrysmoothie')
    register_ore('smoothie', 'kiwismoothie')
    register_ore('soda', 'cherrysoda')
    register_ore('soda', 'colasoda')
    register_ore('soda', 'gingersoda')
    register_ore('soda', 'grapesoda')
    register_ore('soda', 'lemonlimesoda')
    register_ore('soda', 'orangesoda')
    register_ore('soda', 'rootbeersoda')
    register_ore('soda', 'strawberrysoda')
    register_ore('smoothie', 'apricotsmoothie')
    register_ore('smoothie', 'figsmoothie')
    register_ore('smoothie', 'grapefruitsmoothie')
    register_ore('soda', 'grapefruitsoda')
    register_ore('smoothie', 'persimmonsmoothie')
    register_ore('cookie', 'chaoscookie')
    register_ore('spice', 'currypowder')
    register_ore('smoothie', 'gooseberrysmoothie')
    register_ore('smoothie', 'gooseberrysmoothie')
    register_ore('nutbutter', 'almondbutter')
    register_ore('nutbutter', 'cashewbutter')
    register_ore('nutbutter', 'chestnutbutter')
    register_ore('nutbutter', 'pistachiobutter')
    register_ore('smoothie', 'applesmoothie')
    register_ore('smoothie', 'coconutsmoothie')
    register_ore('smoothie', 'cranberrysmoothie')
    register_ore('smoothie', 'grapesmoothie')
    register_ore('smoothie', 'pearsmoothie')
    register_ore('smoothie', 'plumsmoothie')
    register_ore('cookie', 'creepercookie')
    register_ore('cookie', 'buttercookie')
    register_ore('cookie', 'sugarcookie')
    register_ore('cookie', 'fortunecookie')
    register_ore('fruit', 'PAWPAW')
    register_ore('fruit', 'SOURSOP')
    register_ore('fruit', 'GUAVA')
    register_ore('fruit', 'LYCHEE')
    register_ore('fruit', 'PASSIONFRUIT')
    register_ore('fruit', 'RAMBUTAN')
    register_ore('grain', 'BREADFRUIT')
    register_ore('veggie', 'JACKFRUIT')
    register_ore('veggie', 'TAMARIND')
    register_ore('porkcooked', 'bbqjackfruit')
    register_ore('porkraw', 'bbqjackfruit')


do_register_ores()
Path('tags_out').mkdir(exist_ok=True)
for tag_id, tag_contents in tags.items():
    with open(f'tags_out/{tag_id}.json', 'w') as out_file:
        json.dump(tag_contents, out_file, sort_keys=True, indent=2)
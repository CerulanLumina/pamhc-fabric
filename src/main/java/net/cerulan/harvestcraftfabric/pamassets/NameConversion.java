package net.cerulan.harvestcraftfabric.pamassets;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;

/**
 * I really hate this file and it badly needs a refactor
 */
public class NameConversion {
    static final HashSet<String> tags = new HashSet<>();
    private final String value;
    private final boolean isTag;
    public NameConversion(String value, boolean isTag) {
        this.value = value;
        this.isTag = isTag;
        if (isTag) {
            tags.add(value);
        }
    }

    public String getValue() {
        return value;
    }

    public boolean isTag() {
        return isTag;
    }

    private static final HashMap<String, String> CROP_NAME_OVERRIDE = new HashMap<>();
    private static final HashMap<String, NameConversion> ORE_NAME_OVERRIDE = new HashMap<>();
    private static final HashMap<String, NameConversion> ITEM_NAME_OVERRIDE = new HashMap<>();
    private static final HashMap<Integer, NameConversion> DYE_DATA_MAP = new HashMap<>();
    private static final HashMap<String, NameConversion> DROP_DATA_MAP = new HashMap<>();

    // Why do I have 3 different functions that do different things that are the same logically
    public static NameConversion fromOre(String ore) {
        if (ORE_NAME_OVERRIDE.containsKey(ore)) return ORE_NAME_OVERRIDE.get(ore);
        else if (ore.startsWith("crop")) {
            String cropName = ore.substring(4).toLowerCase();
            String result;
            if (CROP_NAME_OVERRIDE.containsKey(cropName)) result = CROP_NAME_OVERRIDE.get(cropName);
            else result = "harvestcraft:" + cropName + "item";
            return new NameConversion(result, false);
        }
        else if (ore.startsWith("tool") || ore.startsWith("food")) {
            String toolName = ore.substring(4).toLowerCase();
            return new NameConversion("harvestcraft:" + toolName + "item", false);
        }
        else if (ore.startsWith("listAll")) {
            String tagName = "harvestcraft:" + ore.substring(7).toLowerCase();
            return new NameConversion(tagName, true);
        } else {
            return null;
        }
    }

    // Why do I have 3 different functions that do different things that are the same logically
    public static NameConversion fromItem(Identifier item) {
        return ITEM_NAME_OVERRIDE.getOrDefault(item.toString(), null);
    }

    // Why do I have 3 different functions that do different things that are the same logically
    public static NameConversion fromItemAndData(Identifier item, int data) {
        if (item.toString().equals("minecraft:dye"))
            return DYE_DATA_MAP.getOrDefault(data, null);
        else if (item.toString().equals("minecraft:double_plant") && data == 0)
            return new NameConversion("minecraft:sunflower", false);
        if (DROP_DATA_MAP.containsKey(item.toString()))
            return DROP_DATA_MAP.get(item.toString());

        return null;
    }

    // what even the actual fuck is this
    // dAtA dRiVeN mOD
    static {
        CROP_NAME_OVERRIDE.put("apple", "minecraft:apple");
        CROP_NAME_OVERRIDE.put("carrot", "minecraft:carrot");
        CROP_NAME_OVERRIDE.put("potato", "minecraft:potato");
        CROP_NAME_OVERRIDE.put("sunflower", "minecraft:sunflower");
        CROP_NAME_OVERRIDE.put("wheat", "minecraft:wheat");
        CROP_NAME_OVERRIDE.put("pumpkin", "minecraft:pumpkin");
        CROP_NAME_OVERRIDE.put("sesame", "harvestcraft:sesameseedsitem");

        ORE_NAME_OVERRIDE.put("flourEqualswheat", new NameConversion("harvestcraft:cookie_base", true));
        ORE_NAME_OVERRIDE.put("bread", new NameConversion("minecraft:bread", false));
        ORE_NAME_OVERRIDE.put("itemSalt", new NameConversion("harvestcraft:salt", true));
        ORE_NAME_OVERRIDE.put("stickWood", new NameConversion("harvestcraft:stick", true));
        ORE_NAME_OVERRIDE.put("seedMustard", new NameConversion("harvestcraft:mustardseedsitem", false));
        ORE_NAME_OVERRIDE.put("dropHoney", new NameConversion("minecraft:honey_bottle", false));
        ORE_NAME_OVERRIDE.put("dyeGreen", new NameConversion("minecraft:green_dye", false));
        ORE_NAME_OVERRIDE.put("cobblestone", new NameConversion("minecraft:cobblestone", false));
        ORE_NAME_OVERRIDE.put("plankWood", new NameConversion("minecraft:planks", true));
        ORE_NAME_OVERRIDE.put("stone", new NameConversion("minecraft:stone", false));
        ORE_NAME_OVERRIDE.put("foodHoneydrop", new NameConversion("minecraft:honey_bottle", false));
        ORE_NAME_OVERRIDE.put("foodSalmonraw", new NameConversion("minecraft:salmon", false));
        ORE_NAME_OVERRIDE.put("foodBread", new NameConversion("minecraft:bread", false));
        ORE_NAME_OVERRIDE.put("materialCloth", new NameConversion("minecraft:wool", true));
        ORE_NAME_OVERRIDE.put("string", new NameConversion("minecraft:string", false));
        ORE_NAME_OVERRIDE.put("ingotIron", new NameConversion("minecraft:iron_ingot", false));

        ITEM_NAME_OVERRIDE.put("harvestcraft:yogurtitem", new NameConversion("harvestcraft:plainyogurtitem", false));
        ITEM_NAME_OVERRIDE.put("minecraft:waterlily", new NameConversion("minecraft:lily_pad", false));
        ITEM_NAME_OVERRIDE.put("minecraft:speckled_melon", new NameConversion("minecraft:glistering_melon_slice", false));
        ITEM_NAME_OVERRIDE.put("minecraft:dye", new NameConversion("minecraft:dye", true));
        ITEM_NAME_OVERRIDE.put("minecraft:trapdoor", new NameConversion("minecraft:trapdoors", true));

        DYE_DATA_MAP.put(0, new NameConversion("minecraft:black_dye", false));
        DYE_DATA_MAP.put(1, new NameConversion("minecraft:red_dye", false));
        DYE_DATA_MAP.put(2, new NameConversion("minecraft:green_dye", false));
        DYE_DATA_MAP.put(3, new NameConversion("minecraft:brown_dye", false));
        DYE_DATA_MAP.put(4, new NameConversion("minecraft:blue_dye", false));
        DYE_DATA_MAP.put(5, new NameConversion("minecraft:purple_dye", false));
        DYE_DATA_MAP.put(6, new NameConversion("minecraft:cyan_dye", false));
        DYE_DATA_MAP.put(7, new NameConversion("minecraft:light_gray_dye", false));
        DYE_DATA_MAP.put(8, new NameConversion("minecraft:gray_dye", false));
        DYE_DATA_MAP.put(9, new NameConversion("minecraft:pink_dye", false));
        DYE_DATA_MAP.put(10, new NameConversion("minecraft:lime_dye", false));
        DYE_DATA_MAP.put(11, new NameConversion("minecraft:yellow_dye", false));
        DYE_DATA_MAP.put(12, new NameConversion("minecraft:light_blue_dye", false));
        DYE_DATA_MAP.put(13, new NameConversion("minecraft:magenta_dye", false));
        DYE_DATA_MAP.put(14, new NameConversion("minecraft:orange_dye", false));
        DYE_DATA_MAP.put(15, new NameConversion("minecraft:white_dye", false));
        DYE_DATA_MAP.put(32767, new NameConversion("minecraft:dye", true));

        DROP_DATA_MAP.put("minecraft:golden_apple", new NameConversion("minecraft:golden_apple", false));
        DROP_DATA_MAP.put("minecraft:coal", new NameConversion("minecraft:coal", false));
        DROP_DATA_MAP.put("minecraft:stone_pressure_plate", new NameConversion("minecraft:stone_pressure_plate", false));
        DROP_DATA_MAP.put("minecraft:sapling", new NameConversion("minecraft:saplings", true));
        DROP_DATA_MAP.put("minecraft:piston", new NameConversion("minecraft:piston", false));
        DROP_DATA_MAP.put("minecraft:wool", new NameConversion("minecraft:wool", true));
        DROP_DATA_MAP.put("minecraft:chest", new NameConversion("c:chest", true));
    }

}

package net.cerulan.harvestcraftfabric.pamassets;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;

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

    public static NameConversion fromItem(Identifier item) {
        return ITEM_NAME_OVERRIDE.getOrDefault(item.toString(), null);
    }

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
        ORE_NAME_OVERRIDE.put("cobblestone", new NameConversion("minecraft:stone_crafting_materials", true));
        ORE_NAME_OVERRIDE.put("plankWood", new NameConversion("minecraft:planks", true));
        ORE_NAME_OVERRIDE.put("stone", new NameConversion("minecraft:stone", false));
        ORE_NAME_OVERRIDE.put("foodHoneydrop", new NameConversion("minecraft:honey_bottle", false));
        ORE_NAME_OVERRIDE.put("foodSalmonraw", new NameConversion("minecraft:salmon", false));
        ORE_NAME_OVERRIDE.put("foodBread", new NameConversion("minecraft:bread", false));
        ORE_NAME_OVERRIDE.put("materialCloth", new NameConversion("minecraft:wool", true));
        ORE_NAME_OVERRIDE.put("string", new NameConversion("minecraft:string", false));

        ITEM_NAME_OVERRIDE.put("harvestcraft:yogurtitem", new NameConversion("harvestcraft:plainyogurtitem", false));
        ITEM_NAME_OVERRIDE.put("minecraft:waterlily", new NameConversion("minecraft:lily_pad", false));
        ITEM_NAME_OVERRIDE.put("minecraft:speckled_melon", new NameConversion("minecraft:glistering_melon_slice", false));
    }

}

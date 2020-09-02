package net.cerulan.harvestcraftfabric.config;

import net.cerulan.harvestcraftfabric.Harvestcraftfabric;
import net.minecraft.item.FoodComponent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FoodsConfig {

    public FoodsConfig() {
    }

    private Map<String, FoodObject> food = new HashMap<>();
    private FoodObject cropResultFood;

    public FoodObject getFood(String id) {
        if (!food.containsKey(id)) {
            Harvestcraftfabric.LOGGER.error("Foods config did not contain " + id + ", using fallback.");
            return FALLBACK;
        }
        return food.get(id);
    }

    public FoodObject getCropResultFood() {
        return cropResultFood;
    }

    public void validateConfig(HashSet<String> registered) {
        food.keySet().stream().filter(s -> !registered.contains(s.substring(0, s.length() - 4)))
                .forEach(notRegistered -> {
                    Harvestcraftfabric.LOGGER.warn("Unused config entry " + notRegistered);
                });
    }

    public static class FoodObject {
        private int hunger;
        private float saturation;
        private boolean drink = false;

        public boolean isDrink() {
            return drink;
        }

        public float getSaturation() {
            return saturation;
        }

        public int getHunger() {
            return hunger;
        }

        public FoodComponent toFoodComponent() {
            return new FoodComponent.Builder()
                    .hunger(this.hunger)
                    .saturationModifier(this.saturation)
                    .build();
        }
    }

    private static final FoodObject FALLBACK = new FoodObject();

    static {
        FALLBACK.drink = false;
        FALLBACK.hunger = 1;
        FALLBACK.saturation = 0.1f;
    }

}

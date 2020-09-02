package net.cerulan.harvestcraftfabric.pamassets;

import java.util.HashSet;

public class PamContent {
    private HashSet<String> crops;
    private HashSet<String> foods;
    private HashSet<String> tools;
    private HashSet<String> ingredients;
    private HashSet<String> fruits;
    private HashSet<String> basicItems;

    public HashSet<String> getCrops() {
        return crops;
    }

    public HashSet<String> getFoods() {
        return foods;
    }

    public HashSet<String> getTools() {
        return tools;
    }

    public HashSet<String> getFruits() {
        return fruits;
    }

    public HashSet<String> getIngredients() {
        return ingredients;
    }

    public HashSet<String> getBasicItems() {
        return basicItems;
    }
}

package net.cerulan.harvestcraftfabric.pamassets;

import java.util.HashMap;
import java.util.HashSet;

public class PamContent {
    private HashSet<String> crops;
    private HashSet<String> foods;
    private HashSet<String> tools;
    private HashSet<String> ingredients;
    private HashSet<String> fruits;
    private HashSet<String> basicItems;
    private HashSet<String> cake;
    private HashMap<String, String> specialModels;
    private HashSet<String> gardens;

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

    public HashMap<String, String> getSpecialModels() {
        return specialModels;
    }

    public HashSet<String> getCake() {
        return cake;
    }

    public HashSet<String> getGardens() {
        return gardens;
    }
}

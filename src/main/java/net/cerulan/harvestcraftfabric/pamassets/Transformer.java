package net.cerulan.harvestcraftfabric.pamassets;

@FunctionalInterface
public interface Transformer<T> {

    T transform(T value);

}

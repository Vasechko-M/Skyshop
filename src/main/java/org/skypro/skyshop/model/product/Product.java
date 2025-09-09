package org.skypro.skyshop.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Objects;
import java.util.UUID;

public abstract class Product implements Searchable {
    private final UUID id;
    private String name;
    private int price;

    public Product(UUID id, String name, int price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название продукта не может быть null, пустым или содержать только пробелы");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public Product(String name, int price) {
        this(UUID.randomUUID(), name, price);
    }
    @Override
    @JsonIgnore
    public String getSearchTerm() {
        return getName();
    }

    @Override
    @JsonIgnore
    public String getContentType() {
        return "PRODUCT";
    }

    public String getName() {
        return name;
    }

    public abstract int getPrice();
    public abstract String toString();

    public abstract boolean isSpecial();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    @Override
    public UUID getId() {
        return id;
    }
}

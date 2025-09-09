package org.skypro.skyshop.model.product;

import java.util.Objects;
import java.util.UUID;

public class SimpleProduct extends Product{
    private int price;
    public SimpleProduct(UUID id, String name, int price) {
        super(id, name, price);
        if (price < 1) {
            throw new IllegalArgumentException("Цена должна быть больше либо равна 1 рублю");
        }
        this.price = price;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return  getName() + " " +
                "Цена: " + price;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleProduct)) return false;
        SimpleProduct that = (SimpleProduct) o;
        return price == that.price && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), price);
    }
}

package org.skypro.skyshop.model.product;

import java.util.UUID;

public class FixPriceProduct extends Product {
    private static final int PRICE = 100;

    public FixPriceProduct(UUID id, String name) {

        super(id, name, PRICE);
    }
    public int getPrice() {
        return PRICE;
    }
    @Override
    public String toString() {
        return getName() + " " +
                "Фиксированная цена: " + PRICE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
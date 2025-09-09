package org.skypro.skyshop.model.product;

import java.util.UUID;

public class DiscountedProduct extends Product {
    private int basePrice;
    private int discount;

    public DiscountedProduct(UUID id, String name, int basePrice, int discount) {
        super(id, name, basePrice);
        if (basePrice < 1) {
            throw new IllegalArgumentException("Базовая цена должна быть больше либо равна 1 рублю");
        }
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Процент скидки должен быть в диапазоне от 0 до 100 включительно");
        }
        this.getPrice();
        this.basePrice = basePrice;
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    @Override
    public int getPrice() {
        return basePrice - (basePrice * discount / 100);
    }

    @Override
    public String toString() {
        return getName() + " " +
                "Цена со скидкой: " + getPrice() + " " +
                "Скидка: " + discount + " %";
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}

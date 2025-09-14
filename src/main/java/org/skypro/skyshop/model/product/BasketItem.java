package org.skypro.skyshop.model.product;

public final class BasketItem {

    private final Product product;
    private final int count;

    public BasketItem(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }
}

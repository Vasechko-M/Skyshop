package org.skypro.skyshop.service;

import org.skypro.skyshop.model.product.BasketItem;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.ProductBasket;
import org.skypro.skyshop.model.product.UserBasket;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class BasketService {

    private final ProductBasket productBasket;
    private final StorageService storageService;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }
    public void addProductToBasket(UUID id) {
        Optional<Product> productOptional = storageService.getProductById(id);
        if (productOptional.isEmpty()) {
            throw new IllegalArgumentException("Товар с id " + id + " не найден");
        }

        productBasket.addProduct(id);
    }
    public UserBasket getUserBasket() {
        Map<UUID, Integer> basketMap = productBasket.getProducts();

        List<BasketItem> items = basketMap.entrySet().stream()
                .map(entry -> {
                    UUID id = entry.getKey();
                    int count = entry.getValue();
                    Product product = storageService.getProductById(id)
                            .orElseThrow(() -> new IllegalStateException("Продукт с id " + id + " не найден"));
                    return new BasketItem(product, count);
                })
                .toList();

        return new UserBasket(items);
    }
}
package org.skypro.skyshop.model.product;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@SessionScope
public class ProductBasket {

    private final Map<UUID, Integer> products = new HashMap<>();
    public void addProduct(UUID id) {
        products.merge(id, 1, Integer::sum);
    }
    public void removeProduct(UUID id) {
        if (products.containsKey(id)) {
            int count = products.get(id);
            if (count > 1) {
                products.put(id, count - 1);
            } else {
                products.remove(id);
            }
        }
    }

    public void clear() {
        products.clear();
    }
    public Map<UUID, Integer> getProducts() {
        return Collections.unmodifiableMap(products);
    }
}
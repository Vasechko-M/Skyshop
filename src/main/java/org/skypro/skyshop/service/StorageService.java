package org.skypro.skyshop.service;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class StorageService {
    private final Map<UUID, Product> productStorage;
    private final Map<UUID, Article> articleStorage;

    public StorageService() {
        this.productStorage = new HashMap<>();
        this.articleStorage = new HashMap<>();
        databaseArticle();
        databaseProduct();
    }
    private void databaseArticle() {
        Stream.of(
                new Article(UUID.randomUUID(), "Война и мир 1", "Школьная литература"),
                new Article(UUID.randomUUID(), "Война и мир 2", "Книга для вечернего чтения"),
                new Article(UUID.randomUUID(), "Книга по Java", "Учебник по Java")
        ).forEach(article -> articleStorage.put(article.getId(), article));
    }
    private void databaseProduct() {
        Stream.of(
                new SimpleProduct(UUID.randomUUID(),"Велосипед", 1285),
                new DiscountedProduct(UUID.randomUUID(),"Порошок зубной",30,18),
                new FixPriceProduct(UUID.randomUUID(),"Мыло для рук")
        ).forEach(product -> productStorage.put(product.getId(), product));
    }

    public Collection<Article> getArticles() {
        return articleStorage.values();
    }

    public Collection<Product> getProducts() {
        return productStorage.values();
    }
    public Collection<Searchable> getAllSearchables() {
        Collection<Searchable> combined = new ArrayList<>();
        combined.addAll(getArticles());
        combined.addAll(getProducts());
        return combined;
    }
    public Product getProductById(UUID id) {
        Product product = productStorage.get(id);
        if (product == null) {
            throw new NoSuchProductException("Продукт с id " + id + " не найден");
        }
        return product;
    }
}

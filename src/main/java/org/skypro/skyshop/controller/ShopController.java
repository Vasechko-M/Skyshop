package org.skypro.skyshop.controller;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.UserBasket;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.service.SearchService;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/shop")
public class ShopController {
    private final StorageService storageService;
    private final SearchService searchService;
    private final BasketService basketService;

    public ShopController(StorageService storageService, SearchService searchService, BasketService basketService) {
        this.storageService = storageService;
        this.searchService = searchService;
        this.basketService = basketService;
    }

    @GetMapping("/products")
    public Collection<Product> getAllProducts() {
        return storageService.getProducts();
    }


    @GetMapping("/articles")
    public Collection<Article> getAllArticles() {
        return storageService.getArticles();
    }
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("pattern") String pattern) {
        Collection<SearchResult> results = searchService.search(pattern);
        if (results.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Товар не найден"));
        }
        return ResponseEntity.ok(results);
    }
    @GetMapping("/basket/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable UUID id) {
        Product product = storageService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    @GetMapping("/basket/add/{id}")
    public ResponseEntity<String> addProduct(@PathVariable("id") UUID id) {
        try {
            basketService.addProductToBasket(id);
            return ResponseEntity.ok("Продукт успешно добавлен");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ошибка: " + e.getMessage());
        }
    }

    @GetMapping("/basket")
    public UserBasket getUserBasket() {
        return basketService.getUserBasket();
    }
    @GetMapping("/basket/remove/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable UUID id) {
        try {
            basketService.removeProductFromBasket(id);
            return ResponseEntity.ok("Продукт успешно удалён");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ошибка: " + e.getMessage());
        }
    }
    @GetMapping("/clear")
    public ResponseEntity<String> clearBasket() {
        basketService.clearBasket();
        return ResponseEntity.ok("Корзина очищена");
    }
}

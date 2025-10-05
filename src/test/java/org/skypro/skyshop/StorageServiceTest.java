package org.skypro.skyshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.Searchable;
import org.skypro.skyshop.service.StorageService;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StorageServiceTest {

    private StorageService storageService;

    @BeforeEach
    void setUp() {
        storageService = new StorageService();
    }

    @Test
    void databaseInitialization_NotEmpty() {
        assertFalse(storageService.getArticles().isEmpty(), "articles не должны быть пусты после инициализации");
        assertFalse(storageService.getProducts().isEmpty(), "products не должны быть пусты после инициализации");
    }

    @Test
    void getArticles_ReturnsExpected() {
        Collection<Article> articles = storageService.getArticles();
        assertNotNull(articles, "articles не должны быть null");
        assertTrue(articles.stream().anyMatch(a -> a.getName().contains("Война и мир")), "Должна быть статья с именем, содержащим 'Война и мир'");
        assertTrue(articles.stream().anyMatch(a -> a.getName().contains("Java")), "Должна быть статья с именем, содержащим 'Java'");
    }

    @Test
    void getProducts_ReturnsExpected() {
        Collection<Product> products = storageService.getProducts();
        assertNotNull(products, "products не должны быть null");
        assertTrue(products.stream().anyMatch(p -> p.getName().equalsIgnoreCase("Велосипед")), "Должен быть продукт 'Велосипед'");
        assertTrue(products.stream().anyMatch(p -> p.getName().toLowerCase().contains("java")), "Должен быть продукт с 'java' в имени");
    }

    @Test
    void getAllSearchables_ReturnsArticlesAndProducts() {
        Collection<Searchable> searchables = storageService.getAllSearchables();
        assertNotNull(searchables, "searchables не должны быть null");
        assertTrue(searchables.stream().anyMatch(s -> s instanceof Article), "Должны быть статьи среди результатов");
        assertTrue(searchables.stream().anyMatch(s -> s instanceof Product), "Должны быть продукты среди результатов");
        assertEquals(storageService.getArticles().size() + storageService.getProducts().size(), searchables.size(), "Общее количество должно совпадать");
    }

    @Test
    void getProductById_ExistingId_ReturnsProduct() {
        Product product = storageService.getProducts().iterator().next();
        UUID id = product.getId();
        Product foundProduct = storageService.getProductById(id);
        assertEquals(product, foundProduct, "Полученный продукт должен совпадать с ожидаемым");
    }

    @Test
    void getProductById_NonExistingId_ThrowsException() {
        UUID randomId = UUID.randomUUID();
        NoSuchProductException thrown = assertThrows(
                NoSuchProductException.class,
                () -> storageService.getProductById(randomId),
                "Ожидается исключение NoSuchProductException"
        );
        assertTrue(thrown.getMessage().contains(randomId.toString()), "Сообщение об ошибке должно содержать id");
    }
}

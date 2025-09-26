package org.skypro.skyshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.search.Searchable;
import org.skypro.skyshop.service.StorageService;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.service.SearchService;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;


import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    public class SearchServiceTest {

        @Mock
        private StorageService storageService;

        @InjectMocks
        private SearchService searchService;

        private Article article1;
        private SimpleProduct product1;

        @BeforeEach
        void setUp() {
            article1 = new Article(UUID.randomUUID(), "Java Programming", "Учебник по Java");
            product1 = new SimpleProduct(UUID.randomUUID(), "Велосипед", 1285);
        }


        @Test
        void search_NoObjectsInStorage_ReturnsEmpty() {
             when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

            Collection<SearchResult> results = searchService.search("java");

            assertTrue(results.isEmpty(), "Результат поиска должен быть пустым при отсутствии объектов");
            verify(storageService, times(1)).getAllSearchables();
        }


        @Test
        void search_ObjectsPresentButNoMatch_ReturnsEmpty() {
            List<Searchable> items = List.of(article1, product1);
            when(storageService.getAllSearchables()).thenReturn(items);

            Collection<SearchResult> results = searchService.search("наенгос");

            assertTrue(results.isEmpty(), "Результат поиска должен быть пустым при отсутствии совпадений");
        }


        @Test
        void search_MatchFound_ReturnsResults() {
            List<Searchable> items = List.of(article1, product1);
            when(storageService.getAllSearchables()).thenReturn(items);

            Collection<SearchResult> results = searchService.search("java");

            assertFalse(results.isEmpty(), "Результат поиска не должен быть пустым, т.к. есть совпадение");
            assertTrue(results.stream().anyMatch(r -> r.getName().equals(article1.getName())),
                    "Должен найтись объект с названием " + article1.getName());
        }



        @Test
        void getProductById_ProductExists_ReturnsProduct() {
            when(storageService.getProductById(product1.getId())).thenReturn(product1);

            Product result = storageService.getProductById(product1.getId());

            assertEquals(product1, result);
        }
        @Test
        void getProductById_ProductDoesNotExist_ThrowsException() {
            UUID unknownId = UUID.randomUUID();
            when(storageService.getProductById(unknownId)).thenThrow(new RuntimeException("Продукт с id " + unknownId + " не найден"));


            RuntimeException ex = assertThrows(RuntimeException.class, () -> storageService.getProductById(unknownId));
            assertTrue(ex.getMessage().contains("не найден"));
        }
        @Test
        void search_ShouldBeCaseInsensitive() {
            List<Searchable> items = List.of(article1, product1);
            when(storageService.getAllSearchables()).thenReturn(items);

            Collection<SearchResult> resultsLower = searchService.search("java");
            Collection<SearchResult> resultsUpper = searchService.search("JAVA");
            Collection<SearchResult> resultsMixed = searchService.search("JaVa");

            assertFalse(resultsLower.isEmpty(), "Поиск с нижним регистром должен найти результат");
            assertFalse(resultsUpper.isEmpty(), "Поиск с верхним регистром должен найти результат");
            assertFalse(resultsMixed.isEmpty(), "Поиск с смешанным регистром должен найти результат");

            assertTrue(resultsLower.stream().anyMatch(r -> r.getName().equals(article1.getName())),
                    "Результат с нижним регистром содержит искомое");
            assertTrue(resultsUpper.stream().anyMatch(r -> r.getName().equals(article1.getName())),
                    "Результат с верхним регистром содержит искомое");
            assertTrue(resultsMixed.stream().anyMatch(r -> r.getName().equals(article1.getName())),
                    "Результат со смешанным регистром содержит искомое");
        }
        @Test
        void search_MultipleMatches_ReturnsAllMatchingResults() {
            Article articleJava1 = new Article(UUID.randomUUID(), "Книга по Java", "Учебник по Java");
            Article articleJava2 = new Article(UUID.randomUUID(), "Книга по Java обновления", "Учебник по Java с обновлениями");
            Article articlePython = new Article(UUID.randomUUID(), "Python Guide", "Learn Python");
            SimpleProduct productBike = new SimpleProduct(UUID.randomUUID(), "Велосипед", 1285);
            DiscountedProduct productJavaBook = new DiscountedProduct(UUID.randomUUID(), "Java не для всех", 3000,18);
            FixPriceProduct productSoap = new FixPriceProduct(UUID.randomUUID(), "Мыло для рук");

            List<Searchable> items = List.of(
                    articleJava1, articleJava2, articlePython,
                    productBike, productJavaBook, productSoap
            );

            when(storageService.getAllSearchables()).thenReturn(items);

            Collection<SearchResult> results = searchService.search("java");

            assertFalse(results.isEmpty(), "Результат поиска не должен быть пустым");

            Set<String> resultNames = new HashSet<>();
            for (SearchResult r : results) {
                resultNames.add(r.getName());
            }

            assertTrue(resultNames.contains(articleJava1.getName()), "Должен содержать " + articleJava1.getName());
            assertTrue(resultNames.contains(articleJava2.getName()), "Должен содержать " + articleJava2.getName());
            assertTrue(resultNames.contains(productJavaBook.getName()), "Должен содержать " + productJavaBook.getName());

            assertFalse(resultNames.contains(articlePython.getName()), "Не должен содержать " + articlePython.getName());
            assertFalse(resultNames.contains(productBike.getName()), "Не должен содержать " + productBike.getName());
            assertFalse(resultNames.contains(productSoap.getName()), "Не должен содержать " + productSoap.getName());
        }

    }

package org.skypro.skyshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
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
//            Searchable someSearchableObject = mock(Searchable.class); для проверки что бы тест упал
//
//            when(someSearchableObject.getId()).thenReturn(UUID.randomUUID());
//            when(someSearchableObject.getName()).thenReturn("Test Name");
//            when(someSearchableObject.getContentType()).thenReturn("TestType");
//            when(someSearchableObject.getSearchTerm()).thenReturn("java");
//            when(storageService.getAllSearchables()).thenReturn(Collections.singletonList(someSearchableObject));

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
           // List<Searchable> items = List.of(product1); для проверки что бы тест упал
            when(storageService.getAllSearchables()).thenReturn(items);

            Collection<SearchResult> results = searchService.search("java");
            //Collection<SearchResult> results = searchService.search("Велосипед"); для проверки что бы тест упал, с нижним сообщением

            assertFalse(results.isEmpty(), "Результат поиска не должен быть пустым, т.к. есть совпадение");
            assertTrue(results.stream().anyMatch(r -> r.getName().equals(article1.getName())),
                    "Должен найтись объект с названием " + article1.getName());
        }



        @Test
        void getProductById_ProductExists_ReturnsProduct() {
            when(storageService.getProductById(product1.getId())).thenReturn(product1);
            //when(storageService.getProductById(product1.getId())).thenReturn(null); тут пробую,что бы null вернулся вместо объекта

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

    }

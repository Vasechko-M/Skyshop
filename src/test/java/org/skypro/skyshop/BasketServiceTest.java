package org.skypro.skyshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.model.product.BasketItem;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.UserBasket;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.StorageService;
import org.skypro.skyshop.model.product.ProductBasket;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BasketServiceTest {

    private ProductBasket productBasketMock;
    private StorageService storageServiceMock;
    private BasketService basketService;

    @BeforeEach
    void setUp() {
        productBasketMock = mock(ProductBasket.class);
        storageServiceMock = mock(StorageService.class);
        basketService = new BasketService(productBasketMock, storageServiceMock);
    }

    @Test
    void addProductToBasket_whenProductExists_callsAddProduct() {
        UUID productId = UUID.randomUUID();
        Product productMock = mock(Product.class);


        when(storageServiceMock.getProductById(productId)).thenReturn(productMock);

        basketService.addProductToBasket(productId);

        verify(productBasketMock, times(1)).addProduct(productId);
    }

    @Test
    void addProductToBasket_whenProductDoesNotExist_throwsNoSuchProductException() {
        UUID productId = UUID.randomUUID();

        when(storageServiceMock.getProductById(productId)).thenThrow(new NoSuchProductException("Not found"));

        assertThrows(NoSuchProductException.class, () -> basketService.addProductToBasket(productId));

        verify(productBasketMock, never()).addProduct(any());
    }

    @Test
    void getUserBasket_whenBasketEmpty_returnsEmptyUserBasket() {
        when(productBasketMock.getProducts()).thenReturn(Map.of());

        UserBasket userBasket = basketService.getUserBasket();

        assertNotNull(userBasket);
        assertTrue(userBasket.getItems().isEmpty());
        assertEquals(0, userBasket.getTotal());
    }

    @Test
    void getUserBasket_whenBasketHasItems_returnsUserBasketWithCorrectData() {
        UUID productId = UUID.randomUUID();
        int quantity = 2;
        int price = 100;
        Product productMock = mock(Product.class);

        when(productBasketMock.getProducts()).thenReturn(Map.of(productId, quantity));
        when(storageServiceMock.getProductById(productId)).thenReturn(productMock);
        when(productMock.getPrice()).thenReturn(price);

        UserBasket userBasket = basketService.getUserBasket();

        assertNotNull(userBasket);
        assertEquals(1, userBasket.getItems().size());

        BasketItem item = userBasket.getItems().get(0);
        assertEquals(productMock, item.getProduct());
        assertEquals(quantity, item.getCount());
        assertEquals(price * quantity, userBasket.getTotal());
    }
    @Test
    void removeProductFromBasket_whenProductExists_removesProduct() {
        UUID productId = UUID.randomUUID();
        when(storageServiceMock.getProductById(productId)).thenReturn(mock(Product.class));
        when(productBasketMock.getProducts()).thenReturn(Map.of(productId, 1));

        basketService.removeProductFromBasket(productId);

        verify(productBasketMock, times(1)).removeProduct(productId);
    }

    @Test
    void removeProductFromBasket_whenProductNotInBasket_throwsNoSuchProductException() {
        UUID productId = UUID.randomUUID();
        when(storageServiceMock.getProductById(productId)).thenReturn(mock(Product.class));
        when(productBasketMock.getProducts()).thenReturn(Map.of());

        assertThrows(NoSuchProductException.class, () -> basketService.removeProductFromBasket(productId));
        verify(productBasketMock, never()).removeProduct(any());
    }
    @Test
    void clearBasket_callsClearOnProductBasket() {
        basketService.clearBasket();
        verify(productBasketMock, times(1)).clear();
    }
}

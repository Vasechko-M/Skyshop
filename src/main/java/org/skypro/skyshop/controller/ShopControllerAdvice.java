package org.skypro.skyshop.controller;

import org.skypro.skyshop.exception.NoSuchProductException;
import org.skypro.skyshop.exception.ShopError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

    @ControllerAdvice
    public class ShopControllerAdvice {

        @ExceptionHandler(NoSuchProductException.class)
        public ResponseEntity<ShopError> handleNoSuchProductException(NoSuchProductException e) {
            ShopError error = new ShopError("PRODUCT_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

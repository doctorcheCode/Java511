package ru.academy.bakery.model;

import java.math.BigDecimal;

public record Product(

        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        boolean isAvailable
) { }

package ru.academy.bakery.service;

import ru.academy.bakery.model.Product;

import java.math.BigDecimal;
import java.time.*;

public class ProductService {

    public Product[] getProducts() {

        double discount;
        LocalTime now = LocalTime.now();
        if (now.isAfter(LocalTime.of(8, 0)) && now.isBefore(LocalTime.of(10, 0))) {
            discount = 0.9;
        } else if (now.isAfter(LocalTime.of(20, 0)) && now.isBefore(LocalTime.of(22, 0))) {
            discount = 0.6;
        } else {
            discount = 1.0;
        }

        Product bun = new Product(
                "Булочка с корицей",
                "Нежная сдобная булочка с ароматной корицей и глазурью.",
                BigDecimal.valueOf(220 * discount),
                "resources/images/cinnamon_bun.jpg",
                true);

        Product croissant = new Product(
                "Круассан классический",
                "Хрустящий снаружи, воздушный внутри &mdash; как в лучших парижских пекарнях.",
                BigDecimal.valueOf(180 * discount),
                "resources/images/croissant.jpg",
                true);

        Product bread = new Product(
                "Хлеб на закваске",
                "Плотный мякиш, хрустящая корочка и глубокий хлебный аромат.",
                BigDecimal.valueOf(350 * discount),
                "resources/images/hero.jpg",
                false);

        Product[] products = {bun, croissant, bread};
        return products;
    }
}

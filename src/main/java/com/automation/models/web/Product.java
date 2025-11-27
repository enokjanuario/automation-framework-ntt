package com.automation.models.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Modelo que representa um Produto para testes Web.
 *
 * Arquitetura:
 * - Representa dados de produto para cenarios de busca e carrinho
 * - Campos mapeados para elementos da pagina Netshoes
 */
public class Product {

    private String id;
    private String name;
    private String brand;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String size;
    private String color;
    private Integer quantity;
    private String imageUrl;
    private boolean available;

    public Product() {
    }

    public Product(String id, String name, String brand, BigDecimal price, BigDecimal originalPrice,
                   String size, String color, Integer quantity, String imageUrl, boolean available) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.originalPrice = originalPrice;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.available = available;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isAvailable() {
        return available;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Verifica se o produto esta em promocao.
     *
     * @return true se preco atual e menor que original
     */
    public boolean isOnSale() {
        return originalPrice != null && price != null &&
                price.compareTo(originalPrice) < 0;
    }

    /**
     * Calcula o percentual de desconto.
     *
     * @return Percentual de desconto ou zero
     */
    public BigDecimal getDiscountPercentage() {
        if (!isOnSale()) {
            return BigDecimal.ZERO;
        }
        BigDecimal difference = originalPrice.subtract(price);
        return difference.divide(originalPrice, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Obtem o valor total considerando quantidade.
     *
     * @return Valor total
     */
    public BigDecimal getTotalPrice() {
        if (price == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    /**
     * Builder class for Product.
     */
    public static class ProductBuilder {
        private String id;
        private String name;
        private String brand;
        private BigDecimal price;
        private BigDecimal originalPrice;
        private String size;
        private String color;
        private Integer quantity;
        private String imageUrl;
        private boolean available;

        ProductBuilder() {
        }

        public ProductBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder originalPrice(BigDecimal originalPrice) {
            this.originalPrice = originalPrice;
            return this;
        }

        public ProductBuilder size(String size) {
            this.size = size;
            return this;
        }

        public ProductBuilder color(String color) {
            this.color = color;
            return this;
        }

        public ProductBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public ProductBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductBuilder available(boolean available) {
            this.available = available;
            return this;
        }

        public Product build() {
            return new Product(id, name, brand, price, originalPrice, size, color, quantity, imageUrl, available);
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", size='" + size + '\'' +
                ", available=" + available +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

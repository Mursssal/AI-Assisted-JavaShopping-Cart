// This file was created with assistance from ChatGPT.
// I modified the following parts myself: Reviewed validation, immutability, equality, and display formatting.

import java.util.Objects;

public final class Product {

    private final String id;
    // Product is immutable: all fields are final and there are no setters.
    // The product ID defines the identity of a Product.
    private final String name;
    private final double price;
    private final ProductCategory category;

    public Product(String id, String name, double price, ProductCategory category)
            throws InvalidParameterException {

        if (id == null || id.isBlank()) {
            throw new InvalidParameterException("Product ID cannot be empty.");
        }

        if (!id.matches("[a-zA-Z0-9]+")) {
            throw new InvalidParameterException("Product ID must be alphanumeric.");
        }

        if (name == null || name.isBlank()) {
            throw new InvalidParameterException("Product name cannot be empty.");
        }

        if (price <= 0) {
            throw new InvalidParameterException("Product price must be greater than zero.");
        }

        if (category == null) {
            throw new InvalidParameterException("Product category cannot be null.");
        }

        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public ProductCategory getCategory() {
        return category;
    }
// Formats the product information for display with the price rounded to two decimals.
    public String getDisplayLabel() {
        return String.format("%s ($%.2f, %s)", name, price, category);
    }

    // Product identity is defined by the product ID.
    // Two Products with the same ID represent the same product.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Product)) {
            return false;
        }

        Product other = (Product) obj;
        return id.equals(other.id);
    }


// hashCode uses the same identity field as equals: the product ID.
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                '}';
    }
}
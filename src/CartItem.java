// This file was created with assistance from ChatGPT.
// I modified the following parts myself: Reviewed quantity validation and price storage.

public class CartItem {

    // Product and unitPrice remain fixed after creation.
   // Quantity is mutable because cart quantities can change.
    private final Product product;
    // The price is captured when the product is added to the cart.
    private final double unitPrice;
    private int quantity;

    public CartItem(Product product, int quantity)
            throws InvalidParameterException {

        if (product == null) {
            throw new InvalidParameterException("Product cannot be null.");
        }

        if (quantity <= 0) {
            throw new InvalidParameterException("Quantity must be greater than zero.");
        }

        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void increaseQuantity(int quantity)
            throws InvalidParameterException {

        if (quantity <= 0) {
            throw new InvalidParameterException(
                    "Quantity must be greater than zero.");
        }

        this.quantity += quantity;
    }
    // CartItem itself cannot reduce its quantity below one.
    // ShoppingCart removes the item when its quantity should reach zero.
    public void decreaseQuantity(int quantity)
            throws InvalidParameterException {

        if (quantity <= 0) {
            throw new InvalidParameterException(
                    "Quantity must be greater than zero.");
        }

        if (this.quantity - quantity <= 0) {
            throw new InvalidParameterException(
                    "Quantity cannot be reduced below one.");
        }

        this.quantity -= quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
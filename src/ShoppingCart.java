// This file was created with assistance from ChatGPT.
// I modified the following parts myself: Reviewed cart operations, checkout rules, state handling, and exception behavior.

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCart {

    // The cart owns its item list and controls all modifications through its methods.
    // A cart begins ACTIVE and becomes CHECKED_OUT after successful checkout.
    private final List<CartItem> items;
    private CartState state;

    public ShoppingCart() {
        items = new ArrayList<>();
        state = CartState.ACTIVE;
    }

    // Adding an existing product increases its quantity instead of creating
    // a duplicate CartItem. Product equality is based on product ID.
    public void addProduct(Product product, int quantity)
            throws InvalidParameterException, InvalidCartStateException {

        if (state == CartState.CHECKED_OUT) {
            throw new InvalidCartStateException("Cart has already been checked out.");
        }

        if (product == null) {
            throw new InvalidParameterException("Product cannot be null.");
        }

        if (quantity <= 0) {
            throw new InvalidParameterException("Quantity must be greater than zero.");
        }

        for (CartItem item : items) {
            if (item.getProduct().equals(product)) {
                item.increaseQuantity(quantity);
                return;
            }
        }

        items.add(new CartItem(product, quantity));
    }

    public double getTotal() {
        double total = 0.0;

        for (CartItem item : items) {
            total += item.getUnitPrice() * item.getQuantity();
        }

        return roundMoney(total);
    }

    // Tax is calculated separately for each item using that product's category tax rate.
    public double getTotalTax() {
        double tax = 0.0;

        for (CartItem item : items) {
            double itemTotal = item.getUnitPrice() * item.getQuantity();
            tax += itemTotal * item.getProduct().getCategory().getTaxRate();
        }

        return roundMoney(tax);
    }
    // The final total is the subtotal plus the calculated tax.
    public double getTotalWithTax() {
        return roundMoney(getTotal() + getTotalTax());
    }
    // Return an unmodifiable copy so callers cannot directly modify the cart's
     // internal list and bypass the cart's validation and state rules.
    public List<CartItem> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }
    // If the requested decrease equals the current quantity, the product is
// removed from the cart. Otherwise, the CartItem quantity is decreased.
    public void decreaseProductQuantity(String productId, int quantity)
            throws InvalidParameterException, InvalidCartStateException {

        if (state == CartState.CHECKED_OUT) {
            throw new InvalidCartStateException("Cart has already been checked out.");
        }

        if (productId == null || productId.isBlank()) {
            throw new InvalidParameterException("Product ID cannot be empty.");
        }

        if (quantity <= 0) {
            throw new InvalidParameterException("Quantity must be greater than zero.");
        }

        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);

            if (item.getProduct().getId().equals(productId)) {

                if (quantity > item.getQuantity()) {
                    throw new InvalidParameterException(
                            "Cannot decrease quantity below zero."
                    );
                }

                if (quantity == item.getQuantity()) {
                    items.remove(i);
                } else {
                    item.decreaseQuantity(quantity);
                }

                return;
            }
        }

        throw new InvalidParameterException("Product not found in cart.");
    }

    public void removeProduct(String productId)
            throws InvalidParameterException, InvalidCartStateException {

        if (state == CartState.CHECKED_OUT) {
            throw new InvalidCartStateException("Cart has already been checked out.");
        }

        if (productId == null || productId.isBlank()) {
            throw new InvalidParameterException("Product ID cannot be empty.");
        }

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getId().equals(productId)) {
                items.remove(i);
                return;
            }
        }

        // If the product is not in the cart, do nothing.
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public CartState getState() {
        return state;
    }
    // Successful checkout changes the cart state from ACTIVE to CHECKED_OUT.
// A checked-out cart cannot be modified or checked out again.
    public void checkout()
            throws InvalidCartStateException {

        if (state == CartState.CHECKED_OUT) {
            throw new InvalidCartStateException("Cart has already been checked out.");
        }

        if (items.isEmpty()) {
            throw new InvalidCartStateException("Cannot checkout an empty cart.");
        }

        state = CartState.CHECKED_OUT;
    }
    // Cash must be sufficient to cover the total including tax.
// Successful checkout returns the customer's change and closes the cart.
    public double checkout(double cashProvided)
            throws InvalidParameterException, InvalidCartStateException {

        if (state == CartState.CHECKED_OUT) {
            throw new InvalidCartStateException("Cart has already been checked out.");
        }

        if (items.isEmpty()) {
            throw new InvalidCartStateException("Cannot checkout an empty cart.");
        }

        if (cashProvided <= 0) {
            throw new InvalidParameterException(
                    "Cash provided must be greater than zero."
            );
        }

        double total = getTotalWithTax();

        if (cashProvided < total) {
            throw new InvalidParameterException(
                    "Insufficient cash provided."
            );
        }

        double change = roundMoney(cashProvided - total);

        state = CartState.CHECKED_OUT;

        return change;
    }
    // Monetary results are rounded to two decimal places as required by the assignment.
    private double roundMoney(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "items=" + items +
                ", state=" + state +
                ", total=" + getTotal() +
                '}';
    }
}

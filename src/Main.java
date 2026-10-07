
import java.util.List;

public class Main {

    private static int pointsEarned = 0;
    private static int pointsPossible = 0;

    public static void main(String[] args) {
        System.out.println("=== ShoppingCart Self-Test Harness ===");

        runTest("Product validation & construction", 4, Main::testProductValidation);
        runTest("Product equals/hashCode/label", 4, Main::testProductEqualsHashLabel);
        runTest("ProductCategory tax rates", 3, Main::testProductCategoryTaxRates);
        runTest("CartItem validation", 4, Main::testCartItemValidation);
        runTest("CartItem quantity operations", 4, Main::testCartItemQuantityOps);
        runTest("Cart basic totals & tax", 5, Main::testBasicTotalsAndTax);
        runTest("Adding same product merges quantity", 5, Main::testAddingSameProductMerges);
        runTest("Remove & decrease behavior", 5, Main::testRemoveAndDecreaseBehavior);
        runTest("Invalid add parameters", 4, Main::testInvalidAddParameters);
        runTest("Cart state transitions & immutability", 5, Main::testCartStateTransitions);
        runTest("Cash checkout logic (change & state)", 5, Main::testCashCheckoutLogic);
        runTest("Cash checkout validation", 4, Main::testCashCheckoutValidation);
        runTest("getItems immutability", 4, Main::testGetItemsImmutability);
        runTest("Cart toString contains data", 4, Main::testToStringContainsData);

        System.out.println("====================================");
        System.out.println("SELF-CHECK SCORE: " + pointsEarned + " / " + pointsPossible);
        System.out.println("====================================");
    }

    // ---------- Test harness utilities ----------

    @FunctionalInterface
    private interface TestBody {
        boolean run() throws Exception;
    }

    private static void runTest(String name, int pts, TestBody body) {
        System.out.println("------------------------------------");
        pointsPossible += pts;
        boolean passed;
        try {
            passed = body.run();
        } catch (Throwable t) {
            System.out.println("  [ERROR] " + name + " threw unexpected exception: " + t);
            t.printStackTrace(System.out);
            passed = false;
        }

        if (passed) {
            pointsEarned += pts;
            System.out.println("[PASS] " + name + " (+" + pts + ")");
        } else {
            System.out.println("[FAIL] " + name + " (0 / " + pts + ")");
        }
    }

    private static Product newProduct(String id, String name, double price, ProductCategory cat)
            throws InvalidParameterException {
        return new Product(id, name, price, cat);
    }

    // ---------- Individual tests ----------

    private static boolean testProductValidation() {
        boolean ok = true;

        try {
            Product p = newProduct("P1", "Test", 10.0, ProductCategory.ELECTRONICS);
            ok &= "P1".equals(p.getId());
            ok &= "Test".equals(p.getName());
            ok &= Math.abs(10.0 - p.getPrice()) < 0.0001;
        } catch (Exception e) {
            System.out.println("  Expected valid product but got exception: " + e);
            ok = false;
        }

        // negative price
        try {
            newProduct("P2", "Bad", -1.0, ProductCategory.ELECTRONICS);
            System.out.println("  Expected InvalidParameterException for negative price but none thrown");
            ok = false;
        } catch (InvalidParameterException e) {
            // expected
        } catch (Exception e) {
            System.out.println("  Expected InvalidParameterException, got: " + e);
            ok = false;
        }

        // null id
        try {
            newProduct(null, "Name", 1.0, ProductCategory.BOOKS);
            System.out.println("  Expected InvalidParameterException for null id but none thrown");
            ok = false;
        } catch (InvalidParameterException e) {
            // expected
        } catch (Exception e) {
            System.out.println("  Expected InvalidParameterException for null id, got: " + e);
            ok = false;
        }

        // blank name
        try {
            newProduct("P3", "   ", 1.0, ProductCategory.BOOKS);
            System.out.println("  Expected InvalidParameterException for blank name but none thrown");
            ok = false;
        } catch (InvalidParameterException e) {
            // expected
        } catch (Exception e) {
            System.out.println("  Expected InvalidParameterException for blank name, got: " + e);
            ok = false;
        }

        // null category
        try {
            newProduct("P4", "Name", 1.0, null);
            System.out.println("  Expected InvalidParameterException for null category but none thrown");
            ok = false;
        } catch (InvalidParameterException e) {
            // expected
        } catch (Exception e) {
            System.out.println("  Expected InvalidParameterException for null category, got: " + e);
            ok = false;
        }

        return ok;
    }

    private static boolean testProductEqualsHashLabel() {
        boolean ok = true;

        try {
            Product p1 = newProduct("P100", "Name A", 10.0, ProductCategory.ELECTRONICS);
            Product p2 = newProduct("P100", "Name B", 20.0, ProductCategory.BOOKS);
            Product p3 = newProduct("P200", "Name C", 10.0, ProductCategory.ELECTRONICS);

            ok &= p1.equals(p2);
            ok &= p1.hashCode() == p2.hashCode();
            ok &= !p1.equals(p3);
            ok &= !p1.equals(null);
            ok &= !p1.equals("not a product");

            String label = p1.getDisplayLabel();
            ok &= label.contains("Name A");
            ok &= label.contains("10.00");
            ok &= label.contains("ELECTRONICS");

            String s = p1.toString();
            ok &= s.contains("P100");
            ok &= s.contains("Name A");
        } catch (Exception e) {
            System.out.println("  Exception in equals/hash/label test: " + e);
            ok = false;
        }

        return ok;
    }

    private static boolean testProductCategoryTaxRates() {
        boolean ok = true;
        ok &= Math.abs(ProductCategory.ELECTRONICS.getTaxRate() - 0.10) < 0.0001;
        ok &= Math.abs(ProductCategory.BOOKS.getTaxRate() - 0.00) < 0.0001;
        ok &= Math.abs(ProductCategory.CLOTHING.getTaxRate() - 0.08) < 0.0001;
        ok &= Math.abs(ProductCategory.GROCERY.getTaxRate() - 0.02) < 0.0001;
        if (!ok) {
            System.out.println("  ProductCategory tax rates do not match expected values.");
        }
        return ok;
    }

    private static boolean testCartItemValidation() {
        boolean ok = true;
        try {
            Product p = newProduct("P1", "Test", 10.0, ProductCategory.GROCERY);
            CartItem item = new CartItem(p, 3);
            ok &= item.getProduct() == p;
            ok &= item.getQuantity() == 3;
            ok &= Math.abs(item.getUnitPrice() - 10.0) < 0.0001;
        } catch (Exception e) {
            System.out.println("  Unexpected exception in valid CartItem construction: " + e);
            ok = false;
        }

        // null product
        try {
            new CartItem(null, 1);
            System.out.println("  Expected InvalidParameterException for null product in CartItem but none thrown");
            ok = false;
        } catch (InvalidParameterException e) {
            // expected
        } catch (Exception e) {
            System.out.println("  Expected InvalidParameterException for null product, got: " + e);
            ok = false;
        }

        // quantity < 1
        try {
            Product p = newProduct("P2", "Test2", 5.0, ProductCategory.GROCERY);
            new CartItem(p, 0);
            System.out.println("  Expected InvalidParameterException for quantity < 1 but none thrown");
            ok = false;
        } catch (InvalidParameterException e) {
            // expected
        } catch (Exception e) {
            System.out.println("  Expected InvalidParameterException for quantity < 1, got: " + e);
            ok = false;
        }

        return ok;
    }

    private static boolean testCartItemQuantityOps() {
        boolean ok = true;
        try {
            Product p = newProduct("P1", "Test", 10.0, ProductCategory.GROCERY);
            CartItem item = new CartItem(p, 5);

            // increase valid
            item.increaseQuantity(3);
            ok &= item.getQuantity() == 8;

            // increase invalid
            try {
                item.increaseQuantity(0);
                System.out.println("  Expected InvalidParameterException for increaseQuantity(0) but none thrown");
                ok = false;
            } catch (InvalidParameterException e) {
                // expected
            }

            // decrease valid
            item.decreaseQuantity(3); // 8 -> 5
            ok &= item.getQuantity() == 5;

            // decrease invalid amount
            try {
                item.decreaseQuantity(0);
                System.out.println("  Expected InvalidParameterException for decreaseQuantity(0) but none thrown");
                ok = false;
            } catch (InvalidParameterException e) {
                // expected
            }

            // decrease to below 1
            CartItem single = new CartItem(p, 1);
            try {
                single.decreaseQuantity(1);
                System.out.println("  Expected InvalidParameterException when decreasing to below 1 but none thrown");
                ok = false;
            } catch (InvalidParameterException e) {
                // expected
            }
        } catch (Exception e) {
            System.out.println("  Unexpected exception in CartItem quantity ops: " + e);
            ok = false;
        }
        return ok;
    }

    private static boolean testBasicTotalsAndTax() {
        boolean ok = true;
        try {
            Product mouse = newProduct("M1", "Mouse", 30.0, ProductCategory.ELECTRONICS); // 10%
            Product book  = newProduct("B1", "Book", 20.0, ProductCategory.BOOKS);        // 0%
            Product jeans = newProduct("J1", "Jeans", 50.0, ProductCategory.CLOTHING);    // 8%

            ShoppingCart cart = new ShoppingCart();
            cart.addProduct(mouse, 2); // 60, tax 6
            cart.addProduct(book, 1);  // 20, tax 0
            cart.addProduct(jeans, 1); // 50, tax 4

            double subtotal = cart.getTotal();         // 130
            double tax = cart.getTotalTax();           // 10
            double totalWithTax = cart.getTotalWithTax(); // 140

            ok &= Math.abs(subtotal - 130.0) < 0.0001;
            ok &= Math.abs(tax - 10.0) < 0.0001;
            ok &= Math.abs(totalWithTax - 140.0) < 0.0001;

            if (!ok) {
                System.out.println("  Expected subtotal=130, tax=10, total=140 but got: "
                        + subtotal + ", " + tax + ", " + totalWithTax);
            }
        } catch (Exception e) {
            System.out.println("  Unexpected exception in basic totals & tax: " + e);
            ok = false;
        }
        return ok;
    }

    private static boolean testAddingSameProductMerges() {
        boolean ok = true;
        try {
            Product p = newProduct("P1", "Test", 10.0, ProductCategory.GROCERY);
            ShoppingCart cart = new ShoppingCart();

            cart.addProduct(p, 1);
            cart.addProduct(p, 2);

            List<CartItem> items = cart.getItems();
            ok &= items.size() == 1;
            ok &= items.get(0).getQuantity() == 3;
            ok &= Math.abs(cart.getTotal() - 30.0) < 0.0001;
        } catch (Exception e) {
            System.out.println("  Unexpected exception in same-product-merge test: " + e);
            ok = false;
        }
        return ok;
    }

    private static boolean testRemoveAndDecreaseBehavior() {
        boolean ok = true;
        try {
            Product p1 = newProduct("P1", "A", 10.0, ProductCategory.GROCERY);
            Product p2 = newProduct("P2", "B", 20.0, ProductCategory.GROCERY);

            ShoppingCart cart = new ShoppingCart();
            cart.addProduct(p1, 3);
            cart.addProduct(p2, 1);

            // decrease quantity on P1
            cart.decreaseProductQuantity("P1", 1); // 3 -> 2
            ok &= cart.getItems().size() == 2;
            ok &= cart.getItems().stream()
                    .filter(ci -> ci.getProduct().getId().equals("P1"))
                    .findFirst().get().getQuantity() == 2;

            // decrease to zero should remove
            cart.decreaseProductQuantity("P1", 2); // remove P1
            ok &= cart.getItems().size() == 1;
            ok &= "P2".equals(cart.getItems().get(0).getProduct().getId());

            // remove P2 explicitly
            cart.removeProduct("P2");
            ok &= cart.isEmpty();

            // missing product remove should not crash or change
            cart.addProduct(p1, 1);
            cart.removeProduct("NOPE");
            ok &= cart.getItems().size() == 1;
        } catch (Exception e) {
            System.out.println("  Unexpected exception in remove/decrease behavior: " + e);
            ok = false;
        }
        return ok;
    }

    private static boolean testInvalidAddParameters() {
        boolean ok = true;
        try {
            ShoppingCart cart = new ShoppingCart();
            Product p = newProduct("P1", "Test", 10.0, ProductCategory.GROCERY);

            // quantity 0
            try {
                cart.addProduct(p, 0);
                System.out.println("  Expected InvalidParameterException for quantity 0 but none thrown");
                ok = false;
            } catch (InvalidParameterException e) {
                // expected
            }

            // quantity negative
            try {
                cart.addProduct(p, -2);
                System.out.println("  Expected InvalidParameterException for negative quantity but none thrown");
                ok = false;
            } catch (InvalidParameterException e) {
                // expected
            }

            // null product
            try {
                cart.addProduct(null, 1);
                System.out.println("  Expected InvalidParameterException for null product but none thrown");
                ok = false;
            } catch (InvalidParameterException e) {
                // expected
            }
        } catch (Exception e) {
            System.out.println("  Unexpected exception in invalid add parameter test: " + e);
            ok = false;
        }
        return ok;
    }

    private static boolean testCartStateTransitions() {
        boolean ok = true;
        try {
            Product p = newProduct("P1", "StateTest", 5.0, ProductCategory.GROCERY);
            ShoppingCart cart = new ShoppingCart();

            // initial state
            ok &= cart.isEmpty();
            ok &= cart.getState() == CartState.ACTIVE;

            cart.addProduct(p, 1);
            ok &= !cart.isEmpty();

            // checkout should set CHECKED_OUT
            cart.checkout();
            ok &= cart.getState() == CartState.CHECKED_OUT;

            // second checkout should fail
            try {
                cart.checkout();
                System.out.println("  Expected InvalidCartStateException on second checkout but none thrown");
                ok = false;
            } catch (InvalidCartStateException e) {
                // expected
            }

            // any further modifications should fail
            try {
                cart.addProduct(p, 1);
                System.out.println("  Expected InvalidCartStateException on add after checkout but none thrown");
                ok = false;
            } catch (InvalidCartStateException e) {
                // expected
            }

            try {
                cart.removeProduct(p.getId());
                System.out.println("  Expected InvalidCartStateException on remove after checkout but none thrown");
                ok = false;
            } catch (InvalidCartStateException e) {
                // expected
            }
        } catch (Exception e) {
            System.out.println("  Unexpected exception in cart state transitions: " + e);
            ok = false;
        }
        return ok;
    }

    private static boolean testCashCheckoutLogic() {
        boolean ok = true;
        try {
            Product mouse = newProduct("M1", "Mouse", 30.0, ProductCategory.ELECTRONICS); // 10%
            Product book  = newProduct("B1", "Book", 20.0, ProductCategory.BOOKS);        // 0%

            ShoppingCart cart = new ShoppingCart();
            cart.addProduct(mouse, 2); // 60, tax 6
            cart.addProduct(book, 1);  // 20, tax 0

            double totalWithTax = cart.getTotalWithTax(); // expected 86
            double cashProvided = 100.0;
            double change = cart.checkout(cashProvided);

            ok &= Math.abs(totalWithTax - 86.0) < 0.0001;
            ok &= Math.abs(change - 14.0) < 0.0001;
            ok &= cart.getState() == CartState.CHECKED_OUT;

            // further modification should fail
            try {
                cart.addProduct(mouse, 1);
                System.out.println("  Expected InvalidCartStateException after cash checkout but none thrown");
                ok = false;
            } catch (InvalidCartStateException e) {
                // expected
            }
        } catch (Exception e) {
            System.out.println("  Unexpected exception in cash checkout logic: " + e);
            ok = false;
        }
        return ok;
    }

    private static boolean testCashCheckoutValidation() {
        boolean ok = true;
        try {
            Product p = newProduct("P1", "Test", 10.0, ProductCategory.GROCERY);
            ShoppingCart cart = new ShoppingCart();
            cart.addProduct(p, 1);

            // negative cash
            try {
                cart.checkout(-5.0);
                System.out.println("  Expected InvalidParameterException for negative cash but none thrown");
                ok = false;
            } catch (InvalidParameterException e) {
                // expected
            }

            // insufficient cash
            double totalWithTax = cart.getTotalWithTax();
            try {
                cart.checkout(totalWithTax - 0.01);
                System.out.println("  Expected InvalidParameterException for insufficient cash but none thrown");
                ok = false;
            } catch (InvalidParameterException e) {
                // expected
            }
        } catch (Exception e) {
            System.out.println("  Unexpected exception in cash checkout validation: " + e);
            ok = false;
        }
        return ok;
    }

    private static boolean testGetItemsImmutability() {
        boolean ok = true;
        try {
            Product p = newProduct("P1", "Test", 10.0, ProductCategory.GROCERY);
            ShoppingCart cart = new ShoppingCart();
            cart.addProduct(p, 1);

            List<CartItem> items = cart.getItems();
            boolean mutatedList = false;
            try {
                items.add(items.get(0));
                mutatedList = true; // if this works, list is mutable
            } catch (UnsupportedOperationException e) {
                // expected: unmodifiable list
            }

            if (mutatedList) {
                System.out.println("  getItems returned a mutable list (should be unmodifiable copy)");
                ok = false;
            }
        } catch (Exception e) {
            System.out.println("  Unexpected exception in getItems immutability test: " + e);
            ok = false;
        }
        return ok;
    }

    private static boolean testToStringContainsData() {
        boolean ok = true;
        try {
            Product p = newProduct("P1", "Widget", 10.0, ProductCategory.GROCERY);
            ShoppingCart cart = new ShoppingCart();
            cart.addProduct(p, 2);

            String s = cart.toString();
            ok &= s != null && !s.isBlank();
            ok &= s.contains("Widget");
            ok &= s.contains("2");
            ok &= s.contains("10"); // price-ish

            if (!ok) {
                System.out.println("  Cart toString does not appear to contain expected data: " + s);
            }
        } catch (Exception e) {
            System.out.println("  Unexpected exception in toStringContainsData: " + e);
            ok = false;
        }
        return ok;
    }
}

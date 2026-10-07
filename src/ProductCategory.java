// This file was created with assistance from ChatGPT.
// I modified the following parts myself: Reviewed the category names and tax rates.

public enum ProductCategory {

    // Each category stores its required sales-tax rate as a decimal.
    ELECTRONICS(0.10),
    BOOKS(0.00),
    CLOTHING(0.08),
    GROCERY(0.02);

    private final double taxRate;

    ProductCategory(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getTaxRate() {
        return taxRate;
    }
}

# Java Shopping Cart

A Java-based shopping cart application built for SE 350 Object-Oriented Software Development. The project demonstrates core object-oriented programming concepts through a product, cart item, and shopping cart model.

## Features

- Product management with validation
- Immutable `Product` objects
- Product categories with different tax rates
- Shopping cart item and quantity management
- Automatic merging of identical products
- Subtotal and tax calculations
- Checkout and cash/change handling
- Cart state management
- Checked exception handling
- Input validation and edge-case handling
- Unmodifiable cart item lists
- `equals()`, `hashCode()`, and `toString()` implementations

## Technologies

- Java
- IntelliJ IDEA
- Object-Oriented Programming
- Git / GitHub

## Project Structure

- `Product.java` — Represents an immutable product and its information.
- `ProductCategory.java` — Defines product categories and their tax rates.
- `CartItem.java` — Represents a product and its quantity in the cart.
- `ShoppingCart.java` — Manages cart items, totals, taxes, and checkout.
- `CartState.java` — Tracks whether the cart is active or checked out.
- `InvalidParameterException.java` — Handles invalid input parameters.
- `InvalidCartStateException.java` — Handles invalid cart operations based on cart state.
- `Main.java` — Provided test driver and self-test harness.

## Concepts Demonstrated

This project focuses on:

- Encapsulation
- Immutability
- Object identity and equality
- Enums
- Checked exceptions
- Collections
- Input validation
- State management
- Method design
- Object-oriented relationships

## Testing

The provided self-test harness was used to verify the implementation.

**Self-Test Score: 60 / 60**

All required tests passed, including product validation, cart operations, tax calculations, checkout, exception handling, state transitions, and collection protection.

## AI-Assisted Development

AI tools were used during development to help generate initial implementations and explore design approaches. The generated code was reviewed, tested, and modified to meet the assignment requirements and the behavior expected by the provided test harness.

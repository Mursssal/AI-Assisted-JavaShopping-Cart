// This file was created with assistance from ChatGPT.
// I modified the following parts myself: Reviewed and tested the exception structure.


// Checked exception used when an operation is invalid because of the cart's current state.

public class InvalidCartStateException extends Exception {

    public InvalidCartStateException(String message) {
        super(message);
    }
}
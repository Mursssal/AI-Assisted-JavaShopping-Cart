// This file was created with assistance from ChatGPT.
// I modified the following parts myself: Reviewed and tested the exception structure.

// Checked exception used when a method receives an invalid parameter.

public class InvalidParameterException extends Exception {

    public InvalidParameterException(String message) {
        super(message);
    }
}
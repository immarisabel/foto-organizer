package nl.marisabel.exceptions;

public class ModifyException extends Exception {
 public ModifyException(String message) {
  super(message);
 }

 public ModifyException(String message, Throwable cause) {
  super(message, cause);
 }
}

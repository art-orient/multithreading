package by.art.multithreading.exception;

public class LogisticBaseException extends Exception {

  public LogisticBaseException() {
  }

  public LogisticBaseException(String message) {
    super(message);
  }

  public LogisticBaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public LogisticBaseException(Throwable cause) {
    super(cause);
  }
}

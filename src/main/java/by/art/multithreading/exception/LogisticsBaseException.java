package by.art.multithreading.exception;

public class LogisticsBaseException extends Exception {

  public LogisticsBaseException() {
  }

  public LogisticsBaseException(String message) {
    super(message);
  }

  public LogisticsBaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public LogisticsBaseException(Throwable cause) {
    super(cause);
  }
}

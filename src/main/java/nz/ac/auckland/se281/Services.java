package nz.ac.auckland.se281;

public abstract class Services {

  protected String bookingReference;

  public Services(String bookingReference) {
    this.bookingReference = bookingReference;
  }

  public abstract void addService();

  public Object getBookingReference() {
    return bookingReference;
  }
}

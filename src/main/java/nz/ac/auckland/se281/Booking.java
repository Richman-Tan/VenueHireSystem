package nz.ac.auckland.se281;

import java.util.ArrayList;

public class Booking extends EventSpace {
  // Instance fields specific to Booking
  private String bookingReference;
  private String customerEmail;
  private String bookingDate;
  private String numberOfGuests;
  private String systemDate;

  // Constructor
  public Booking(
      String code,
      String bookingReference,
      String customerEmail,
      String bookingDate,
      String numberOfGuests) {
    super(code);
    this.bookingReference = bookingReference;
    this.customerEmail = customerEmail;
    this.bookingDate = bookingDate;
    this.numberOfGuests = numberOfGuests;
    this.systemDate = null;
  }

  // Getters and setters

  public void setSystemDate(String systemDate) {
    this.systemDate = systemDate;
  }

  public String getSystemDate() {
    return systemDate;
  }

  public String getBookingReference() {
    return bookingReference;
  }

  public void setBookingReference(String bookingReference) {
    this.bookingReference = bookingReference;
  }

  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public String getBookingDate() {
    return bookingDate;
  }

  public void setBookingDate(String bookingDate) {
    this.bookingDate = bookingDate;
  }

  public String getNumberOfGuests() {
    return numberOfGuests;
  }

  public void setNumberOfGuests(String numberOfGuests) {
    this.numberOfGuests = numberOfGuests;
  }

  // Implementation of abstract methods from EventSpace

  @Override
  public String getCode() {
    return super.getCode();
  }

  @Override
  public String getName() {
    return ""; // As booking doesn't have a name, return empty string
  }

  @Override
  public int getCapacity() {
    return 0; // As booking doesn't have a capacity, return 0
  }

  @Override
  public boolean isAvailable() {
    return true; // As booking is always available, return true
  }

  @Override
  public String getCorrespondingVenueName(ArrayList<Venue> venues, String venueCode) {
    for (Venue venue : venues) {
      if (venue.getCode().equals(venueCode)) {
        return venue.getName();
      }
    }
    return "";
  }

  public static boolean bookingTooFewAttendees(Venue selectedVenue, String numberOfGuests) {
    if ((selectedVenue.getCapacity() * 0.25) > Integer.parseInt(numberOfGuests)) {
      return true;
    }
    return false;
  }

  public static boolean bookingTooMuchAttendees(Venue selectedVenue, String numberOfGuests) {
    if ((selectedVenue.getCapacity()) < Integer.parseInt(numberOfGuests)) {
      return true;
    }
    return false;
  }
}

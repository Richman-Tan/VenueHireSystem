package nz.ac.auckland.se281;

import java.util.ArrayList;

public class Venue extends EventSpace {

  // Declaring Instance fields
  private String name;
  private String code;
  private String capacityInput;
  private String hireFeeInput;
  private String nextAvailable;
  private boolean isAvailable;

  // Constructor
  public Venue(
      String name, String code, String capacityInput, String hireFeeInput, String nextAvailable) {
    super(code);
    this.name = name;
    this.code = code;
    this.capacityInput = capacityInput;
    this.hireFeeInput = hireFeeInput;
    this.nextAvailable = nextAvailable;
  }

  // Getters and setters

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCapacityInput() {
    return capacityInput;
  }

  public void setCapacityInput(String capacityInput) {
    this.capacityInput = capacityInput;
  }

  public String getHireFeeInput() {
    return hireFeeInput;
  }

  public void setHireFeeInput(String hireFeeInput) {
    this.hireFeeInput = hireFeeInput;
  }

  public String getNextAvailable() {
    return nextAvailable;
  }

  public void setNextAvailable(String nextAvailable) {
    this.nextAvailable = nextAvailable;
  }

  // Method to see see the hireFeeInput is a valid number in the form of a string
  public boolean isNumeric(String hireFeeInput) {
    try {
      Integer.parseInt(hireFeeInput);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public boolean isDuplicateVenueCode(ArrayList<Venue> venues) {
    for (Venue venue : venues) {
      if (venue.getCode().equals(code)) {
        return true; // Found a venue with the same code
      }
    }
    return false; // No venue with the same code found
  }

  // Increments the days by one.
  public String getNextDay(String inputDate) {

    // Split the input date into day, month, and year
    String[] parts = inputDate.split("/");
    int day = Integer.parseInt(parts[0]);
    int month = Integer.parseInt(parts[1]);
    int year = Integer.parseInt(parts[2]);

    // Array to store the number of days in each month (0-based index) for a non-leap year max
    // months
    int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    // Increment the day by one
    day++;
    if (day > daysInMonth[month - 1]) {
      day = 1;
      month++;
      if (month > 12) {
        month = 1;
        year++;
      }
    }

    // Return the next day in the format "dd/mm/yyyy"
    return String.format("%02d/%02d/%04d", day, month, year);
  }

  @Override
  public String getCorrespondingVenueName(ArrayList<Venue> venues, String venueCode) {

    // Loop through the list of venues to find the venue with the matching code
    for (Venue venue : venues) {
      if (venue.getCode().equals(venueCode)) {
        return venue.getName();
      }
    }
    return "";
  }

  @Override
  public String getName() {
    // Returning a name of the venue
    return name;
  }

  @Override
  public int getCapacity() {
    // Assuming capacity is an integer value, you might need to parse it accordingly
    return Integer.parseInt(capacityInput);
  }

  @Override
  public boolean isAvailable() {
    return isAvailable;
  }
}

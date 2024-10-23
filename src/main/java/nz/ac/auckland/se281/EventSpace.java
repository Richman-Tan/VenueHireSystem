package nz.ac.auckland.se281;

import java.util.ArrayList;

public abstract class EventSpace {
  private String code;

  // Constructor
  public EventSpace(String code) {
    this.code = code;
  }

  // Abstract methods
  public abstract String getName();

  public abstract int getCapacity();

  public abstract boolean isAvailable();

  public abstract String getCorrespondingVenueName(ArrayList<Venue> venues, String venueCode);

  // Other methods
  public String getCode() {
    return code;
  }

  // You may add more common methods as needed
}

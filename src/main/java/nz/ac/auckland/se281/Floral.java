package nz.ac.auckland.se281;

import nz.ac.auckland.se281.Types.FloralType;

class Floral extends Services {

  // Declaring the floralType variable
  private FloralType floralType;

  // Constructor for the Floral class
  public Floral(String bookingReference, FloralType floralType) {
    // Calls the constructor of the parent class
    super(bookingReference);
    this.floralType = floralType;
  }

  // Makes sure to print the correct message when a Serivce is added to the the booking
  @Override
  public void addService() {
    // Prints the message to the console
    System.out.println(
        "Successfully added Floral ("
            + floralType.getName()
            + ") service to booking '"
            + bookingReference
            + "'.");
  }

  // Method to return the cost of the floraltypes
  public int getCost() {
    return floralType.getCost();
  }

  // Method to return the name of the floraltypes
  public String getName() {
    return floralType.getName();
  }
}

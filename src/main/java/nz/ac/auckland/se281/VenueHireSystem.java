package nz.ac.auckland.se281;

import java.util.ArrayList;
import nz.ac.auckland.se281.Types.CateringType;
import nz.ac.auckland.se281.Types.FloralType;

public class VenueHireSystem {

  // Declare Instance fields
  private ArrayList<Venue> venues;
  private ArrayList<Booking> bookings;
  private String systemDate = ""; // System date is DD/MM/YYYY
  private ArrayList<Services> services;

  // Constructor
  public VenueHireSystem() {
    venues = new ArrayList<>();
    bookings = new ArrayList<>();
    services = new ArrayList<>();
  }

  public void printVenues() {
    // Check if there are any venues
    int numberOfVenues = venues.size();
    if (numberOfVenues == 0) { // If there are no venues
      MessageCli.NO_VENUES.printMessage();
    } else {
      if (numberOfVenues == 1) { // Check if there is only one venue
        MessageCli.NUMBER_VENUES.printMessage("is", "one", "");
      } else if (numberOfVenues < 10
          && numberOfVenues > 1) { // Check if there are less than 10 venues but more than 1
        String[] units = {
          "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
        };
        MessageCli.NUMBER_VENUES.printMessage("are", units[numberOfVenues], "s");
      } else { // If there are more than 9 venues
        MessageCli.NUMBER_VENUES.printMessage("are", Integer.toString(numberOfVenues), "s");
      }
      for (Venue venue : venues) { // Print the details of each venue
        MessageCli.VENUE_ENTRY.printMessage(
            venue.getName(),
            venue.getCode(),
            venue.getCapacityInput(),
            venue.getHireFeeInput(),
            venue.getNextAvailable());
      }
    }
  }

  public void createVenue(
      String venueName, String venueCode, String capacityInput, String hireFeeInput) {

    // Check if the venue name is empty
    if (venueName.trim().isEmpty()) {
      MessageCli.VENUE_NOT_CREATED_EMPTY_NAME.printMessage();
    } else {
      venueName = venueName.trim();
      venueCode = venueCode.trim();

      // Remove leading zeros from capacity and hire fee inputs
      capacityInput = capacityInput.replaceFirst("^0+(?!$)", "");
      hireFeeInput = hireFeeInput.replaceFirst("^0+(?!$)", "");

      // Create a new instance of Venue
      Venue newVenue =
          new Venue(venueName.trim(), venueCode.trim(), capacityInput, hireFeeInput, systemDate);

      if (!newVenue.isNumeric(hireFeeInput)) { // Check if hire fee is a valid number
        MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage("hire fee", "");
      } else if (!newVenue.isNumeric(capacityInput)) { // Check if capacity is a valid number
        MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage("capacity", "");
      } else if (Integer.parseInt(capacityInput) <= 0) { // Check if capacity is positive
        MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage("capacity", " positive");
      } else if (Integer.parseInt(hireFeeInput) <= 0) { // Check if hire fee is positive
        MessageCli.VENUE_NOT_CREATED_INVALID_NUMBER.printMessage("hire fee", " positive");
      } else if (venueName.isEmpty()) { // Check if venue name is empty
        MessageCli.VENUE_NOT_CREATED_EMPTY_NAME.printMessage();
      } else if (newVenue.isDuplicateVenueCode(venues)) { // Check if venue code is a duplicate
        String firstInstanceofVenueNameString = venueName;

        // Find the first instance of the venue name
        for (Venue venue : venues) {
          if (venue.getCode().equals(venueCode)) {
            firstInstanceofVenueNameString = venue.getName();
          }
        }

        MessageCli.VENUE_NOT_CREATED_CODE_EXISTS.printMessage(
            venueCode, firstInstanceofVenueNameString);
      } else {
        // All checks passed, add venue to the list
        venues.add(newVenue);
        MessageCli.VENUE_SUCCESSFULLY_CREATED.printMessage(venueName.trim(), venueCode);
      }
    }
  }

  public void setSystemDate(String dateInput) {

    // Set the System date to the inputed date and print out correct message
    systemDate = dateInput;
    MessageCli.DATE_SET.printMessage(dateInput);

    // Check if the system date is empty
    if (systemDate.equals("")) {
      for (Venue venue : venues) {
        venue.setNextAvailable(dateInput);
      }
      return;
    }

    // Collect the corrosponding dates for the venue
    for (Venue venue : venues) {
      ArrayList<String> dateStrings = new ArrayList<>();
      for (Booking booking : bookings) {
        if (booking.getCode().equals(venue.getCode())) {
          dateStrings.add(booking.getBookingDate());
        }
      }

      // If there are no bookings for the venue, set the next available date to the system date
      if (dateStrings.isEmpty()) {
        venue.setNextAvailable(dateInput);
        continue;
      }

      // Sort date strings using bubble sort
      for (int i = 0; i < dateStrings.size() - 1; i++) {
        for (int j = 0; j < dateStrings.size() - i - 1; j++) {
          if (compareDates(dateStrings.get(j), dateStrings.get(j + 1)) > 0) {
            // Swap the dates
            String temp = dateStrings.get(j);
            dateStrings.set(j, dateStrings.get(j + 1));
            dateStrings.set(j + 1, temp);
          }
        }
      }

      // Find the next available date and set next avaliabledate for that venue.
      String nextAvailableDate = findNextAvailableDate(dateStrings, systemDate);
      venue.setNextAvailable(nextAvailableDate);
    }
  }

  public void printSystemDate() {
    // Print the system date
    if (systemDate.isEmpty()) { // If the system date is empty
      MessageCli.CURRENT_DATE.printMessage("not set");
    } else {
      MessageCli.CURRENT_DATE.printMessage(systemDate);
    }
  }

  public void makeBooking(String[] options) {

    // Check if we have set the systemDate is empty we return or if the venues array is empty we
    // return
    if (systemDate.isEmpty()) {
      MessageCli.BOOKING_NOT_MADE_DATE_NOT_SET.printMessage();
      return;
    } else if (venues.isEmpty()) { // If there are no venues
      MessageCli.BOOKING_NOT_MADE_NO_VENUES.printMessage();
      return;
    }

    // Gathering inputs from the user.
    String venueCode = options[0];
    String bookingDate = options[1];
    String customerEmail = options[2];
    String numberOfGuests = options[3];
    String bookingReference =
        BookingReferenceGenerator.generateBookingReference(); // Generate a booking reference

    // Find the corrosponding code in the array
    Venue selectedVenue = null;
    for (Venue venue : venues) {
      if (venue.getCode().equals(venueCode)) {
        selectedVenue = venue;
        break;
      }
    }

    // No venue that corresponds to the input venue code in the system --> output a message.
    if (selectedVenue == null) {
      MessageCli.BOOKING_NOT_MADE_VENUE_NOT_FOUND.printMessage(venueCode);
      return;
    }

    // Go through the booking array to check if the date is a duplicate/already booked.
    for (Booking book : bookings) {
      if (book.getCode().equals(selectedVenue.getCode())
          && bookingDate.equals(book.getBookingDate())) {
        MessageCli.BOOKING_NOT_MADE_VENUE_ALREADY_BOOKED.printMessage(
            selectedVenue.getName(), bookingDate);
        return;
      }
    }

    // Check if booking date is in the past
    String[] parts = systemDate.split("/");
    int day = Integer.parseInt(parts[0]);
    int month = Integer.parseInt(parts[1]);
    int year = Integer.parseInt(parts[2]);

    // Get the current date
    String[] currentParts = bookingDate.split("/");
    int bookingDay = Integer.parseInt(currentParts[0]);
    int bookingMonth = Integer.parseInt(currentParts[1]);
    int bookingYear = Integer.parseInt(currentParts[2]);

    // Check if the input date is in the past
    if (year > bookingYear
        || (year == bookingYear && month > bookingMonth)
        || (year == bookingYear && month == bookingMonth && day > bookingDay)) {
      MessageCli.BOOKING_NOT_MADE_PAST_DATE.printMessage(bookingDate, systemDate);
      return;
    }

    // Check if booking have too few attendents
    if (Booking.bookingTooFewAttendees(selectedVenue, numberOfGuests)) {
      int adjustedValue = (int) (selectedVenue.getCapacity() * 0.25);

      MessageCli.BOOKING_ATTENDEES_ADJUSTED.printMessage(
          numberOfGuests,
          String.valueOf(adjustedValue),
          String.valueOf(selectedVenue.getCapacity()));

      numberOfGuests = Integer.toString(adjustedValue);
    }

    // Check if booking have too much attendents
    if (Booking.bookingTooMuchAttendees(selectedVenue, numberOfGuests)) {

      MessageCli.BOOKING_ATTENDEES_ADJUSTED.printMessage(
          numberOfGuests,
          String.valueOf(selectedVenue.getCapacity()),
          String.valueOf(selectedVenue.getCapacity()));

      numberOfGuests = Integer.toString(selectedVenue.getCapacity());
    }

    // Create a new instance of Booking and add it to the booking array
    Booking newBooking =
        new Booking(venueCode, bookingReference, customerEmail, bookingDate, numberOfGuests);

    newBooking.setSystemDate(systemDate);
    bookings.add(newBooking);

    MessageCli.MAKE_BOOKING_SUCCESSFUL.printMessage(
        bookingReference,
        newBooking.getCorrespondingVenueName(venues, venueCode),
        bookingDate,
        numberOfGuests);

    // Update the next available date for the venue
    for (Venue venue : venues) {
      ArrayList<String> dateStrings = new ArrayList<>();
      for (Booking booking : bookings) {
        if (booking.getCode().equals(venue.getCode())) {
          dateStrings.add(booking.getBookingDate());
        }
      }

      // If there are no bookings for the venue, set the next available date to the system date
      if (dateStrings.isEmpty()) {
        venue.setNextAvailable(systemDate);
        continue;
      }

      // Sort date strings using bubble sort
      for (int i = 0; i < dateStrings.size() - 1; i++) {
        for (int j = 0; j < dateStrings.size() - i - 1; j++) {
          if (compareDates(dateStrings.get(j), dateStrings.get(j + 1)) > 0) {
            // Swap the dates
            String temp = dateStrings.get(j);
            dateStrings.set(j, dateStrings.get(j + 1));
            dateStrings.set(j + 1, temp);
          }
        }
      }

      String nextAvailableDate = findNextAvailableDate(dateStrings, systemDate);
      venue.setNextAvailable(nextAvailableDate);
    }
  }

  private int compareDates(String date1, String date2) {

    // Split the dates into parts
    String[] parts1 = date1.split("/");
    String[] parts2 = date2.split("/");

    // Parse the parts into integers
    int year1 = Integer.parseInt(parts1[2]);
    int month1 = Integer.parseInt(parts1[1]);
    int day1 = Integer.parseInt(parts1[0]);
    int year2 = Integer.parseInt(parts2[2]);
    int month2 = Integer.parseInt(parts2[1]);
    int day2 = Integer.parseInt(parts2[0]);

    // Compare the dates
    if (year1 != year2) { // If the years are different
      return year1 - year2;
    } else if (month1 != month2) { // If the months are different
      return month1 - month2;
    } else { // If the days are different
      return day1 - day2;
    }
  }

  public void printBookings(String venueCode) {
    // Check if there are any bookings
    Venue selectedVenue = null;
    // Find the venue based on the provided venue code
    for (Venue venue : venues) {
      if (venue.getCode().equals(venueCode)) { // If the venue code matches
        selectedVenue = venue;
        break;
      }
    }

    // If the venue doesn't exist
    if (selectedVenue == null) {
      MessageCli.PRINT_BOOKINGS_VENUE_NOT_FOUND.printMessage(venueCode);
      return;
    }

    // Check if there are bookings for the specified venue
    boolean bookingsExist = false;
    for (Booking booking : bookings) {
      if (venueCode.equals(booking.getCode())) {
        bookingsExist = true;
        break;
      }
    }

    // Print appropriate messages based on bookings existence
    MessageCli.PRINT_BOOKINGS_HEADER.printMessage(selectedVenue.getName());
    if (!bookingsExist) {
      MessageCli.PRINT_BOOKINGS_NONE.printMessage(selectedVenue.getName());
    } else { // Print the bookings that match the venue code
      for (Booking booking : bookings) {
        if (booking.getCode().equals(venueCode)) {
          MessageCli.PRINT_BOOKINGS_ENTRY.printMessage(
              booking.getBookingReference(), booking.getBookingDate());
        }
      }
    }
  }

  public void addCateringService(String bookingReference, CateringType cateringType) {

    // Create a Catering object
    Catering catering = new Catering(bookingReference, cateringType);

    // Check if there exists a booking reference
    boolean bookingFound = false;
    for (Booking book : bookings) {
      if (bookingReference.equals(book.getBookingReference())) {
        bookingFound = true;
        break;
      }
    }

    // If booking not found, print message and return
    if (!bookingFound) {
      MessageCli.SERVICE_NOT_ADDED_BOOKING_NOT_FOUND.printMessage("Catering", bookingReference);
      return;
    }

    // Add catering service to the ArrayList
    catering.addService();
    services.add(catering);
  }

  public void addServiceMusic(String bookingReference) {
    // Create a Music object
    Music music = new Music(bookingReference);

    // Search through the bookings array to find if there is a booking with the booking reference
    boolean bookingFound = false;
    for (Booking book : bookings) {
      if (bookingReference.equals(book.getBookingReference())) {
        bookingFound = true;
        break;
      }
    }

    // If booking not found, print message and return
    if (!bookingFound) {
      MessageCli.SERVICE_NOT_ADDED_BOOKING_NOT_FOUND.printMessage("Music", bookingReference);
      return;
    }

    music.addService();
    // Add music service to the ArrayList
    services.add(music);
  }

  public void addServiceFloral(String bookingReference, FloralType floralType) {
    // Create a Floral object
    Floral floral = new Floral(bookingReference, floralType);

    // Search through the bookings array to find if there is a booking with the booking reference
    boolean bookingFound = false;
    for (Booking book : bookings) {
      if (bookingReference.equals(book.getBookingReference())) {
        bookingFound = true;
        break;
      }
    }

    // If booking not found, print message and return
    if (!bookingFound) {
      MessageCli.SERVICE_NOT_ADDED_BOOKING_NOT_FOUND.printMessage("Floral", bookingReference);
      return;
    }

    // Output correct message and add the service
    floral.addService();
    services.add(floral);
  }

  public void viewInvoice(String bookingReference) {

    // Initialise all the variables
    int totalCost = 0;
    String capacity = "";
    String customerEmail = "";
    String bookingDate = "";
    String numberOfGuests = "";
    String thisVenue = "";
    String costOfVenue = "";
    String thissystemDate = systemDate;

    // Check if the booking reference exists in the booking array
    boolean bookingFound = false;
    for (Booking book : bookings) {
      if (book.getBookingReference().equals(bookingReference)) {
        bookingFound = true;
        break;
      }
    }

    // If booking not found, print message and return
    if (!bookingFound) {
      MessageCli.VIEW_INVOICE_BOOKING_NOT_FOUND.printMessage(bookingReference);
      return;
    }

    // Find the associated venue cost for the given booking reference
    for (Booking book : bookings) {
      if (book.getBookingReference().equals(bookingReference)) {
        for (Venue venue : venues) {
          if (venue.getCode().equals(book.getCode())) {
            costOfVenue = venue.getHireFeeInput();
            totalCost += Integer.parseInt(venue.getHireFeeInput());
            break; // No need to continue searching once cost is found
          }
        }
        break; // No need to continue searching once booking is found
      }
    }

    // Find the associated booking details for the given booking reference
    for (Booking book : bookings) {
      if (book.getBookingReference().equals(bookingReference)) {
        customerEmail = book.getCustomerEmail();
        capacity = book.getNumberOfGuests();
        bookingDate = book.getBookingDate();
        numberOfGuests = book.getNumberOfGuests();
        thisVenue = book.getCorrespondingVenueName(venues, book.getCode());
        thissystemDate = book.getSystemDate();
        break; // No need to continue searching once the booking is found
      }
    }

    // Print the invoice
    MessageCli.INVOICE_CONTENT_TOP_HALF.printMessage(
        bookingReference, customerEmail, thissystemDate, bookingDate, numberOfGuests, thisVenue);

    MessageCli.INVOICE_CONTENT_VENUE_FEE.printMessage(costOfVenue);

    // Iterate through the list of services
    for (Services service : services) {
      // Check if the service is associated with the given booking reference
      if (service.getBookingReference().equals(bookingReference)) {
        // Check the type of service
        if (service instanceof Catering) {
          Catering catering = (Catering) service;

          // Calculate the cost of the catering service
          int cateringCost =
              catering.getCateringType().getCostPerPerson() * Integer.parseInt(capacity);

          // Add the cost to the total cost
          totalCost += catering.getCateringType().getCostPerPerson() * Integer.parseInt(capacity);
          MessageCli.INVOICE_CONTENT_CATERING_ENTRY.printMessage(
              catering.getCateringType().getName(), String.valueOf(cateringCost));
        } else if (service instanceof Music) { // Check if the service is music
          totalCost += 500;
          MessageCli.INVOICE_CONTENT_MUSIC_ENTRY.printMessage("500");
        } else if (service instanceof Floral) { // Check if the service is floral
          Floral floral = (Floral) service;

          // Calculate the cost of the floral service
          int floralCost = floral.getCost();

          // Add the cost to the total cost
          totalCost += floral.getCost();

          // Print the floral entry
          MessageCli.INVOICE_CONTENT_FLORAL_ENTRY.printMessage(
              floral.getName(), String.valueOf(floralCost));
        }
      }
    }
    // Print the total cost
    MessageCli.INVOICE_CONTENT_BOTTOM_HALF.printMessage(String.valueOf(totalCost));
  }

  public String findNextAvailableDate(ArrayList<String> dates, String systemDate) {

    // Parse the system date
    int day = Integer.parseInt(systemDate.split("/")[0]);
    int month = Integer.parseInt(systemDate.split("/")[1]);
    int year = Integer.parseInt(systemDate.split("/")[2]);

    // Increment the system date by one day until we find a date that is not in the list
    while (true) {
      String formattedSystemDate = String.format("%02d/%02d/%04d", day, month, year);
      if (!dates.contains(formattedSystemDate)) {
        return formattedSystemDate;
      }
      // Increment the system date
      if (day < daysInMonth(month, year)) {
        day++;
      } else {
        day = 1;
        if (month < 12) {
          month++;
        } else {
          month = 1;
          year++;
        }
      }
    }
  }

  private int daysInMonth(int month, int year) {
    // Return the number of days in a month
    switch (month) {
      case 4:
      case 6:
      case 9:
      case 11:
        return 30;
      case 2:
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28; // Leap year
      default:
        return 31; // January, March, May, July, August, October, December
    }
  }
}

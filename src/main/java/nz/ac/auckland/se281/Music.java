package nz.ac.auckland.se281;

public class Music extends Services {

  public Music(String bookingReference) {
    super(bookingReference);
  }

  @Override
  public void addService() {
    MessageCli.ADD_SERVICE_SUCCESSFUL.printMessage("Music", bookingReference);
  }
}

package nz.ac.auckland.se281;

import nz.ac.auckland.se281.Types.CateringType;

public class Catering extends Services {

  private CateringType cateringType;

  public Catering(String bookingReference, CateringType cateringType) {
    super(bookingReference);
    this.cateringType = cateringType;
  }

  @Override
  public void addService() {
    MessageCli.ADD_SERVICE_SUCCESSFUL.printMessage(
        "Catering (" + cateringType.getName() + ")", bookingReference);
  }

  public CateringType getCateringType() {
    return cateringType;
  }
}

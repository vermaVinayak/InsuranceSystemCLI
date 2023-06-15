package nz.ac.auckland.se281.policies;

import nz.ac.auckland.se281.MessageCli;

public class Home extends Policy {

  // declare required information from client
  private String address = "";
  private boolean isRental = false;

  public Home(String[] options) {
    super.sumInsured = Integer.parseInt(options[0]);
    this.address = options[1];
    this.isRental = options[2].equals("yes") ? true : false;
  }

  // setters
  public void setHomeAddress(String address) {
    this.address = address;
  }

  public void setIsRental(boolean isRental) {
    this.isRental = isRental;
  }

  // getters
  public String getHomeAddress() {
    return this.address;
  }

  public boolean getIsRental() {
    return this.isRental;
  }

  @Override
  public int getBasePremium(int clientAge) {
    // calculate and return base premium for client.
    if (this.isRental) {
      // return 2% of sumInsured
      return (super.sumInsured * 2) / 100;
    }
    // else return 1% of sumInsured
    return super.sumInsured / 100;
  }

  @Override
  public void introduce(int numPolicies, int clientAge) {
    // prints info about the policy
    MessageCli.PRINT_DB_HOME_POLICY.printMessage(
        this.address,
        Integer.toString(super.sumInsured),
        Integer.toString(getBasePremium(clientAge)),
        Integer.toString(super.getDiscountedPremium(numPolicies, getBasePremium(clientAge))));
  }
}

package nz.ac.auckland.se281.policies;

import nz.ac.auckland.se281.MessageCli;

public class Car extends Policy {
  // initial required information and their placeholders
  private String carModel = "";
  private String licensePlate = "";
  private boolean isCoveredForBreakdown = false;

  public Car(String[] options) {
    super.sumInsured = Integer.parseInt(options[0]);
    this.carModel = options[1];
    this.licensePlate = options[2];
    this.isCoveredForBreakdown = options[3].equals("yes") ? true : false;
  }
  // getters
  @Override
  public int getBasePremium(int clientAge) {
    // calculates and returns the base premium for car policy
    if (clientAge < 25) {
      // calculate also considering breakdown coverage
      return this.isCoveredForBreakdown
          ? ((super.sumInsured * 15) / 100) + 80
          : (super.sumInsured * 15) / 100;
    } else {
      // handle client with age 25 or older and calculate premium considering breakdown coverage
      return this.isCoveredForBreakdown
          ? ((super.sumInsured * 10) / 100) + 80
          : (super.sumInsured * 10) / 100;
    }
  }

  @Override
  public void introduce(int numPolicies, int clientAge) {
    // prints info about the policy
    MessageCli.PRINT_DB_CAR_POLICY.printMessage(
        this.carModel,
        Integer.toString(super.sumInsured),
        Integer.toString(this.getBasePremium(clientAge)),
        Integer.toString(super.getDiscountedPremium(numPolicies, getBasePremium(clientAge))));
  }
}

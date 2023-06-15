package nz.ac.auckland.se281.policies;

import nz.ac.auckland.se281.MessageCli;

public class Life extends Policy {

  public Life(String[] options) {
    super.sumInsured = Integer.parseInt(options[0]);
  }

  // getters
  @Override
  public int getBasePremium(int clientAge) {
    // basePremium = (1 + age/100)% of sum-insured
    float percentage = (1 + (float) clientAge / 100) / 100;
    float basePremium = percentage * super.sumInsured;
    return (int) basePremium;
  }

  @Override
  public void introduce(int numPolicies, int clientAge) {
    // prints info about the policy
    MessageCli.PRINT_DB_LIFE_POLICY.printMessage(
        Integer.toString(super.sumInsured),
        Integer.toString(getBasePremium(clientAge)),
        Integer.toString(super.getDiscountedPremium(numPolicies, getBasePremium(clientAge))));
  }
}

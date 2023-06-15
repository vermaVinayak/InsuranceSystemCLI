package nz.ac.auckland.se281.policies;

public abstract class Policy {
  // gives brief report on policy subscribed by the uer
  public abstract void introduce(int numPolicies, int clientAge);

  // provides base premium before any applied discounts
  public abstract int getBasePremium(int clientAge);

  protected int sumInsured; // shared across all child polices

  public int getDiscountedPremium(int numPolicies, int basePremium) {
    // returns the new discounted base premium
    if (numPolicies == 2) {
      return (basePremium * 90) / 100;
    } else if (numPolicies >= 3) {
      return (basePremium * 80) / 100;
    } else {
      return basePremium;
    }
  }
}

package nz.ac.auckland.se281;

import java.util.ArrayList;
import nz.ac.auckland.se281.policies.Life;
import nz.ac.auckland.se281.policies.Policy;

public class Client {
  private String name;
  private String age;
  private boolean loaded = false; // false initially
  private ArrayList<Policy> policies;

  public Client(String name, String age) {
    // constructor to create the person and store its bio.
    this.name = name;
    this.age = age;
    this.loaded = loaded;
    this.policies = new ArrayList<Policy>();
  }

  // getters
  public String getName() {
    return this.name;
  }

  public String getAge() {
    return this.age;
  }

  public boolean getLoadedStatus() {
    return this.loaded;
  }

  public int getPolicyCount() {
    // returns the amount of policies subscribed by the client
    return this.policies.size();
  }

  public void printPolicies() {
    // prints the details of all the policies subscribed by the client.
    int numPolicies = this.policies.size();
    for (int i = 0; i < numPolicies; i++) {
      policies.get(i).introduce(numPolicies, Integer.parseInt(age));
    }
  }

  public boolean hasLifePolicy() {
    // returns true if client has already subscribed to a life policy.
    for (Policy policy : this.policies) {
      if (policy instanceof Life) {
        return true;
      }
    }
    return false;
  }

  public int getSumOfDiscountedPremiums() {
    // loops over all polices and sums their discounted premiums
    int sum = 0;
    for (Policy policy : this.policies) {
      sum +=
          policy.getDiscountedPremium(
              this.getPolicyCount(), policy.getBasePremium(Integer.parseInt(this.age)));
    }
    return sum;
  }
  // setters
  public void setLoadStatus(boolean loadStatus) {
    this.loaded = loadStatus;
  }

  public void addPolicy(Policy policy) {
    // add policy to client
    this.policies.add(policy);
  }
}

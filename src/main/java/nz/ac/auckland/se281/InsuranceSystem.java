package nz.ac.auckland.se281;

import java.util.ArrayList;
import nz.ac.auckland.se281.Main.PolicyType;
import nz.ac.auckland.se281.policies.*;

public class InsuranceSystem {

  // Database for the insurance system.
  ArrayList<Client> database;

  public InsuranceSystem() {
    // instantiate the database.
    this.database = new ArrayList<Client>();
  }

  public void printDatabase() {
    // get number of registered clients.
    int databaseSize = this.database.size();

    // check the amount of clients present in the database and reply accordingly.
    switch (databaseSize) {
      case 0:
        MessageCli.PRINT_DB_POLICY_COUNT.printMessage(Integer.toString(databaseSize), "s", ".");
        break;
      default:
        printClientData(databaseSize);
    }
  }

  public void createNewProfile(String userName, String age) {
    // check whether any profiles are loaded
    for (int i = 0; i < this.database.size(); i++) {
      if (this.database.get(i).getLoadedStatus()) {
        // print error message and exit method
        MessageCli.CANNOT_CREATE_WHILE_LOADED.printMessage(this.database.get(i).getName());
        return;
      }
    }
    // change userName to titlecase
    userName = toTitleCase(userName);

    // check whether username and age is valid
    if (validUsername(userName) & validAge(userName, age)) {
      this.database.add(new Client(userName, age));
      MessageCli.PROFILE_CREATED.printMessage(userName, age);
    }
  }

  public void loadProfile(String userName) {
    // change userName to titleCase
    userName = toTitleCase(userName);

    // search for name in database
    for (int i = 0; i < this.database.size(); i++) {
      if (this.database.get(i).getName().equals(userName)) {
        // before loading anything set all profiles to unloaded.
        this.database = unloadAllProfiles(this.database);
        // change client profile status to loaded.
        this.database.get(i).setLoadStatus(true);
        // exit method with success message
        MessageCli.PROFILE_LOADED.printMessage(userName);
        return;
      }
    }
    // profile not found so exit with not found error
    MessageCli.NO_PROFILE_FOUND_TO_LOAD.printMessage(userName);
  }

  public void unloadProfile() {
    // check whether any profiles are loaded
    for (int i = 0; i < this.database.size(); i++) {
      if (this.database.get(i).getLoadedStatus()) {
        // unload profile and print confirmation
        this.database.get(i).setLoadStatus(false);
        MessageCli.PROFILE_UNLOADED.printMessage(this.database.get(i).getName());
        return;
      }
    }
    // no loaded profile detected so print the below message.
    MessageCli.NO_PROFILE_LOADED.printMessage();
  }

  public void deleteProfile(String userName) {
    // change userName to titlecase
    userName = toTitleCase(userName);

    // variable to detect whether deleted file exits
    boolean fileExists = false; // placeholder

    // get index of profile we want to remove
    int i;

    for (i = 0; i < this.database.size(); i++) {
      if (this.database.get(i).getName().equals(userName)) {
        fileExists = true;
        break;
      }
    }
    // delete file only if it exists
    if (fileExists) {
      // only delete profile if not loaded
      if (this.database.get(i).getLoadedStatus()) {
        // do not delete
        MessageCli.CANNOT_DELETE_PROFILE_WHILE_LOADED.printMessage(this.database.get(i).getName());
      } else {
        // delete profile and print confirmation
        MessageCli.PROFILE_DELETED.printMessage(this.database.get(i).getName());
        this.database.remove(i);
      }
    } else {
      MessageCli.NO_PROFILE_FOUND_TO_DELETE.printMessage(userName);
    }
  }

  public void createPolicy(PolicyType type, String[] options) {

    // check if a client profile is loaded.
    int loadedClientIndex = getLoadedClientIndex();
    if (loadedClientIndex == -1) {
      // no client is loaded, print error message and exit
      MessageCli.NO_PROFILE_FOUND_TO_CREATE_POLICY.printMessage();
      return;
    }

    // create and add policy to a client
    Client clientUpdt = this.database.get(loadedClientIndex);
    switch (type) {
      case HOME:
        clientUpdt.addPolicy(new Home(options));
        this.database.set(loadedClientIndex, clientUpdt);
        MessageCli.NEW_POLICY_CREATED.printMessage(
            "home", this.database.get(loadedClientIndex).getName());
        break;
      case CAR:
        clientUpdt.addPolicy(new Car(options));
        this.database.set(loadedClientIndex, clientUpdt);
        MessageCli.NEW_POLICY_CREATED.printMessage(
            "car", this.database.get(loadedClientIndex).getName());
        break;
      case LIFE:
        // check for age restriction
        int clientAge = Integer.parseInt(this.database.get(loadedClientIndex).getAge());
        if (clientAge > 100) {
          // throw error message and exit method
          MessageCli.OVER_AGE_LIMIT_LIFE_POLICY.printMessage(
              this.database.get(loadedClientIndex).getName());
          return;
        }
        // check for one life policy per person restriction.
        else if (this.database.get(loadedClientIndex).hasLifePolicy()) {
          // throw error message and exit method
          MessageCli.ALREADY_HAS_LIFE_POLICY.printMessage(
              this.database.get(loadedClientIndex).getName());
          return;
        }
        clientUpdt.addPolicy(new Life(options));
        this.database.set(loadedClientIndex, clientUpdt);
        MessageCli.NEW_POLICY_CREATED.printMessage(
            "life", this.database.get(loadedClientIndex).getName());
        break;
      default:
        System.out.println("Default case triggered!");
        break;
    }
  }

  // helper functions
  private boolean validUsername(String userName) {
    // check whether username exists in the database.
    for (Client client : this.database) {
      if (client.getName().equals(userName)) {
        MessageCli.INVALID_USERNAME_NOT_UNIQUE.printMessage(userName);
        return false;
      }
    }
    // check whether username is too short.
    if (userName.length() < 3) {
      MessageCli.INVALID_USERNAME_TOO_SHORT.printMessage(userName);
      return false;
    }
    return true;
  }

  private boolean validAge(String userName, String age) {
    // check whether age is of appropirate type
    try {
      // convert parameter age to integer
      Integer.parseInt(age);
    } catch (Exception e) {
      MessageCli.INVALID_AGE.printMessage(age, userName);
      return false;
    }
    // check whether age is positive
    int ageInt = Integer.parseInt(age);
    if (ageInt <= 0) {
      MessageCli.INVALID_AGE.printMessage(age, userName);
      return false;
    }
    return true;
  }

  private String toTitleCase(String userName) {
    // modify username to make it more appropriate for database.
    userName = userName.toLowerCase();
    userName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
    return userName;
  }

  private ArrayList<Client> unloadAllProfiles(ArrayList<Client> clientDatabase) {
    // set all status of clients to unloaded
    for (int i = 0; i < clientDatabase.size(); i++) {
      clientDatabase.get(i).setLoadStatus(false);
    }
    return clientDatabase;
  }

  private int getLoadedClientIndex() {
    // returns the index to the currently loaded client index
    // If not client is loaded return -1
    for (int i = 0; i < this.database.size(); i++) {
      if (this.database.get(i).getLoadedStatus()) {
        return i;
      }
    }
    return -1;
  }

  private void printClientData(int databaseSize) {
    // handle if Header if database size > 1 client or not
    switch (databaseSize) {
      case 1:
        MessageCli.PRINT_DB_POLICY_COUNT.printMessage(Integer.toString(databaseSize), "", ":");
        break;
      default:
        MessageCli.PRINT_DB_POLICY_COUNT.printMessage(Integer.toString(databaseSize), "s", ":");
    }
    // placeholder for no. of polices subscribed by every client
    int clientPolicyCount;

    // print clients' list
    for (int i = 0; i < this.database.size(); i++) {
      clientPolicyCount = this.database.get(i).getPolicyCount();

      // check whether profile is loaded and print according
      boolean clientLoaded = this.database.get(i).getLoadedStatus();
      if (clientLoaded) {
        MessageCli.PRINT_DB_PROFILE_HEADER_LONG.printMessage(
            "*** ",
            Integer.toString(i + 1),
            this.database.get(i).getName(),
            this.database.get(i).getAge(),
            Integer.toString(this.database.get(i).getPolicyCount()),
            clientPolicyCount == 1 ? "y" : "ies",
            Integer.toString(this.database.get(i).getSumOfDiscountedPremiums()));
        // print client policies
        this.database.get(i).printPolicies();
      } else {
        MessageCli.PRINT_DB_PROFILE_HEADER_LONG.printMessage(
            "",
            Integer.toString(i + 1),
            this.database.get(i).getName(),
            this.database.get(i).getAge(),
            Integer.toString(this.database.get(i).getPolicyCount()),
            clientPolicyCount == 1 ? "y" : "ies",
            Integer.toString(this.database.get(i).getSumOfDiscountedPremiums()));
        // print client policies
        this.database.get(i).printPolicies();
      }
    }
  }
}

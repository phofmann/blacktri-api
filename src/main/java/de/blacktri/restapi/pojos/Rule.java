package de.blacktri.restapi.pojos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The rule resource represents a personalization rule, consisting of one or more condition resources.
 */
public class Rule {
  /**
   * Internal goal ID
   */
  private int id;
  /**
   * Display name of the rule
   */
  private String name;
  /**
   * The boolean operator to combine all related conditions. One out of:
   * <p/>
   * AND
   * OR
   */
  private String operation;
  /**
   * A list of related conditions resource objects.
   */
  private List<Condition> conditions;


  public Rule() {
  }

  public Rule(String name, String operation) {
    this.name = name;
    this.operation = operation;
  }

  public Map<String, Object> getRuleForRemoteCreation() {
    Map<String, Object> result = new HashMap<>();
    result.put("name", this.getName());
    result.put("operation", this.getOperation());
    return result;
  }

  public Map<String, Object> getRuleForRemoteUpdate() {
    Map<String, Object> result = new HashMap<>();
    result.put("name", this.getName());
    result.put("operation", this.getOperation());
    return result;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public List<Condition> getConditions() {
    return conditions;
  }
}

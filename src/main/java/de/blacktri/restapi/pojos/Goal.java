package de.blacktri.restapi.pojos;

import java.util.HashMap;
import java.util.Map;

/**
 * A goal is a criterion to attach a conversion event to a decision.
 * It typically represents a user action or the technical event that indicates a certain action.
 * Goals are assigned to projects. Most of them only apply to online/mobile based projects.
 */
public class Goal {
  /**
   * Internal goal ID
   */
  private int id;
  /**
   * Type of goal, one of the following:
   * <p/>
   * ENGAGEMENT: user clicks a link on the landingpage.
   * AFFILIATE: user klicks a link which is from a known affiliate network.
   * TARGETPAGE: user loads a page with a given URL. An asterisk (*) can be used as a wildcard in the URL.
   * LINKURL: user klicks a link with a given URL. An asterisk (*) can be used as a wildcard in the URL.
   * CUSTOMJS: user does something that triggers calling a certain predifined Javascript function.
   */
  private String type;
  /**
   * An optional parameter, needed for some of above goals:
   * <p/>
   * If goal is TARGETPAGE: URL of page that shall be opened.
   * If goal is LINKURL: URL of link that shall be clicked.
   */
  private String param;


  public Goal() {
  }

  public Goal(String type, String param) {
    this.type = type;
    this.param = param;
  }

  public Map<String, Object> getGoalForRemoteCreation() {
    Map<String, Object> result = new HashMap<>();
    result.put("type", this.getType());
    result.put("param", this.getParam());
    return result;
  }

  public Map<String, Object> getGoalForRemoteUpdate() {
    Map<String, Object> result = new HashMap<>();
    result.put("type", this.getType());
    result.put("param", this.getParam());
    return result;
  }


  public int getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getParam() {
    return param;
  }

  public void setParam(String param) {
    this.param = param;
  }
}

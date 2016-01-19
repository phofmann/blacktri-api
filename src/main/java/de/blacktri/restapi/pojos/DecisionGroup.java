package de.blacktri.restapi.pojos;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DecisionGroup extends BlackTriObject {

  /**
   * Internal decisiongroup ID
   */
  private int id;
  /**
   * Date and time when decisiongroup has been created
   */
  private Date createddate;
  /**
   * Name of the decisiongroup, does not need to be unique.
   */
  private String name;
  /**
   * Shows wether decisiongroup is running or not:
   * PAUSED
   * RUNNING
   */
  private String status;
  /**
   * Number of visitors that have participated in this decisiongroup.
   */
  private int visitors;
  /**
   * Number of conversions in this decisiongroup.
   */
  private int conversions;
  /**
   * Conversions divided by Visitors. If an A/B test is statistically significant, the conversion rate of the winning decision is returned here.
   */
  private float conversionrate;
  /**
   * The result of the decisiongroup.
   * NONE: The A/B test is not significant yet.
   * LOST: The control decision has the highest conversion rate.
   * WON: At least one decision has a higher conversion rate than the control.
   */
  private String result;
  /**
   * ID of the original decision.
   */
  private int originalid;
  /**
   * In case result=WON this field contains the ID of the winning decision (and -1 if result != WON).
   */
  private int winnerid;
  /**
   * In case result=WON this field contains the name of the winning decision (and 'NA' if result != WON)
   */
  private String winnername;
  /**
   * In case result=WON this field contains the improvement of conversion rate of the winning decision relative to the original decision (and -1 if result != WON)
   */
  private float uplift;

  public DecisionGroup() {
  }

  public DecisionGroup(String name) {
    this.name = name;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> result = new HashMap<>();
    addRequiredParameter(result, "name", this.getName());
    return result;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getCreateddate() {
    return createddate;
  }

  public void setCreateddate(Date createddate) {
    this.createddate = createddate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getVisitors() {
    return visitors;
  }

  public void setVisitors(int visitors) {
    this.visitors = visitors;
  }

  public int getConversions() {
    return conversions;
  }

  public void setConversions(int conversions) {
    this.conversions = conversions;
  }

  public float getConversionrate() {
    return conversionrate;
  }

  public void setConversionrate(float conversionrate) {
    this.conversionrate = conversionrate;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public int getOriginalid() {
    return originalid;
  }

  public void setOriginalid(int originalid) {
    this.originalid = originalid;
  }

  public int getWinnerid() {
    return winnerid;
  }

  public void setWinnerid(int winnerid) {
    this.winnerid = winnerid;
  }

  public String getWinnername() {
    return winnername;
  }

  public void setWinnername(String winnername) {
    this.winnername = winnername;
  }

  public float getUplift() {
    return uplift;
  }

  public void setUplift(float uplift) {
    this.uplift = uplift;
  }
}

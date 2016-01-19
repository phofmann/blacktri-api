package de.blacktri.restapi.pojos;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Project extends BlackTriObject {
  /**
   * Internal project ID
   */
  int id;
  /**
   * Type of project:
   * VISUAL: Visual A/B-test
   * SPLIT: Split-test
   * TEASERTEST: Teaser- or Headlinetest
   */
  public enum ProjectType {
    VISUAL, SPLIT, TEASERTEST
  }

  ProjectType type;
  /**
   * A URL representing the main original URL of a project. It is used to create a preview page and internal documentation.
   * <p/>
   * Syntactically invaid URLs (protocol missing etc.) will raise an error.
   */
  String mainurl;
  /**
   * Pattern (typically for a URL) which determines whether or not the project runs on a given URL or not. Oftentimes it is identical to mainurl.
   * <p/>
   * An asterisk (*) can be used as a wildcard in the URL.
   */
  String runpattern;
  /**
   * Date and time when project has been created
   */
  Calendar createddate;
  /**
   * Project does not collect after this datetime.
   */
  Calendar startdate;
  /**
   * Project does not collect before this datetime.
   */

  Calendar enddate;
  /**
   * Date when project has been restarted the last time
   */
  Calendar restartdate;
  /**
   * Number of days needed to make the project significant. In case this number can not be computed yet, this fields contains -1.
   */
  int remainingdays;
  /**
   * Name of the project, does not need to be unique.
   */
  String name;
  /**
   * Shows whether project is running or not:
   * <p/>
   * PAUSED
   * RUNNING
   */
  String status;
  /**
   * Number of visitors that have participated in this project.
   */
  int visitors;
  /**
   * Number of conversions in this project.
   */
  int conversions;
  /**
   * Conversions divided by Visitors. If an A/B test is statistically significant, the conversion rate of the winning decision is returned here.
   */
  float conversionrate;
  /**
   * The result of the project, meaningful for A/B-testing only.
   * <p/>
   * NA: Not applicable. The project is no A/B-test and has thus no result.
   * NONE: The A/B test is not significant yet.
   * LOST: The control decision has the highest conversion rate.
   * WON: At least one decision has a higher conversion rate than the control.
   */
  String result;
  /**
   * ID of the original decision.
   */
  int originalid;
  /**
   * In case result=WON this field contains the ID of the winning decision.
   */
  String winnerid;
  /**
   * In case result=WON this field contains the improvement of conversion rate of the winning decision relative to the original decision.
   */
  String uplift;
  /**
   * Tells wether autopilot is running or not.
   * <p/>
   * NA: Not applicable because the project is no A/B test and has thus no significance.
   * RUNNING: significant decisions are cut off from traffic and the best decision is delivered on end of test
   * PAUSED: all decisions are delivered equally distributed
   */
  String autopilot;
  /**
   * Value between 0 and 100. Determines the percentage of visitors to be allocated to this test. Default is 100.
   */
  int allocation;
  /**
   * (true|false) Determines wether or not an IP blacklisting for the asccount is applied for this project (default is true)
   */
  boolean ipblacklisting;
  /**
   * Determines how personalization is managed in this project:
   * <p/>
   * NONE: No personalization for this project.
   * COMPLETE: The project as a while is personalized, this means we have a personalized A/B test.
   * SINGLE: Each decision can be assigned it's own personalization rule. The original is not delivered anymore.
   * <p/>
   * The default is NONE. If this field is set to SINGLE or COMPLETE and personalization is not an available feature (see the account resource), then an error is raised.
   */
  String personalizationmode;
  /**
   * In case personalizationmode is set to COMPLETE, this value refers to the rule resource which determines wether the project is delivered to visitors or not.
   * <p/>
   * If the field is empty AND personalizationmode = COMPLETE, then an error is returned.
   */
  int ruleid;
  String winnername;
  String devicetype;

  public Project() {
  }

  public Project(ProjectType type, String mainurl, String runpattern, String name) {
    this.type = type;
    this.mainurl = mainurl;
    this.runpattern = runpattern;
    this.name = name;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> result = new HashMap<>();
    addRequiredParameter(result, "type", this.getType().toString());
    addRequiredParameter(result, "mainurl", this.getMainurl());
    addRequiredParameter(result, "runpattern", this.getRunpattern());
    addRequiredParameter(result, "name", this.getName());
    addOptionalParameter(result, "startdate", this.getStartdate());
    addOptionalParameter(result, "enddate", this.getEnddate());
    addOptionalParameter(result, "allocation", this.getAllocation());
    addOptionalParameter(result, "ipblacklisting", this.isIpblacklisting());
    addOptionalParameter(result, "personalizationmode", this.getPersonalizationmode());
    addOptionalParameter(result, "ruleid", this.getRuleid());
    return result;
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ProjectType getType() {
    return type;
  }

  public void setType(ProjectType type) {
    this.type = type;
  }

  public String getMainurl() {
    return mainurl;
  }

  public void setMainurl(String mainurl) {
    this.mainurl = mainurl;
  }

  public String getRunpattern() {
    return runpattern;
  }

  public void setRunpattern(String runpattern) {
    this.runpattern = runpattern;
  }


  public Calendar getCreateddate() {
    return createddate;
  }

  public void setCreateddate(Calendar createddate) {
    this.createddate = createddate;
  }

  public Calendar getStartdate() {
    return startdate;
  }

  public void setStartdate(Calendar startdate) {
    this.startdate = startdate;
  }

  public Calendar getEnddate() {
    return enddate;
  }

  public void setEnddate(Calendar enddate) {
    this.enddate = enddate;
  }


  public Calendar getRestartdate() {
    return restartdate;
  }

  public void setRestartdate(Calendar restartdate) {
    this.restartdate = restartdate;
  }


  public int getRemainingdays() {
    return remainingdays;
  }

  public void setRemainingdays(int remainingdays) {
    this.remainingdays = remainingdays;
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


  public String getWinnerid() {
    return winnerid;
  }

  public void setWinnerid(String winnerid) {
    this.winnerid = winnerid;
  }


  public String getUplift() {
    return uplift;
  }

  public void setUplift(String uplift) {
    this.uplift = uplift;
  }


  public String getAutopilot() {
    return autopilot;
  }

  public void setAutopilot(String autopilot) {
    this.autopilot = autopilot;
  }

  public int getAllocation() {
    return allocation;
  }

  public void setAllocation(int allocation) {
    this.allocation = allocation;
  }

  public boolean isIpblacklisting() {
    return ipblacklisting;
  }

  public void setIpblacklisting(boolean ipblacklisting) {
    this.ipblacklisting = ipblacklisting;
  }

  public String getPersonalizationmode() {
    return personalizationmode;
  }

  public void setPersonalizationmode(String personalizationmode) {
    this.personalizationmode = personalizationmode;
  }

  public int getRuleid() {
    return ruleid;
  }

  public void setRuleid(int ruleid) {
    this.ruleid = ruleid;
  }


  public String getWinnername() {
    return winnername;
  }

  public void setWinnername(String winnername) {
    this.winnername = winnername;
  }

  public String getDevicetype() {
    return devicetype;
  }

  public void setDevicetype(String devicetype) {
    this.devicetype = devicetype;
  }
}

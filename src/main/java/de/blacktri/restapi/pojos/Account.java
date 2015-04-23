package de.blacktri.restapi.pojos;

import java.util.Calendar;
import java.util.List;

public class Account {

  /**
   * Internal account ID
   */
  private int id;
  /**
   * Optional custom account ID for user system
   */
  private String subid;
  /**
   * Publicly used account ID string (e.g. in tracking code). Must be unique among all accounts. If no publicid is provided when the account is created, an ID is assigned automatically and can be read from the resource after creation.
   */
  private String publicid;
  /**
   * Email address. When set it must be unique for all accounts of a tenant.
   */
  private String email;
  /**
   * The account's password.
   */
  private String password;
  /**
   * Key of API user. The system creates an API key for every new account.
   */
  private String apikey;

  /**
   * The account's API secret. After creation of the account it is empty and needs to be set by the API tenant.
   */
  private String apisecret;

  /**
   * Indicates email has been verified by a double opt-in mail.
   */
  private int emailvalidated;
  /**
   * Custom field.
   */
  private String custom1;

  /**
   * Custom field.
   */
  private String custom2;
  /**
   * Custom field.
   */
  private String custom3;
  /**
   * An array indicating which features of BlackTri Optimizer the account has permissions for. This can be used to create "packages" or "plans" with certain sets of features.
   * <p/>
   * See the table below for possible values.
   */
  private List features;

  /**
   * First name of account user.
   */
  private String firstname;
  /**
   * Last name of account user.
   */
  private String lastname;
  /**
   * Status of this account:
   * <p/>
   * EVALUATION: Accounts in evaluation phase can be assigned a quota, and this quota will not automatically be renewed each month.
   * <p/>
   * FULL: An account with full access and monthly renewed quota.
   * <p/>
   * HIBERNATED: The client can still log in to change some profile data and manage projects, but all projects are stopped.
   * <p/>
   * CANCELLED: The account has been cancelled, users can not log in or use the account in any other way.
   */
  private String status;
  /**
   * An ID indicating the user plan. There is no logic in BlackTri that makes use of it, it is more an identifier for the API-Tenant, who can set the quota or give access to
   */
  private int plan;

  /**
   * Date and time when the account has been created.
   */
  private Calendar createddate;
  /**
   * Available quota of unique visitors per month.
   */
  private int quota;
  /**
   * Amount of used quota in the current 30-day-period which starts each month on the day indicated in quota_reset_dayinmonth
   */
  private int usedquota;
  /**
   * Amount of monthly quota which will not be billed.
   */
  private int freequota;
  /**
   * Day in month (1-28) to reset the used quota to 0. Days 29 to 31 are not used in order to be able to use the same day in each month (even february).
   */

  private int quotaresetdayinmonth;
  /**
   * A semicolon separated list of IP address substrings which shall be excluded for visitors.
   */
  private String ipblacklist;
  /**
   * The BlackTri Tracking Code to be included in the client website.
   */
  private String trackingcode;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSubid() {
    return subid;
  }

  public void setSubid(String subid) {
    this.subid = subid;
  }

  public String getPublicid() {
    return publicid;
  }

  public void setPublicid(String publicid) {
    this.publicid = publicid;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getApikey() {
    return apikey;
  }

  public void setApikey(String apikey) {
    this.apikey = apikey;
  }

  public String getApisecret() {
    return apisecret;
  }

  public void setApisecret(String apisecret) {
    this.apisecret = apisecret;
  }

  public int getEmailvalidated() {
    return emailvalidated;
  }

  public void setEmailvalidated(int emailvalidated) {
    this.emailvalidated = emailvalidated;
  }

  public String getCustom1() {
    return custom1;
  }

  public void setCustom1(String custom1) {
    this.custom1 = custom1;
  }

  public String getCustom2() {
    return custom2;
  }

  public void setCustom2(String custom2) {
    this.custom2 = custom2;
  }

  public String getCustom3() {
    return custom3;
  }

  public void setCustom3(String custom3) {
    this.custom3 = custom3;
  }

  public List getFeatures() {
    return features;
  }

  public void setFeatures(List features) {
    this.features = features;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getPlan() {
    return plan;
  }

  public void setPlan(int plan) {
    this.plan = plan;
  }

  public Calendar getCreateddate() {
    return createddate;
  }

  public void setCreateddate(Calendar createddate) {
    this.createddate = createddate;
  }

  public int getQuota() {
    return quota;
  }

  public void setQuota(int quota) {
    this.quota = quota;
  }

  public int getUsedquota() {
    return usedquota;
  }

  public void setUsedquota(int usedquota) {
    this.usedquota = usedquota;
  }

  public int getFreequota() {
    return freequota;
  }

  public void setFreequota(int freequota) {
    this.freequota = freequota;
  }

  public int getQuotaresetdayinmonth() {
    return quotaresetdayinmonth;
  }

  public void setQuotaresetdayinmonth(int quotaresetdayinmonth) {
    this.quotaresetdayinmonth = quotaresetdayinmonth;
  }

  public String getIpblacklist() {
    return ipblacklist;
  }

  public void setIpblacklist(String ipblacklist) {
    this.ipblacklist = ipblacklist;
  }

  public String getTrackingcode() {
    return trackingcode;
  }

  public void setTrackingcode(String trackingcode) {
    this.trackingcode = trackingcode;
  }
}

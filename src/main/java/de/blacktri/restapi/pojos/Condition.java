package de.blacktri.restapi.pojos;

import java.util.HashMap;
import java.util.Map;

/**
 * The condition resource represents a personalization condition which is always part of a rule.
 */
public class Condition {
  /**
   * Internal account ID
   */
  private int id;
  /**
   * (true|false) Wether the condition or it's negation must be met:
   * <p/>
   * true: the condition's result will be negated before it gets returned
   * <p/>
   * false: teh conditions original value gets returned
   */
  private boolean negation;
  /**
   * The kind of condition that will be evaluated. Which types are available is depending on the tenant and server implementation. Currently one out of the following:
   * <p/>
   * REFERRER_CONTAINS: the given substring ist contained in the HTTP referrer.
   * URL_CONTAINS: the given substring ist contained in the URL or querystring.
   * SOURCE_IS: the referrer depicts that the visitor's traffic source is as specified.
   * IS_RETURNING: the visitor is a new visitor vs. a returning visitor.
   * SEARCH_IS: the traffic source is paid or organic search with one or more of the given keywords.
   * TARGETPAGE_OPENED: visitor has opened the given page URL one or more times. Asterisk can be used as wildcard in URL.
   * DEVICE_IS: visitor uses the given device.
   */
  private String type;
  /**
   * An argument, necessary for most of the rules (e.g. a substring for URL_CONTAINS). The following shows condition types where an argument is necessary, together with the allowed values:
   * <p/>
   * REFERRER_CONTAINS
   * string with the following validation: alphanumeric (numbers and letters) and the following characters: &_-
   * URL_CONTAINS
   * string with the following validation: alphanumeric (numbers and letters) and the following characters: &_-
   * SOURCE_IS: one of the following
   * TYPE_IN: no referrer
   * SOCIAL: social media
   * ORGANIC_SEARCH: organic search
   * PAID_SEARCH: paid earch
   * IS_RETURNING: one of the following
   * YES: is a returning visitor
   * NO: is new
   * SEARCH_IS
   * A string with one or more words, separated by blanks. Validation is: alphanumeric (numbers and letters) and blanks.
   * TARGETPAGE_OPENED
   * string with the following validation: alphanumeric (numbers and letters) and the following characters: &_-.*
   * DEVICE_IS: one of the following
   * MOBILE
   * TABLET
   * DESKTOP
   */
  private String arg1;

  public Condition() {
  }

  public Condition(boolean negation, String type, String arg1) {
    this.negation = negation;
    this.type = type;
    this.arg1 = arg1;
  }

  public Map<String, Object> getConditionForRemoteCreation() {
    Map<String, Object> result = new HashMap<>();
    result.put("negation", this.isNegation());
    result.put("type", this.getType());
    result.put("arg1", this.getArg1());
    return result;
  }

  public Map<String, Object> getConditionForRemoteUpdate() {
    Map<String, Object> result = new HashMap<>();
    result.put("negation", this.isNegation());
    result.put("type", this.getType());
    result.put("arg1", this.getArg1());

    return result;
  }

  public int getId() {
    return id;
  }

  public boolean isNegation() {
    return negation;
  }

  public void setNegation(boolean negation) {
    this.negation = negation;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getArg1() {
    return arg1;
  }

  public void setArg1(String arg1) {
    this.arg1 = arg1;
  }
}

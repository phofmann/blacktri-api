package de.blacktri.restapi.pojos;


import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * A decision is one out of a set of contents which can be delivered in a project. There is always one decision called the "original" or "control" decision which is the entity that shall be optimized, for example a web page. Additional variant decisions can be defined which are delivered alternatively. In an A/B test they are delivered together with the control in a random manner, in case of personalization they are delivered depending on wether personalization rules are matching or not.
 */

public class Decision extends BlackTriObject {
  /**
   * Internal decision ID
   */
  private int id;
  /**
   * Name of the decision, does not need to be unique.
   */
  private String name;
  /**
   * Only relevant for Split-Test variants: contains the URL that shall be called instead of the control.
   */
  private String url;
  /**
   * A URL which opens a preview of the decision.
   */
  private String previewurl;
  /**
   * The type of decision:
   * <p/>
   * CONTROL
   * VARIANT
   * <p/>
   * The type can not be set. When creating a project, the control decision is created automatically. All additional decisions are of type VARIANT.
   */
  private String type;
  /**
   * For projects where
   * <p/>
   * personalizationmode is set to SINGLE
   * AND the type of this decision is VARIANT
   * <p/>
   * this field refers to to the rule resource which determines wether the decision is delivered to visitors or not. If the field is empty, the decision is not personalized.
   */
  private int ruleid;
  /**
   * The result/status that this variant has in an A/B test:
   * <p/>
   * NA: Not applicable since the project is no A/B test
   * NONE: The status is not known since confidence is not significant yet.
   * LOST:
   * If the decision is the control: There is at least one variant decision with a higher conversion rate.
   * If the decision is a variant: The control has a higher conversion rate than this decision.
   * WON:
   * If the decision is the control: There is no variant decision with a higher conversion rate.
   * If the decision is a variant: The control has a lower conversion rate than this decision.
   */
  private String result;
  /**
   * Number of visitors that have participated in the project and been delivered the decision.
   */
  private int visitors;
  /**
   * Number of visitors that have participated in the project and been delivered the decision and converted subsequently.
   */
  private int conversions;
  /**
   * Conversion rate of this decision (conversions / visitors).
   */
  float conversionrate;
  /**
   * A value between 0.5 and 1 representing the probability that the result is not by accident. In case the confidence can not be calculated it is 0.
   */
  float confidence;
  /**
   * A value between 0 and 1 representing the probability this decision will be delivered to a visitor.
   */
  float distribution;
  /**
   * Javascript code to be injected into a webpage to visualize the content of the decision. This is only applicable for projects of type VISUAL.
   */
  private String jsinjection;
  /**
   * CSS code to be injected into a webpage to visualize the content of the decision. This is only applicable for projects of type VISUAL.
   */
  private String cssinjection;

  /**
   * Currently required due to a bug in remote API.
   */
  @JsonIgnore
  String runpattern;

  public Decision() {
  }

  public Decision(String name) {
    this.name = name;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> result = new HashMap<>();
    addRequiredParameter(result, "name", this.getName());
    addOptionalParameter(result, "jsinjection", this.getJsinjection());
    addOptionalParameter(result, "cssinjection", this.getCssinjection());
    addOptionalParameter(result, "url", this.getUrl());
    addOptionalParameter(result, "ruleid", this.getRuleid());
    return result;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getPreviewurl() {
    return previewurl;
  }

  public void setPreviewurl(String previewurl) {
    this.previewurl = previewurl;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getRuleid() {
    return ruleid;
  }

  public void setRuleid(int ruleid) {
    this.ruleid = ruleid;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
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

  public float getConfidence() {
    return confidence;
  }

  public void setConfidence(float confidence) {
    this.confidence = confidence;
  }

  public float getDistribution() {
    return distribution;
  }

  public void setDistribution(float distribution) {
    this.distribution = distribution;
  }

  public String getJsinjection() {
    return jsinjection;
  }

  public void setJsinjection(String jsinjection) {
    this.jsinjection = jsinjection;
  }

  public String getCssinjection() {
    return cssinjection;
  }

  public void setCssinjection(String cssinjection) {
    this.cssinjection = cssinjection;
  }

  @JsonIgnore
  public String getRunpattern() {
    return runpattern;
  }

  @JsonIgnore
  public void setRunpattern(String runpattern) {
    this.runpattern = runpattern;
  }

  @Override
  public String toString() {
    return "Decision{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", visitors=" + visitors +
            ", conversions=" + conversions +
            '}';
  }
}
 
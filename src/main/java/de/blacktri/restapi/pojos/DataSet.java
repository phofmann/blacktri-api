package de.blacktri.restapi.pojos;

public class DataSet {
  /**
   * The name of the decision.
   */
  private String name;
  /**
   * An array of int. Each value represents the number of impressions for the decision on the corresponding timestamp.
   */
  private int impressions;
  /**
   * An array of int. Each value represents the number of conversions for the decision on the corresponding timestamp.
   */
  private int conversions;

  /**
   * An array of float. Each value represents the conversion rate for the decision on the corresponding timestamp, aggregated over all impressions and conversions since starting or restarting the project.
   */
  private float aggregatedcr;

  private String values;

  public String getName() {
    return name;
  }

  public int getImpressions() {
    return impressions;
  }

  public int getConversions() {
    return conversions;
  }

  public float getAggregatedcr() {
    return aggregatedcr;
  }

  public String getValues() {
    return values;
  }
}

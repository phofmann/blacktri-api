package de.blacktri.restapi.pojos;

import java.util.List;

/**
 * The trend resource represents an array of data sets, each containing impressions and conversions of a decision resource in a given period of time. The trend resource can thus be used to populate charts or create custom reports.
 */
public class Trend {
  /**
   * An array of datetime-objects, each representing the sample date and time of one datapoint. Currently each timestamp represents one day.
   */
  private List timestamps;

  /**
   * An array of dataset objects. Each represents one decision in a project. The first dataset is by convention the original ("control").
   */
  private List<DataSet> datasets;

  public List getTimestamps() {
    return timestamps;
  }

  public List<DataSet> getDatasets() {
    return datasets;
  }
}

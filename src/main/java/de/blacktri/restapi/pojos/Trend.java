package de.blacktri.restapi.pojos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * The trend resource represents an array of data sets, each containing impressions and conversions of a decision
 * resource in a given period of time. The trend resource can thus be used to populate charts or create custom reports.
 */
public class Trend {
  private List<TrendEntry> trend = new ArrayList<>();

  public List<TrendEntry> getTrend() {
    return trend;
  }

  public Trend(Map<Calendar, Map<String, DataSet>> trend) {
    for (Map.Entry<Calendar, Map<String, DataSet>> calendarMapEntry : trend.entrySet()) {
      this.trend.add(new TrendEntry(calendarMapEntry.getKey(), calendarMapEntry.getValue()));

    }
  }

  public class TrendEntry {
    private Calendar day;
    private List<Experiment> experiments = new ArrayList<>();

    public TrendEntry(Calendar day, Map<String, DataSet> results) {
      this.day = day;
      for (Map.Entry<String, DataSet> entry : results.entrySet()) {
        this.experiments.add(new Experiment(entry.getKey(), entry.getValue()));
      }
    }

    public Calendar getDay() {
      return day;
    }

    public List<Experiment> getExperiments() {
      return experiments;
    }
  }

  public class Experiment {
    String id;
    DataSet dataSet;

    public Experiment(String id, DataSet dataSet) {
      this.id = id;
      this.dataSet = dataSet;
    }

    public String getId() {
      return id;
    }

    public DataSet getDataSet() {
      return dataSet;
    }
  }
}

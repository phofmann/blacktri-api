package de.blacktri.restapi;

import de.blacktri.restapi.httpclient.ABTestingRestConnector;
import de.blacktri.restapi.pojos.Account;
import de.blacktri.restapi.pojos.DataSet;
import de.blacktri.restapi.pojos.Decision;
import de.blacktri.restapi.pojos.Goal;
import de.blacktri.restapi.pojos.Project;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:/framework/spring/ab-testing-services.xml"
})
public class ABTestTest {
  private static final String API_KEY = "cr_productdemo";
  private static final String API_SECRET = "4zdget465er3z4";

  @Inject
  ABTestingRestConnector restConnector;

  int clientId;

  private ABTest testling;

  @Test
  @Ignore
  public void testLogin() throws Exception {
    int clientId = getTestling().login("api-client");
    int clientId2 = testling.loginClient();
    Assert.assertEquals(clientId, clientId2);
  }

  @Test
  @Ignore
  public void testGetAccount() {
    Account account = getTestling().getAccount(clientId);
    Assert.assertNotNull(account);
  }

  @Test
  @Ignore
  public void testCreateProject() {
    int projectId = getTestling().createProject(clientId, getProject());
    Project project = getTestling().getProject(clientId, projectId);
    project.setName("Blub");
    getTestling().updateProject(clientId, projectId, project);
    Project project2 = getTestling().getProject(clientId, projectId);
    Assert.assertEquals(project2.getName(), "Blub");
    getTestling().deleteProject(clientId, projectId);
  }

  @Test
  @Ignore
  public void testStartStopRestartProject() {
    int projectId = getTestling().createProject(clientId, getProject());
    getTestling().startProject(clientId, projectId);
    getTestling().stopProject(clientId, projectId);
    getTestling().startAutopilot(clientId, projectId);
    getTestling().stopAutopilot(clientId, projectId);
    getTestling().restartProject(clientId, projectId);
    getTestling().deleteProject(clientId, projectId);

  }

  @Test
  @Ignore
  public void testDeleteAllProjects() {
    List<Project> projects = getTestling().getProjects(clientId);
    for (Project project : projects) {
      getTestling().deleteProject(clientId, project.getId());
    }
  }

  @Test
  @Ignore
  public void testGetTrend() {
    ABTest test = getTestling();
    List<Project> projects = test.getProjects(clientId);
    for (Project project : projects) {
      Map<Calendar, Map<String, DataSet>> trend = test.getTrend(clientId, project.getId(), null, 1, -1);
      for (Map.Entry<Calendar, Map<String, DataSet>> calendarMapEntry : trend.entrySet()) {
        System.out.println("For " + ABTestingRestConnector.DATE_FORMAT.format(calendarMapEntry.getKey().getTime()));
        for (Map.Entry<String, DataSet> o : calendarMapEntry.getValue().entrySet()) {
          System.out.println(o.getKey() + ": Name: " + o.getValue().getName() + " Conversion: " + o.getValue().getConversions() + " Impressions: " + o.getValue().getImpressions() + " Aggregatedcr: " + o.getValue().getAggregatedcr());

        }
      }
    }
  }


  @Test
  @Ignore
  public void playground() {
    ABTest test = getTestling();
    int projectId = test.createProject(clientId, getProject());
    getTestling().createGoal(clientId, projectId, new Goal("ENGAGEMENT", "NA"));
    Decision decision1 = new Decision("Green");
    decision1.setCssinjection(".cm-teaser--hero .cm-heading2--boxed {\n" +
            "    background-color: #00ff00;\n" +
            "}");
    test.createDecision(clientId, projectId, decision1);

    Decision decision2 = new Decision("Red");
    decision2.setCssinjection(".cm-teaser--hero .cm-heading2--boxed {\n" +
            "    background-color: #ff0000;\n" +
            "}");
    test.createDecision(clientId, projectId, decision2);


    test.startProject(clientId, projectId);

  }

  private Project getProject() {
    return new Project("VISUAL", "http://localhost:40081/blueprint/servlet/perfectchef", "*", "PerfectChef " + System.currentTimeMillis());
  }

  private ABTest getTestling() {
    if (testling == null) {
      this.testling = new ABTest(API_KEY, API_SECRET, restConnector);
      this.clientId = testling.loginClient();
    }
    return testling;
  }
}
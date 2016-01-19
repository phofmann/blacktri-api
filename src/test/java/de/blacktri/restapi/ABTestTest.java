package de.blacktri.restapi;

import de.blacktri.restapi.httpclient.ABTestingRestConnector;
import de.blacktri.restapi.pojos.Account;
import de.blacktri.restapi.pojos.Decision;
import de.blacktri.restapi.pojos.DecisionGroup;
import de.blacktri.restapi.pojos.Goal;
import de.blacktri.restapi.pojos.Project;
import de.blacktri.restapi.pojos.Trend;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

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
    int projectId = getTestling().createProject(clientId, getVisualProject());
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
    int projectId = getTestling().createProject(clientId, getVisualProject());
    getTestling().startProject(clientId, projectId);
    getTestling().stopProject(clientId, projectId);
    getTestling().startAutopilot(clientId, projectId);
    getTestling().stopAutopilot(clientId, projectId);
    getTestling().restartProject(clientId, projectId);
    getTestling().deleteProject(clientId, projectId);

  }

  @Test
  public void testTeaserTest() throws Exception {
    /*int projectId = getTestling().createProject(clientId, getTeaserTestProject());
    Goal goal1 = new Goal();
    goal1.setType(Goal.Type.TIMEONPAGE);
    goal1.setLevel(Goal.Level.SECONDARY);
    getTestling().createGoal(clientId, projectId, goal1);
    Goal goal2 = new Goal();
    goal2.setType(Goal.Type.CLICK);
    goal2.setLevel(Goal.Level.SECONDARY);
    getTestling().createGoal(clientId, projectId, goal2);
    Goal goal3 = new Goal();
    goal3.setType(Goal.Type.COMBINED);
    goal3.setLevel(Goal.Level.PRIMARY);
    getTestling().createGoal(clientId, projectId, goal3);
    getTestling().startProject(clientId, projectId);*/

    List<DecisionGroup> groups = getTestling().getDecisionGroups(clientId, 1090, null);

    for (DecisionGroup group : groups) {
      System.out.println(group);
    }
/*
    DecisionGroup decisionGroup = new DecisionGroup("holy moly");
    getTestling().createDecisionGroup(clientId, projectId, decisionGroup);

    List<DecisionGroup> decisionGroups = getTestling().getDecisionGroups(clientId, projectId, null);
    for (DecisionGroup group : decisionGroups) {
      System.out.println(group);
      List<Decision> decisions = getTestling().getDecisions(clientId, projectId, group.getId(), null, null);

      for (Decision decision : decisions) {
        getTestling().deleteDecision(clientId, projectId, group.getId(), decision.getId());
      }


      getTestling().deleteDecisionGroup(clientId, projectId, group.getId());
    }
    getTestling().deleteProject(clientId, projectId);*/
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
      Trend trend = test.getTrend(clientId, project.getId(), null, 1, -1);
      for (Trend.TrendEntry entry : trend.getTrend()) {
        System.out.println("For " + ABTestingRestConnector.DATE_FORMAT.format(entry.getDay().getTime()));
        for (Trend.Experiment o : entry.getExperiments()) {
          System.out.println(o.getId() + ": Name: " + o.getDataSet().getName() + " Conversion: " + o.getDataSet().getConversions() + " Impressions: " + o.getDataSet().getImpressions() + " Aggregatedcr: " + o.getDataSet().getAggregatedcr());

        }
      }
    }
  }


  @Test
  @Ignore
  public void playground() {
    ABTest test = getTestling();
    int projectId = test.createProject(clientId, getVisualProject());
    getTestling().createGoal(clientId, projectId, new Goal(Goal.Type.ENGAGEMENT, "NA"));
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

  private Project getVisualProject() {
    return new Project(Project.ProjectType.VISUAL, "http://localhost:40081/blueprint/servlet/perfectchef", "*", "PerfectChef " + System.currentTimeMillis());
  }

  private Project getTeaserTestProject() {
    return new Project(Project.ProjectType.TEASERTEST, "http://livecontext.coremedia.com", "*", "Corporate Headline Project " + System.currentTimeMillis());
  }

  private ABTest getTestling() {
    if (testling == null) {
      this.testling = new ABTest(API_KEY, API_SECRET, restConnector);
      this.clientId = testling.loginClient();
    }
    return testling;
  }
}
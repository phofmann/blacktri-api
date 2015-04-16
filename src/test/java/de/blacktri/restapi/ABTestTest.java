package de.blacktri.restapi;

import de.blacktri.restapi.httpclient.ABTestingRestConnector;
import de.blacktri.restapi.pojos.Account;
import de.blacktri.restapi.pojos.Project;
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

  @Inject
  ABTestingRestConnector restConnector;

  int clientId;

  private ABTest testling;

  @Test
  @Ignore
  public void testLogin() throws Exception {
    int clientId = testling.login("api-client");
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
  public void testGetProjects() {
    Project testProject1 = new Project("VISUAL", "http://test.de", "pattern", "Testname1");
    int projectId = getTestling().createProject(clientId, testProject1);
    getTestling().startProject(clientId, projectId);
    List<Project> projects = getTestling().getProjects(clientId);
    for (Project project : projects) {
      getTestling().deleteProject(clientId, project.getId());
    }
  }

  @Test
  @Ignore
  public void testCreateProject() {
    Project testProject = new Project("VISUAL", "http://test.de", "pattern", "Testname");
    int projectId = getTestling().createProject(clientId, testProject);
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
    Project testProject = new Project("VISUAL", "http://test.de", "pattern", "Testname");
    int projectId = getTestling().createProject(clientId, testProject);
    getTestling().startProject(clientId, projectId);
    getTestling().stopProject(clientId, projectId);
    getTestling().startAutopilot(clientId, projectId);
    getTestling().stopAutopilot(clientId, projectId);
    getTestling().restartProject(clientId, projectId);
    getTestling().deleteProject(clientId, projectId);

  }

  private ABTest getTestling() {
    if (testling == null) {
      ABTest abTest = new ABTest();
      abTest.setRestConnector(restConnector);
      this.testling = abTest;
      this.clientId = testling.loginClient();
    }
    return testling;
  }
}
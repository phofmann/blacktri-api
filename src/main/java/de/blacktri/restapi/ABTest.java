package de.blacktri.restapi;

import de.blacktri.restapi.httpclient.ABTestingRestConnector;
import de.blacktri.restapi.pojos.Account;
import de.blacktri.restapi.pojos.Project;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides access to all available resources that can be found in the API
 * <p/>
 * Al methods included in this class are accessible by any type of user (tenant and clients) and works
 * as an abstraction layer for the API itself. It contains multiple methods that enables users to
 * Create, Read, Update and Delete resources associated to his/her account like projects,
 * decisions, personalization rules, goals or his/her account details.
 */
public class ABTest {

  private ABTestingRestConnector restConnector;


  @Required
  public void setRestConnector(ABTestingRestConnector connector) {
    this.restConnector = connector;
  }

  public ABTestingRestConnector getRestConnector() {
    return restConnector;
  }

  /**
   * Provides convenience login functionlality
   * <p/>
   * This method is a convenience method to retrieve the account id for a given API key / API secret.
   *
   * @param data contains the apikey/secret and the expected usertype ("client"|"tenant")
   * @return Object containing the account id
   */
  public int login(String data) {
    Map<String, Object> body = new HashMap<>();
    body.put("apikey", getRestConnector().getApiKey());
    body.put("apisecret", getRestConnector().getApiSecret());
    body.put("usertype", data);

    return getRestConnector().callService(HttpMethod.POST, "login", new TypeReference<Integer>() {
    }, Collections.<String, Object>emptyMap(), Collections.<String, String>emptyMap(), body);
  }


  public int loginClient() {
    return login("api-client");
  }

  public int loginTenant() {
    return login("api-tenant");
  }

  /**
   * Retrieves a particular account information
   * <p/>
   * If this method is called by a tenant, the provided parameter "clientId" has to match one of his clients,
   * if it is called by a client, if thas to match his own ID, otherwise, the API will throw an exception
   *
   * @param clientId - the user account id to retrieve the data for
   * @return Object Containing all the details for the specified account
   */
  public Account getAccount(int clientId) {
    return getRestConnector().callService(HttpMethod.GET, "account/" + clientId, new TypeReference<Account>() {
    }, Collections.<String, Object>emptyMap(), Collections.<String, String>emptyMap(), null);
  }

  /**
   * Returns an object with a list of projects and its corresponding data
   * <p/>
   * If the user is a tenant, this method get the information of all of the projects that he or his clients
   * have created.
   * If the user is a client, returns the projects that has been created himself.
   * <p/>
   * Results can be searched/filtered with the following querystring qualifiers that refer to values of the
   * resource attributes "type" and "status" (e.g, type=SPLIT status=RUNNING).
   * <p/>
   * The result list can be sorted by providing the qualifier sort with one of the project attributes.
   * A dash can be added to the attribute to indicate descending order (e.g. sort=-createddate).
   * <p/>
   * The fields contained in the result set can be determined or limited by providing a parameter "fields"
   * with a comma separated list of field names. (e.g. fields=id,name,mainurl,visitors,conversions)
   *
   * @param clientId - the user account id to retrieve the data for
   * @return Object Containing a list of project with their respective details
   */
  public List<Project> getProjects(int clientId) {
    return getProjects(clientId, null, null, null, Collections.<String>emptyList());
  }

  public List<Project> getProjects(int clientId, String type, String status, String sort, List<String> fields) {
    Map<String, Object> queryParameters = new HashMap<>();
    if (StringUtils.hasText(type)) {
      queryParameters.put("type", type);
    }
    if (StringUtils.hasText(status)) {
      queryParameters.put("status", status);
    }
    if (StringUtils.hasText(sort)) {
      queryParameters.put("sort", sort);
    }
    if (!fields.isEmpty()) {

      StringBuilder stringBuilder = new StringBuilder();
      for (String field : fields) {
        if (stringBuilder.length() > 0) {
          stringBuilder.append(",");
        }
        stringBuilder.append(field);
      }
      queryParameters.put("fields", stringBuilder.toString());
    }

    return getRestConnector().callService(HttpMethod.GET, "account/" + clientId + "/projects", new TypeReference<List<Project>>() {
    }, queryParameters, Collections.<String, String>emptyMap(), null);
  }

  /**
   * Creates a new project.
   * <p/>
   * Users can create project by calling this method with an array containing all required data including
   * the project type, mainurl, run pattern, name, etc.
   *
   * @param clientId - the user account id to retrieve the data for
   * @param project  contains all required fields to create a new project
   * @return Object Containing the new created project id
   */
  public Integer createProject(int clientId, Project project) {
    return getRestConnector().callService(HttpMethod.POST, "account/" + clientId + "/project", new TypeReference<Integer>() {
    }, Collections.<String, Object>emptyMap(), Collections.<String, String>emptyMap(), project.getProjectForRemoteCreation());
  }

  /**
   * Returns all data given a project id
   * <p/>
   * Receives a project ID as parameter and returns a JSON object with all the project
   * related data
   * <p/>
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The ID of the project to retrieve the data for
   * @return Object Containing the details for the specified project
   */
  public Project getProject(int clientId, int projectId) {
    return getRestConnector().callService(HttpMethod.GET, "account/" + clientId + "/project/" + projectId, new TypeReference<Project>() {
    }, Collections.<String, Object>emptyMap(), Collections.<String, String>emptyMap(), null);
  }

  /**
   * Deletes a project given a project ID
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId ID of the project to be deleted
   */
  public void deleteProject(int clientId, int projectId) {
    getRestConnector().callService(HttpMethod.DELETE, "account/" + clientId + "/project/" + projectId, null, Collections.<String, Object>emptyMap(), Collections.<String, String>emptyMap(), null);
  }

  /**
   * Updates a project information
   * <p/>
   * To update a project information, users have to pass the project id and the array of related data
   * to this method.
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId the ID of the project to be updated
   * @param project   contains all edited fields to be updated
   */
  public void updateProject(int clientId, int projectId, Project project) {
    getRestConnector().callService(HttpMethod.PUT, "account/" + clientId + "/project/" + projectId, null, Collections.<String, Object>emptyMap(), Collections.<String, String>emptyMap(), project.getProjectForRemoteUpdate());
  }

  /**
   * given a project ID, starts the project by updating the necessary fields in the DB (like setting the
   * status to 1)
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId the ID of the project to be started
   * @return Object Containing  the response from the server after trying to start the project
   */
  public Boolean startProject(int clientId, int projectId) {
    return getRestConnector().callService(HttpMethod.POST, "account/" + clientId + "/project/" + projectId + "/start", new TypeReference<Boolean>() {
    }, Collections.<String, Object>emptyMap(), Collections.<String, String>emptyMap(), null);
  }

  /**
   * given a project ID, stops the project by updating the necessary fields in the DB (like setting the
   * status to 0)
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId the ID of the project to be stopped
   * @return Object Containing the response from the server after trying to stop the project
   */
  public Boolean stopProject(int clientId, int projectId) {
    return getRestConnector().callService(HttpMethod.POST, "account/" + clientId + "/project/" + projectId + "/stop", new TypeReference<Boolean>() {
    }, Collections.<String, Object>emptyMap(), Collections.<String, String>emptyMap(), null);
  }

  /**
   * given a project ID, restarts the project by updating the necessary fields in the DB (like resetting
   * conversions, impressions, etc)
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId the ID of the project to be restarted
   * @return Object Containing  the response from the server after trying to restart the project
   */
  public Boolean restartProject(int clientId, int projectId) {
    return getRestConnector().callService(HttpMethod.POST, "account/" + clientId + "/project/" + projectId + "/restart", new TypeReference<Boolean>() {
    }, Collections.<String, Object>emptyMap(), Collections.<String, String>emptyMap(), null);
  }

  /**
   * given a project ID, starts the autopilot by setting its value to 1
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The ID of the project to start the autopilot for
   * @return Object Containing the response from the server after trying to start the autopilot for the project
   */
  public Object startAutopilot(int clientId, int projectId) {
    return getRestConnector().callService(HttpMethod.POST, "account/" + clientId + "/project/" + projectId + "/autopilot/start", new TypeReference<Boolean>() {
    }, Collections.<String, Object>emptyMap(), Collections.<String, String>emptyMap(), null);
  }

  /**
   * given a project ID, stops the autopilot by setting its value to 0
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The ID of the project to stop the autopilot for
   * @return Object Containing the response from the server after trying to stop the atopilot for the project
   */
  public Object stopAutopilot(int clientId, int projectId) {
    return getRestConnector().callService(HttpMethod.POST, "account/" + clientId + "/project/" + projectId + "/autopilot/stop", new TypeReference<Boolean>() {
    }, Collections.<String, Object>emptyMap(), Collections.<String, String>emptyMap(), null);
  }
}
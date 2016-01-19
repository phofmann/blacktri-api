package de.blacktri.restapi;

import de.blacktri.restapi.httpclient.ABTestingRestConnector;
import de.blacktri.restapi.pojos.Account;
import de.blacktri.restapi.pojos.Condition;
import de.blacktri.restapi.pojos.DataSet;
import de.blacktri.restapi.pojos.Decision;
import de.blacktri.restapi.pojos.DecisionGroup;
import de.blacktri.restapi.pojos.Goal;
import de.blacktri.restapi.pojos.Project;
import de.blacktri.restapi.pojos.Rule;
import de.blacktri.restapi.pojos.Trend;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import java.util.Calendar;
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


  private static final Logger LOG = LoggerFactory.getLogger(ABTest.class);

  public static final String ACCOUNT = "account/";
  public static final String PROJECT = "/project/";
  public static final String START = "/start";
  public static final String STOP = "/stop";
  public static final String RESTART = "/restart";
  public static final String AUTOPILOT = "/autopilot";
  public static final String DECISION = "/decision/";
  public static final String DECISIONS = "/decisions";
  public static final String GOAL = "/goal/";
  public static final String RULE = "/rule/";
  public static final String CONDITION = "/condition/";
  public static final String DECISIONGROUPS = "/decisiongroups";
  public static final String DECISIONGROUP = "/decisiongroup/";
  private ABTestingRestConnector restConnector;
  private String apiKey;
  private String apiSecret;

  public ABTest(String apiKey, String apiSecret, ABTestingRestConnector restConnector) {
    this.apiKey = apiKey;
    this.apiSecret = apiSecret;
    this.restConnector = restConnector;
    this.restConnector.setApiKey(apiKey);
    this.restConnector.setApiSecret(apiSecret);
  }

  public ABTestingRestConnector getRestConnector() {
    restConnector.setApiKey(apiKey);
    restConnector.setApiSecret(apiSecret);
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
    }, body);
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
    return getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId, new TypeReference<Account>() {
    }, null);
  }

  /**
   * Returns an object with a list of projects and its corresponding data
   * <p/>
   * If the user is a tenant, this method get the information of all of the projects that he or his clients
   * have created.
   * If the user is a client, returns the projects that has been created himself.
   * <p/>
   * Results can be searched/filtered with the following querystring qualifiers that refer to values of the
   * resource attributes "type" and "status" (e.g, type=SPLIT&status=RUNNING).
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

    return getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId + "/projects", new TypeReference<List<Project>>() {
    }, queryParameters, null);
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
    LOG.info("Creating project with name " + project.getName());
    Integer projectId = getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + "/project", new TypeReference<Integer>() {
    }, project.toMap());
    LOG.info("Created project '" + project.getName() + "' with id " + projectId);
    return projectId;
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
    return getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId + PROJECT + projectId, new TypeReference<Project>() {
    }, null);
  }

  /**
   * Deletes a project given a project ID
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId ID of the project to be deleted
   */
  public void deleteProject(int clientId, int projectId) {
    LOG.info("Deleting project with id " + projectId);
    getRestConnector().callService(HttpMethod.DELETE, ACCOUNT + clientId + PROJECT + projectId, null, null);
    LOG.info("Project with id " + projectId + " has been deleted");
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
    getRestConnector().callService(HttpMethod.PUT, ACCOUNT + clientId + PROJECT + projectId, null, project.toMap());
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
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + PROJECT + projectId + START, new TypeReference<Boolean>() {
    }, null);
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
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + PROJECT + projectId + STOP, new TypeReference<Boolean>() {
    }, null);
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
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + PROJECT + projectId + RESTART, new TypeReference<Boolean>() {
    }, null);
  }

  /**
   * given a project ID, starts the autopilot by setting its value to 1
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The ID of the project to start the autopilot for
   * @return Object Containing the response from the server after trying to start the autopilot for the project
   */
  public Object startAutopilot(int clientId, int projectId) {
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + PROJECT + projectId + AUTOPILOT + START, new TypeReference<Boolean>() {
    }, null);
  }

  /**
   * given a project ID, stops the autopilot by setting its value to 0
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The ID of the project to stop the autopilot for
   * @return Object Containing the response from the server after trying to stop the atopilot for the project
   */
  public Object stopAutopilot(int clientId, int projectId) {
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + PROJECT + projectId + AUTOPILOT + STOP, new TypeReference<Boolean>() {
    }, null);
  }


  /**
   * Get a list of decision_groups that the current user has created.
   * <p/>
   * It can be filtered and sorted by one or more of the available fields (e.g, status=RUNNING&sort=name).
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId - the ID of the project that the decision group belongs to
   * @param filter    - "&" separated key=value pairs to filter, sort and limit results
   * @return Object containig the list of created decision groups
   */
  public List<DecisionGroup> getDecisionGroups(int clientId, int projectId, String filter) {
    return getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId + PROJECT + projectId + DECISIONGROUPS, new TypeReference<List<DecisionGroup>>() {
    }, null);
  }

  /**
   * Returns all data given a project id and a group decision id
   * <p/>
   * Receives a project ID and a decision group id as parameters and returns a JSON object with
   * all of the decision group data
   *
   * @param clientId        - the user account id to retrieve the data for
   * @param projectId       - the ID of the project that the decision group belongs to
   * @param decisionGroupId - the decision group id
   * @return Object Containing the details for the specified project
   */
  public DecisionGroup getDecisionGroup(int clientId, int projectId, int decisionGroupId) {
    return getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId + PROJECT + projectId + DECISIONGROUP + decisionGroupId, new TypeReference<DecisionGroup>() {
    }, null);
  }

  /**
   * Creates a new decision group
   * <p/>
   * Users can create decision groups by calling this method with a project ID and an array containing
   * all required data for the decision group (name)
   *
   * @param clientId      - the user account id to retrieve the data for
   * @param projectId     - the ID of the project that the decision group belongs to
   * @param decisionGroup - containing mandatory fields ( array(name => 'groupname'); )
   * @return The new decision group ID
   */
  public Integer createDecisionGroup(int clientId, int projectId, DecisionGroup decisionGroup) {
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + PROJECT + projectId + DECISIONGROUP, new TypeReference<Integer>() {
    }, decisionGroup.toMap());
  }

  /**
   * Updates a decision group data
   * <p/>
   * Users can create decision groups by calling this method with a project ID and an array containing
   * all required data for the decision group (name)
   *
   * @param clientId      - the user account id to retrieve the data for
   * @param projectId     - the ID of the project that the decision group belongs to
   * @param decisionGroup - containing mandatory fields ( array(name => 'groupname'); )
   * @return The new decision group ID
   */
  public Object updateDecisionGroup(int clientId, int projectId, int decisionGroupId, DecisionGroup decisionGroup) {
    return getRestConnector().callService(HttpMethod.PUT, ACCOUNT + clientId + PROJECT + projectId + DECISIONGROUP + decisionGroupId, null, decisionGroup.toMap());
  }

  /**
   * Deletes a decision group given a project and a decision group ID
   *
   * @param clientId        - the user account id to retrieve the data for
   * @param projectId       - the ID of the project that the decision group belongs to
   * @param decisionGroupId The ID of the decision group to be deleted
   * @return Object Containing the response from the server after trying to delete the given decision
   */
  public Object deleteDecisionGroup(int clientId, int projectId, int decisionGroupId) {
    return getRestConnector().callService(HttpMethod.DELETE, ACCOUNT + clientId + PROJECT + projectId + DECISIONGROUP + decisionGroupId, null, null);
  }

  /**
   * given a project ID and a decision group ID, starts the group by updating the necessary fields in the DB
   * (like setting the status to "RUNNING")
   *
   * @param clientId        - the user account id to retrieve the data for
   * @param projectId       - the ID of the project that the decision group belongs to
   * @param decisionGroupId -  The ID of the decision group to be started
   * @return Object Containing  the response from the server after trying to start the group
   */
  public Object startDecisionGroup(int clientId, int projectId, int decisionGroupId) {
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + PROJECT + projectId + DECISIONGROUP + decisionGroupId + START, new TypeReference<Boolean>() {
    }, null);
  }

  /**
   * given a project ID and a decision group ID, stops the group by updating the necessary fields in the DB
   * (like setting the status to "PAUSED")
   *
   * @param clientId        - the user account id to retrieve the data for
   * @param projectId       - the ID of the project that the decision group belongs to
   * @param decisionGroupId -  The ID of the decision group to be paused
   * @return Object Containing the response from the server after trying to stop the group
   */
  public Object stopDecisionGroup(int clientId, int projectId, int decisionGroupId) {
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + PROJECT + projectId + DECISIONGROUP + decisionGroupId + STOP, new TypeReference<Boolean>() {
    }, null);
  }

  /**
   * given a project ID and a decision group ID, restarts the group by updating the necessary fields in the DB.
   *
   * @param clientId        - the user account id to retrieve the data for
   * @param projectId       - the ID of the project that the decision group belongs to
   * @param decisionGroupId -  The ID of the decision group to be restarted
   * @return Object Containing  the response from the server after trying to restart the group
   */
  public Object restartDecisionGroup(int clientId, int projectId, int decisionGroupId) {
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + PROJECT + projectId + DECISIONGROUP + decisionGroupId + RESTART, new TypeReference<Boolean>() {
    }, null);
  }


  private String getDecisionBasePath(int clientId, int projectId, int decisionsGroupId) {
    String path = ACCOUNT + clientId + PROJECT + projectId;
    if (decisionsGroupId != -1) {
      path += DECISIONGROUP + decisionsGroupId;
    }
    return path;
  }


  /**
   * Returns a list of decisions for the given project
   * <p/>
   * The result list can be sorted by providing the qualifier "sort" with one of the following attributes:
   * "name", "conversions", A dash can be added to the attribute to indicate descending order
   * (e.g. sort=-name).
   * The list can be searched/filtered with the following querystring qualifiers that refer to values of the
   * resource attributes: "result". (e.g. sort=-name&result=WON)
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The id of the project to retrieve all decisions from
   * @param sort      - URL parameters to sort results ( sort=-id )
   * @param filter    - URL parameters to filter results ( name=MyVariatn )
   * @return Object Containing the list of decisions with their respective details for the given project
   */
  public List<Decision> getDecisions(int clientId, int projectId, int decisionGroupId, String sort, String filter) {
    Map<String, Object> queryParameters = new HashMap<>();
    if (StringUtils.hasText(sort)) {
      queryParameters.put("sort", sort);
    }
    return getRestConnector().callService(HttpMethod.GET, getDecisionBasePath(clientId, projectId, decisionGroupId) + DECISIONS, new TypeReference<List<Decision>>() {
    }, queryParameters, null);
  }

  public List<Decision> getDecisions(int clientId, int projectId, String sort, String filter) {
    return getDecisions(clientId, projectId, -1, sort, filter);
  }

  /**
   * Returns all data given a decision id
   * <p/>
   * The decision object contains the same data returned for the list of decisions in the previous method
   * only that this is for a single decision.
   *
   * @param clientId   - the user account id to retrieve the data for
   * @param projectId  the id of the project to retrieve the decision info from
   * @param decisionId the id of the decision
   * @return Object Containing all details for the specified decision for the given project
   */
  public Decision getDecision(int clientId, int projectId, int decisionGroupId, int decisionId) {
    return getRestConnector().callService(HttpMethod.GET, getDecisionBasePath(clientId, projectId, decisionGroupId) + DECISION + decisionId, new TypeReference<Decision>() {
    }, null);
  }

  public int getDecision(int clientId, int projectId, Decision decision) {
    return createDecision(clientId, projectId, -1, decision);
  }

  /**
   * Creates a new decision which will be associated with the given project
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId - the project ID which decisions belongs to
   * @param decision  - contains each decision field to be created in the DB
   * @return Object Containing the new created decision id
   */
  public int createDecision(int clientId, int projectId, int decisionGroupId, Decision decision) {
    return getRestConnector().callService(HttpMethod.POST, getDecisionBasePath(clientId, projectId, decisionGroupId) + "/decision", new TypeReference<Integer>() {
    }, decision.toMap());
  }

  public int createDecision(int clientId, int projectId, Decision decision) {
    return createDecision(clientId, projectId, -1, decision);
  }

  /**
   * Updates a decision with the data passed as parameter
   *
   * @param clientId   - the user account id to retrieve the data for
   * @param projectId  The ID of the project to update the decision for
   * @param decisionId The ID of the decision to be updated
   * @param decision   - contains all required fields to be updated in the DB
   */
  public void updateDecision(int clientId, int projectId, int decisionId, int decisionGroupId, Decision decision) {
    getRestConnector().callService(HttpMethod.PUT, ACCOUNT + getDecisionBasePath(clientId, projectId, decisionGroupId) + DECISION + decisionId, null, decision.toMap());
  }

  public void updateDecision(int clientId, int projectId, int decisionGroupId, Decision decision) {
    updateDecision(clientId, projectId, -1, decisionGroupId, decision);
  }

  /**
   * Deletes a decision given a project and a decision ID
   *
   * @param clientId   - the user account id to retrieve the data for
   * @param projectId  The ID of the project to delete the decision for
   * @param decisionId The ID of the decision to be deleted
   */
  public void deleteDecision(int clientId, int projectId, int decisionGroupId, int decisionId) {
    getRestConnector().callService(HttpMethod.DELETE, getDecisionBasePath(clientId, projectId, decisionGroupId) + DECISION + decisionId, null, null);
  }

  public void deleteDecision(int clientId, int projectId, int decisionId) {
    deleteDecision(clientId, projectId, -1, decisionId);
  }

  /**
   * Returns an object with a list of goals assigned to a particular project
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The ID of the project to retrieve the goals for
   * @return Object Containing the list of goals with their respective details for the given project
   */
  public List<Goal> getGoals(int clientId, int projectId) {
    Map<String, Object> queryParameters = new HashMap<>();
    return getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId + PROJECT + projectId + "/goals", new TypeReference<List<Goal>>() {
    }, queryParameters, null);
  }

  /**
   * gets a particular goal information
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The ID of the project to retrieve the goal data for
   * @param goalId    The ID od the goal itself
   * @return Object Containing all details for the specified goal for the given project
   */
  public Goal getGoal(int clientId, int projectId, int goalId) {
    return getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId + PROJECT + projectId + GOAL + goalId, new TypeReference<Goal>() {
    }, null);
  }

  /**
   * Creates a relation betwen a goal and a project
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The ID of the project to assign the goal for
   * @param goal      The ID of the goal itself
   * @return Object Containing  the new created goal id
   */
  public int createGoal(int clientId, int projectId, Goal goal) {
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + PROJECT + projectId + "/goal", new TypeReference<Integer>() {
    }, goal.getGoalForRemoteCreation());
  }

  /**
   * Updates  a goal entry with the new data passed as parameter
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The ID of the project to update the goal for
   * @param goalId    The ID of the goal to be related to the given project
   * @param goal      Contains all required data for the goal
   */
  public void updateGoal(int clientId, int projectId, int goalId, Goal goal) {
    getRestConnector().callService(HttpMethod.PUT, ACCOUNT + clientId + PROJECT + projectId + GOAL + goalId, null, goal.getGoalForRemoteUpdate());
  }

  /**
   * Un-relates a goal from a project given their ID's
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The ID of the project to delete the goal for
   * @param goalId    The ID of the goal to be deleted
   */
  public void deleteGoal(int clientId, int projectId, int goalId) {
    getRestConnector().callService(HttpMethod.DELETE, ACCOUNT + clientId + PROJECT + projectId + GOAL + goalId, null, null);
  }

  /**
   * Gets an object containing a list of rules for the logged client
   * <p/>
   * If the client is a tenant, he can get the rules that has been created by himself or by all of his
   * clients.
   *
   * @param clientId - the user account id to retrieve the data for
   * @return Object Containing a list of rules with their respective details
   */
  public List<Rule> getRules(int clientId) {
    return getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId + "/rules", new TypeReference<List<Rule>>() {
    }, null);
  }

  /**
   * Gets all data for a particular rule
   *
   * @param clientId - the user account id to retrieve the data for
   * @param ruleId   The ID of the rule to retrieve the data for
   * @return Object Containing all details for a specific rule
   */
  public Rule getRule(int clientId, int ruleId) {
    return getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId + RULE + ruleId, new TypeReference<Rule>() {
    }, null);
  }

  /**
   * Creates a new rule with the respective data sent as parameter
   *
   * @param clientId - the user account id to retrieve the data for
   * @param rule     Contains the rule required data (name, operation)
   * @return Object Containing  the new created rule id
   */
  public int createRule(int clientId, Rule rule) {
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + "/rule", new TypeReference<Integer>() {
    }, rule.getRuleForRemoteCreation());
  }

  /**
   * Given a rule ID updates it data.
   * <p/>
   * The $rule array contains the name of the rule and the operation (AND/OR)
   *
   * @param clientId - the user account id to retrieve the data for
   * @param ruleId   The ID of the rule to be updated
   * @param rule     Contains the rule required data
   */
  public void updateRule(int clientId, int ruleId, Rule rule) {
    getRestConnector().callService(HttpMethod.PUT, ACCOUNT + clientId + RULE + ruleId, null, rule.getRuleForRemoteUpdate());
  }

  /**
   * Given a rule ID, sends a request to be deleted from the DB
   *
   * @param clientId - the user account id to retrieve the data for
   * @param ruleId   The ID of the rule to be deleted
   */
  public void deleteRule(int clientId, int ruleId) {
    getRestConnector().callService(HttpMethod.DELETE, ACCOUNT + clientId + RULE + ruleId, null, null);
  }

  /**
   * Returns an object with a list of conditions and the respective data.
   * <p/>
   * Each element of the response object contains the value of the "negation" field (boolean),
   * the type (String) and the arguments (String).
   *
   * @param clientId - the user account id to retrieve the data for
   * @param ruleId
   * @return Object Containing a list of conditions with their respective details for a given rule
   */
  public List<Condition> getConditions(int clientId, int ruleId) {
    return getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId + RULE + ruleId + "/conditions", new TypeReference<List<Condition>>() {
    }, null);
  }

  /**
   * Returns all data for the given condition.
   * <p/>
   * Returned object contains the value of the "negation" field (boolean), the type (String) and the
   * arguments (String).
   *
   * @param clientId    - the user account id to retrieve the data for
   * @param ruleId      The ID of the rule that the condition belongs to
   * @param conditionId The ID of the condition itself
   * @return Object Containing all details given a condition for the specified rule
   */
  public Condition getCondition(int clientId, int ruleId, int conditionId) {
    return getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId + RULE + ruleId + CONDITION + conditionId, new TypeReference<Condition>() {
    }, null);
  }

  /**
   * creates a new condition for the given rule
   * <p/>
   * The condition array (second parameter) has to contain the valu of the negation attribute (Boolean),
   * the type of the condition(String) and optionally the arguments(String)
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param ruleId    The ID of the rule to create the condition for
   * @param condition An array containing all required fields to create a new condition
   * @return Object Containing  the new created condition id
   */
  public int createCondition(int clientId, int ruleId, Condition condition) {
    return getRestConnector().callService(HttpMethod.POST, ACCOUNT + clientId + RULE + ruleId + "/condition", new TypeReference<Integer>() {
    }, condition.getConditionForRemoteCreation());
  }

  /**
   * Updates all or some of the data for a particular decision
   *
   * @param clientId    - the user account id to retrieve the data for
   * @param ruleId      The ID of the rule that the condition belogs to
   * @param conditionId The ID of the condition to be updated
   * @param condition   Contains the required data to update the condition in the DB
   */
  public void updateCondition(int clientId, int ruleId, int conditionId, Condition condition) {
    getRestConnector().callService(HttpMethod.PUT, ACCOUNT + clientId + RULE + ruleId + CONDITION + conditionId, null, condition.getConditionForRemoteUpdate());
  }

  /**
   * Given a rule ID and a condition ID, deletes the particular condition which is part of the given rule
   *
   * @param clientId    - the user account id to retrieve the data for
   * @param ruleId      The ID of the rule that the condition belongs to
   * @param conditionId The ID of the condition to be deleted
   */
  public void deleteCondition(int clientId, int ruleId, int conditionId) {
    getRestConnector().callService(HttpMethod.DELETE, ACCOUNT + clientId + RULE + ruleId + CONDITION + conditionId, null, null);
  }

  /**
   * Returns an object containing statistical data for the given project
   * <p/>
   * In the server, the project and its decisions are evaluated to determine the amount of impressions,
   * conversions and aggregated conversion rate to create an object with statistical data for a period
   * of time.
   * Users can filter results to retrieve a custom number of entries, to define an end date or a particular
   * goal ID, e.g: getTrend(456, entries=50,enddate=2015-01-31,goalid=74);
   * This will return statistics for the project with ID = 456 which goal id =  74
   * and a total of 50 entries from 2014-12-21 to 2015-01-31 .
   *
   * @param clientId  - the user account id to retrieve the data for
   * @param projectId The Id of the project to return the statistics for
   * @param end       Specifies the latest required timestamp in the result.
   * @param entries   Number of datapoints / timestamps in the result (currently translated to the number of days in the result).
   *                  <p/>
   *                  The default is 30.
   * @param goalId    A goal for which conversions shall be calculated.
   * @return Object Containing a set of date points and details for a period of time given a project id
   */
  public Trend getTrend(int clientId, int projectId, Calendar end, int entries, int goalId) {
    Map<String, Object> queryParameters = new HashMap<>();
    if (end != null) {
      queryParameters.put("end", ABTestingRestConnector.DATE_FORMAT.format(end.getTime()));
    }
    if (entries > 0) {
      queryParameters.put("entries", entries);
    }
    if (goalId > 0) {
      queryParameters.put("goalid", goalId);
    }

    Map<Calendar, Map<String, DataSet>> result = getRestConnector().callService(HttpMethod.GET, ACCOUNT + clientId + PROJECT + projectId + "/trend/", new TypeReference<Map<Calendar, Map<String, DataSet>>>() {
    }, queryParameters, null);
    return new Trend(result);
  }
}
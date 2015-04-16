<?php

/**
 * URL of the main API class
 * 
 * provides the right URL to perform requests to the API itself.
 */
define('API_URL', 'https://www.blacktri.com/api/v1/', TRUE);

/**
 * Provides access to all available resources that can be found in the API
 * 
 * Al methods included in this class are accessible by any type of user (tenant and clients) and works
 * as an abstraction layer for the API itself. It contains multiple methods that enables users to
 * Create, Read, Update and Delete resources associated to his/her account like projects,
 * decisions, personalization rules, goals or his/her account details.
 */
class apiv1sdk {

    /**
     * For tenants only: retrieves all client's account information
     * 
     * If the logged user is a tenant, he can call this method to get a list of  his client's accounts with their 
     * respective information including the client id, first name, last name, email, status, user plan, etc.
     * 
     * The list can be searched/filtered with the following querystring qualifiers
     * subid, publicid, custom1, custom2, custom3, apikey, apisecret, email, emailvalidated, status.
     * 
     * The result list can be sorted by providing the qualifier "sort" with one of the following attributes
     * publicid, custom1, custom2, custom3, apikey, email, createddate
     * A dash can be added to the attribute to indicate descending order.
     * 
     * e.g. getAccounts(status=ACTIVE&sort-publidid)
     * 
     * @param String $filter "&" separated key=value pairs to filter and sort results
     * @return Object Containing the list of accouts and details for each of them
     */
    public function getAccounts($filter = FALSE) {
        $f = '';
        if ($filter) {
            $filter = str_replace(' ', '%20', $filter);
            $f = '/?' . str_replace('?', '', $filter);
        }
        $path = "accounts" . $f;
        return self::performRequest($path);
    }

    /**
     * Retrieves a particular account information
     * 
     * If this method is called by a tenant, the provided parameter "accountid" has to match one of his clients,
     * if it is called by a client, if thas to match his own ID, otherwise, the API will throw an exception
     * 
     * @param Int $accountid - the user account id to retrieve the data for
     * @return Object Containing all the details for the specified account
     */
    public function getAccount($accountid) {
        $path = "account/$accountid";
        return self::performRequest($path);
    }

    /**
     * Allows a tenant to create a new account
     * 
     * If a tenant needs to create a new account, he can send an array with all required data as parameter
     * to this method, including i.e. his first name, last name, status, quota, etc.
     * 
     * @param Array $account - an array with the required data to create a new account
     * @return Object Containing the new created account id
     */
    public function createAccount($account) {
        $path = "account";
        return self::performRequest($path, 'POST', $account);
    }

    /**
     * Updates an account information
     * 
     * Tenants can update one of his client's account information by sending  the account Id and the 
     * array with the new data as parameters to this method
     * 
     * @param Int $accountid the account ID
     * @param Array $account Array with all the required account data
     * @return Object Containing the response from the server after trying to edit the account details
     */
    public function updateAccount($accountid, $account) {
        $path = "account/$accountid";
        return self::performRequest($path, 'PUT', $account);
    }

    /**
     * Returns an object with a list of projects and its corresponding data
     * 
     * If the user is a tenant, this method get the information of all of the projects that he or his clients
     * have created.
     * If the user is a client, returns the projects that has been created himself.
     * 
     * Results can be searched/filtered with the following querystring qualifiers that refer to values of the 
     * resource attributes "type" and "status" (e.g, type=SPLIT&status=RUNNING).
     * 
     * The result list can be sorted by providing the qualifier sort with one of the project attributes. 
     * A dash can be added to the attribute to indicate descending order (e.g. sort=-createddate).
     * 
     * The fields contained in the result set can be determined or limited by providing a parameter "fields" 
     * with a comma separated list of field names. (e.g. fields=id,name,mainurl,visitors,conversions)
     * 
     * @param String $filter - "&" separated key=value pairs to filter and sort and limit results
     * @return Object Containing a list of project with their respective details
     */
    public function getProjects($filter = FALSE) {
        $f = '';
        if ($filter) {
            $filter = str_replace(' ', '%20', $filter);
            $f = '/?' . str_replace('?', '', $filter);
        }
        $path = "account/$this->clientid/projects" . $f;
        return self::performRequest($path);
    }









    /**
     * Returns a list of decisions for the given project
     * 
     * The result list can be sorted by providing the qualifier "sort" with one of the following attributes:
     * "name", "conversions", A dash can be added to the attribute to indicate descending order
     * (e.g. sort=-name).
     * The list can be searched/filtered with the following querystring qualifiers that refer to values of the 
     * resource attributes: "result". (e.g. sort=-name&result=WON)
     * 
     * @param Int $projectid The id of the project to retrieve all decisions from
     * @param String $filter - URL parameters to filter or sort results ( name=MyVariatn&sort=-id )
     * @return Object Containing the list of decisions with their respective details for the given project
     */
    public function getDecisions($projectid, $filter = FALSE) {
        $f = '';
        if ($filter) {
            $filter = str_replace(' ', '%20', $filter);
            $f = '/?' . str_replace('?', '', $filter);
        }
        $path = "account/$this->clientid/project/$projectid/decisions" . $f;
        return self::performRequest($path);
    }

    /**
     * Returns all data given a decision id
     * 
     * The decision object contains the same data returned for the list of decisions in the previous method
     * only that this is for a single decision.
     * 
     * @param Int $projectid the id of the project to retrieve the decision info from
     * @param Int $decisionid the id of the decision
     * @return Object Containing all details for the specified decision for the given project
     */
    public function getDecision($projectid, $decisionid) {
        $path = "account/$this->clientid/project/$projectid/decision/$decisionid";
        return self::performRequest($path);
    }

    /**
     * Creates a new decision which will be associated with the given project
     * 
     * @param Int $projectid - the project ID which decisions belongs to
     * @param Array $decision - contains each decision field to be created in the DB
     * @return Object Containing the new created decision id
     */
    public function createDecision($projectid, $decision) {
        $path = "account/$this->clientid/project/$projectid/decision";
        return self::performRequest($path, 'POST', $decision);
    }

    /**
     * Updates a decision with the data passed as parameter
     * 
     * @param Int $projectid The ID of the project to update the decision for
     * @param Int $decisionid The ID of the decision to be updated
     * @param Array $decision - contains all required fields to be updated in the DB
     * @return Object Containing the response from the server after trying to edit the decision details
     */
    public function updateDecision($projectid, $decisionid, $decision) {
        $path = "account/$this->clientid/project/$projectid/decision/$decisionid";
        return self::performRequest($path, 'PUT', $decision);
    }

    /**
     * Deletes a decision given a project and a decision ID
     * 
     * @param Int $projectid The ID of the project to delete the decision for
     * @param Int $decisionid The ID of the decision to be deleted
     * @return Object Containing the response from the server after trying to delete the given decision
     */
    public function deleteDecision($projectid, $decisionid) {
        $path = "account/$this->clientid/project/$projectid/decision/$decisionid";
        return self::performRequest($path, 'DELETE');
    }

    /**
     * Returns an object with a list of goals assigned to a particular project
     * 
     * @param Int $projectid The ID of the project to retrieve the goals for
     * @return Object Containing the list of goals with their respective details for the given project
     */
    public function getGoals($projectid, $filter = FALSE) {
        $f = '';
        if ($filter) {
            $filter = str_replace(' ', '%20', $filter);
            $f = '/?' . str_replace('?', '', $filter);
        }
        $path = "account/$this->clientid/project/$projectid/goals" . $f;
        return self::performRequest($path);
    }

    /**
     * gets a particular goal information
     * 
     * @param Int $projectid The ID of the project to retrieve the goal data for
     * @param Int $goalid The ID od the goal itself
     * @return Object Containing all details for the specified goal for the given project
     */
    public function getGoal($projectid, $goalid) {
        $path = "account/$this->clientid/project/$projectid/goal/$goalid";
        return self::performRequest($path);
    }

    /**
     * Creates a relation betwen a goal and a project
     * 
     * @param Int $projectid The ID of the project to assign the goal for
     * @param Array $goal The ID of the goal itself
     * @return Object Containing  the new created goal id
     */
    public function createGoal($projectid, $goal) {
        $path = "account/$this->clientid/project/$projectid/goal";
        return self::performRequest($path, 'POST', $goal);
    }

    /**
     * Updates  a goal entry with the new data passed as parameter
     * 
     * @param Int $projectid The ID of the project to update the goal for
     * @param Int $goalid The ID of the goal to be related to the given project
     * @param Array $goal Contains all required data for the goal
     * @return Object Containing the response from the server after trying to update the goal details for the given project
     */
    public function updateGoal($projectid, $goalid, $goal) {
        $path = "account/$this->clientid/project/$projectid/goal/$goalid";
        return self::performRequest($path, 'PUT', $goal);
    }

    /**
     * Un-relates a goal from a project given their ID's
     * 
     * @param Int $projectid The ID of the project to delete the goal for
     * @param Int $goalid The ID of the goal to be deleted
     * @return Object Containing the response from the server after trying to delete a goal from a given project
     */
    public function deleteGoal($projectid, $goalid) {
        $path = "account/$this->clientid/project/$projectid/goal/$goalid";
        return self::performRequest($path, 'DELETE');
    }

    /**
     * Gets an object containing a list of rules for the logged client
     * 
     * If the client is a tenant, he can get the rules that has been created by himself or by all of his
     * clients.
     * 
     * @return Object Containing a list of rules with their respective details
     */
    public function getRules() {
        $path = "account/$this->clientid/rules";
        return self::performRequest($path);
    }

    /**
     * Gets all data for a particular rule
     * 
     * @param Int $ruleid The ID of the rule to retrieve the data for
     * @return Object Containing all details for a specific rule
     */
    public function getRule($ruleid) {
        $path = "account/$this->clientid/rule/$ruleid";
        return self::performRequest($path);
    }

    /**
     * Creates a new rule with the respective data sent as parameter
     * 
     * @param Array $rule Contains the rule required data (name, operation)
     * @return Object Containing  the new created rule id
     */
    public function createRule($rule) {
        $path = "account/$this->clientid/rule";
        return self::performRequest($path, 'POST', $rule);
    }

    /**
     * Given a rule ID updates it data.
     * 
     * The $rule array contains the name of the rule and the operation (AND/OR)
     * 
     * @param Int $ruleid The ID of the rule to be updated
     * @param Array $rule Contains the rule required data
     * @return Object Containing the response from the server after trying to update the details for the given rule
     */
    public function updateRule($ruleid, $rule) {
        $path = "account/$this->clientid/rule/$ruleid";
        return self::performRequest($path, 'PUT', $rule);
    }

    /**
     * Given a rule ID, sends a request to be deleted from the DB
     * 
     * @param Int $ruleid The ID of the rule to be deleted
     * @return Object Containing  the response from the server after trying to delete the specified rule
     */
    public function deleteRule($ruleid) {
        $path = "account/$this->clientid/rule/$ruleid";
        return self::performRequest($path, 'DELETE');
    }

    /**
     * Returns an object with a list of conditions and the respective data.
     * 
     * Each element of the response object contains the value of the "negation" field (boolean), 
     * the type (String) and the arguments (String).
     * 
     * @param Int $ruleid
     * @return Object Containing a list of conditions with their respective details for a given rule
     */
    public function getConditions($ruleid) {
        $path = "account/$this->clientid/rule/$ruleid/conditions";
        return self::performRequest($path);
    }

    /**
     * Returns all data for the given condition.
     * 
     * Returned object contains the value of the "negation" field (boolean), the type (String) and the 
     * arguments (String).
     * 
     * @param Int $ruleid The ID of the rule that the condition belongs to
     * @param Int $conditionid The ID of the condition itself
     * @return Object Containing all details given a condition for the specified rule
     */
    public function getCondition($ruleid, $conditionid) {
        $path = "account/$this->clientid/rule/$ruleid/condition/$conditionid";
        return self::performRequest($path);
    }

    /**
     * creates a new condition for the given rule
     * 
     * The condition array (second parameter) has to contain the valu of the negation attribute (Boolean), 
     * the type of the condition(String) and optionally the arguments(String)
     * 
     * @param Int $ruleid The ID of the rule to create the condition for
     * @param Array $condition An array containing all required fields to create a new condition
     * @return Object Containing  the new created condition id
     */
    public function createCondition($ruleid, $condition) {
        $path = "account/$this->clientid/rule/$ruleid/condition";
        return self::performRequest($path, 'POST', $condition);
    }

    /**
     * Updates all or some of the data for a particular decision
     * 
     * @param Int $ruleid The ID of the rule that the condition belogs to
     * @param Int $conditionid The ID of the condition to be updated
     * @param Array $condition Contains the required data to update the condition in the DB
     * @return Object Containing the response from the server after trying to update the details for a condition
     */
    public function updateCondition($ruleid, $conditionid, $condition) {
        $path = "account/$this->clientid/rule/$ruleid/condition/$conditionid";
        return self::performRequest($path, 'PUT', $condition);
    }

    /**
     * Given a rule ID and a condition ID, deletes the particular condition which is part of the given rule
     * 
     * @param Int $ruleid The ID of the rule that the condition belongs to
     * @param Int $conditionid The ID of the condition to be deleted
     * @return Object Containing the response from the server after trying to delete a condition for a given rule
     */
    public function deleteCondition($ruleid, $conditionid) {
        $path = "account/$this->clientid/rule/$ruleid/condition/$conditionid";
        return self::performRequest($path, 'DELETE');
    }

    /**
     * Returns an object containing statistical data for the given project
     * 
     * In the server, the project and its decisions are evaluated to determine the amount of impressions,
     * conversions and aggregated conversion rate to create an object with statistical data for a period
     * of time.
     * Users can filter results to retrieve a custom number of entries, to define an end date or a particular
     * goal ID, e.g: getTrend(456, entries=50,enddate=2015-01-31,goalid=74);
     * This will return statistics for the project with ID = 456 which goal id =  74
     *  and a total of 50 entries from 2014-12-21 to 2015-01-31 .
     * 
     * @param Int $projectid The Id of the project to return the statistics for
     * @param String $filter users can use custom filters to restrict the returned results
     * @return Object Containing a set of date points and details for a period of time given a project id
     */
    public function getTrend($projectid, $filter = FALSE) {
        $f = '';
        if ($filter) {
            $filter = str_replace(' ', '%20', $filter);
            $f = '/?' . str_replace('?', '', $filter);
        }
        $path = "account/$this->clientid/project/$projectid/trend" . $f;
        return self::performRequest($path);
    }

}

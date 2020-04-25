#!groovy

import groovy.json.JsonSlurperClassic

node {

    def SF_CONSUMER_KEY          ="3MVG97quAmFZJfVy5VgSD0VfwcdBR1ufeX4ULL919TZu4fshfI_UO0yCjCLG2v4azJSvhEnOOhOz8stblhaZC";
    def SF_USERNAME              ="partialcopy@gmail.com";
    def SERVER_KEY_CREDENTALS_ID ="f45a0e76-ee17-4f39-addb-fce83d2b09ed";
    def TEST_LEVEL               ="RunLocalTests";
    def SF_INSTANCE_URL          ="https://login.salesforce.com";

    def toolbelt = tool 'toolbelt'


    // -------------------------------------------------------------------------
    // Check out code from source control.
    // -------------------------------------------------------------------------
	
	println 'printing information of env variable'
	
	print 'SF_CONSUMER_KEY :'
	
	println SF_CONSUMER_KEY
	
	print 'SF_USERNAME :'
	
	println SF_USERNAME
	
	print 'SERVER_KEY_CREDENTALS_ID :'
	
	println SERVER_KEY_CREDENTALS_ID
	
	print 'TEST_LEVEL :'
	
	println TEST_LEVEL
	
	print 'PACKAGE_NAME:'
	
	println PACKAGE_NAME
	
	print 'SF_INSTANCE_URL :'
	
	println SF_INSTANCE_URL



    stage('checkout source') {
        checkout scm
    }
	
	println 'after checkout source:'


    // -------------------------------------------------------------------------
    // Run all the enclosed stages with access to the Salesforce
    // JWT key credentials.
    // -------------------------------------------------------------------------

	
	withEnv(["HOME=${env.WORKSPACE}"]) {
		
		withCredentials([file(credentialsId: SERVER_KEY_CREDENTALS_ID, variable: 'jwt_key_file')]) {
            
            /*Step-1: Authorize DevHub Org*/

			stage('Authorize DevHub') {

                rc = bat returnStatus: true, script: "\"${toolbelt}\" force:org:list"

                rc = bat returnStatus: true, script: "\"${toolbelt}\" force:auth:logout -u ${SF_USERNAME} -p"

                rc = bat returnStatus: true, script: "\"${toolbelt}\" force:auth:jwt:grant --instanceurl ${SF_INSTANCE_URL} --clientid ${SF_CONSUMER_KEY} --username ${SF_USERNAME} --jwtkeyfile \"${jwt_key_file}\" --setdefaultdevhubusername --setalias HubOrg";
                if (rc != 0) {
                    error 'Salesforce dev hub org authorization failed.'
                }
                rc = bat returnStatus: true, script: "\"${toolbelt}\" force:org:list"
            }


		}
	}
	
}

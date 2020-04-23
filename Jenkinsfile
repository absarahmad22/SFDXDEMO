#!groovy

import groovy.json.JsonSlurperClassic

node {

    def SF_CONSUMER_KEY=env.SF_CONSUMER_KEY
    def SF_USERNAME=env.SF_USERNAME
    def SERVER_KEY_CREDENTALS_ID=env.SERVER_KEY_CREDENTALS_ID
    def TEST_LEVEL='RunLocalTests'
    def PACKAGE_NAME='0Ho1U000000CaUzSAK'
    def PACKAGE_VERSION
    def SF_INSTANCE_URL = env.SF_INSTANCE_URL ?: "https://login.salesforce.com"

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

	
//	withEnv(["HOME=${env.WORKSPACE}"]) {
		
		withCredentials([file(credentialsId: SERVER_KEY_CREDENTALS_ID, variable: 'jwt_key_file')]) {
		
			 stage('Authorize DevHub') {
                rc = bat returnStatus: true, script: "\"${toolbelt}\" force:auth:jwt:grant --instanceurl ${SF_INSTANCE_URL} --clientid ${SF_CONSUMER_KEY} --username ${SF_USERNAME} --jwtkeyfile ${jwt_key_file} --setdefaultdevhubusername --setalias HubOrg"
                if (rc != 0) {
                    error 'Salesforce dev hub org authorization failed.'
                }
            }
		
		
		}
	
//	}
	
}

def command(script) {
    if (isUnix()) {
        return sh(returnStatus: true, script: script);
    } else {
        return bat(returnStatus: true, script: script);
    }
}

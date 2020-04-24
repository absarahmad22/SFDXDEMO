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


            /*Step 2 Create Scratch Org*/

            stage('Create Test Scratch Org') {
                rc = bat returnStatus: true, script: "\"${toolbelt}\" force:org:create --targetdevhubusername HubOrg --setdefaultusername --definitionfile config/project-scratch-def.json --setalias ciorg --wait 10 --durationdays 1"
                if (rc != 0) {
                    error 'Salesforce test scratch org creation failed.'
                }
            }


            /*Stage 3 Push Source Code to Scratch Org*/
            stage('Push To Test Scratch Org') {
                rc = bat returnStatus: true, script: "\"${toolbelt}\" force:source:push --targetusername ciorg"
                if (rc != 0) {
                    error 'Salesforce push to test scratch org failed.'
                }
            }



            /*Stage 4 Run Unit Testing Scratch Org*/

            stage('Run Tests In Test Scratch Org') {
                rc = bat returnStatus: true, script: "\"${toolbelt}\" force:apex:test:run --targetusername ciorg --wait 10 -r human --resultformat tap --codecoverage --testlevel ${TEST_LEVEL}"
                if (rc != 0) {
                    error 'Salesforce unit test run in test scratch org failed.'
                }
            }

            /*Stage 5 Delete Scratch Org*/

             stage('Delete Test Scratch Org') {
                rc = bat returnStatus: true, script: "\"${toolbelt}\" force:org:delete --targetusername ciorg --noprompt"
                if (rc != 0) {
                    error 'Salesforce test scratch org deletion failed.'
                }
            }


		
		
		}
	}
	
}

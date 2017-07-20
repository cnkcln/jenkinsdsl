job('iRecruit Service Build and Test') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/irecruit-service.git'
		credentials 'bbid'
					
			}
			extensions {
				wipeOutWorkspace()
			}
			branch '*/phase1'
		
		}
	}

	steps {
		gradle {
			tasks('clean')
			tasks('test')
			switches('-i')
			useWrapper()
		}
	}

	triggers {
		
		scm('* * * * *') {
			ignorePostCommitHooks()
		}
		bitbucketPush()
	}


	wrappers {
		colorizeOutput()
	}
	
	 publishers {
            archiveJunit('**/*.xml') {
            allowEmptyResults()
            retainLongStdout()
            healthScaleFactor(1.5)
            testDataPublishers {
				allowClaimingOfFailedTests()
				publishFlakyTestsReport()
               			publishTestStabilityData()
            }
        }
    }
}

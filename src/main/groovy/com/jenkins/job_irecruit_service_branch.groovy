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
			branch '*/RS*'
		
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
			downstreamParameterized {
				trigger('iRecruit Service Branch Sonar') {
					condition('SUCCESS')
					parameters {
						gitRevision()
					}
				}
			}
        }
    }
}

job('iRecruit Service Branch Sonar') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/irecruit-service.git'
				credentials 'bbid'
			}
			extensions {
				wipeOutWorkspace()
			}
			branch '*/RS*'
		}
	}

	steps {
		gradle {
			tasks('clean')
			tasks('sonarqube')
			switches('-i -Pversion=${GIT_COMMIT}')
			useWrapper()
		}
	}

	wrappers {
		colorizeOutput()
	}
}
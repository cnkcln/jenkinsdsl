job('iRecruit Service Build and Test -- Master') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/irecruit-service.git'
				credentials 'bbid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/phase1'
		}
	}

	steps {
		gradle {
			tasks('clean')
			tasks('build')
			switches('-i')
			useWrapper()
		}
	}

	triggers {

		scm('* * * * *') { ignorePostCommitHooks() }
		bitbucketPush()
	}


	wrappers { colorizeOutput() }

	publishers {
		downstreamParameterized {
			trigger('iRecruit Service Branch Sonar -- Master') {
				condition('SUCCESS')
				parameters { gitRevision() }
			}
		}
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

job('iRecruit Service Branch Sonar -- Master') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/irecruit-service.git'
				credentials 'bbid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/phase1'
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
	
	publishers {
		downstreamParameterized {
			trigger('iRecruit Service Publish') {
				condition('SUCCESS')
				parameters {
					gitRevision()
				}
			}
		}
	}
	
	wrappers { colorizeOutput() }
}

job('iRecruit Service Publish') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/irecruit-service.git'
				credentials 'bbid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/phase1'
		}
	}

	steps {
		gradle {
			tasks('clean')
			tasks('artifactory')
			switches('-i -Pversion=${GIT_COMMIT}')
			useWrapper()
		}
	}
	wrappers { colorizeOutput() }
}

listView('RS Master Jobs') {
	columns {
		status()
		weather()
		name()
		lastSuccess()
		lastFailure()
		lastDuration()
		buildButton()
	}
	jobs {
		name('iRecruit Service Build and Test -- Master')
		name('iRecruit Service Branch Sonar -- Master')
		name('iRecruit Service Publish')
//		name('iRecruit Service Deploy')
//		name('iRecruit Service Performance Deploy')
//		name('iRecruit Service Isolation Test')
//		name('iRecruit Service Performance Test')
//		name('iRecruit Service Promote Artifact')
		
	}
}
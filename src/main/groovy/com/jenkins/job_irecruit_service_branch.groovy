<<<<<<< HEAD
job('iRecruit Service Unit And Integration Test - Branch') {
=======
job('iRecruit Service Branch Build and Test') {
>>>>>>> 0cce170c7610d66da0c8e8607e9a28fed8689a3e
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/irecruit-service.git'
				credentials 'bbid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/RS*'
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
			trigger('iRecruit Service Sonar -- Branch ') {
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

job('iRecruit Service Sonar -- Branch ') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/irecruit-service.git'
				credentials 'bbid'
			}
			extensions { wipeOutWorkspace() }
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

	wrappers { colorizeOutput() }
}

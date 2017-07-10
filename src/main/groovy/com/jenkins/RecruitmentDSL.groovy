job('Recruitment Portal Build and Test') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/osi-recruitment-portal.git'
				credentials 'bbid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/MD_MASTER_DEV'
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

		scm('* * * * *') { ignorePostCommitHooks() }
		bitbucketPush()
	}


	wrappers { colorizeOutput() }

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

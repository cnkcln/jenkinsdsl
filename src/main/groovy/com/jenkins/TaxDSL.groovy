job('Tax Portal Build and Test') {
	scm {
		git {
			remote {
				url 'https://itsubproject@bitbucket.org/itsubproject/taxsubmission.git'
				credentials 'itid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/phase3'
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

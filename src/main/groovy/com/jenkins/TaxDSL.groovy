job('Tax-Portal-Build-and-Test(Phase3.1)') {
	scm {
		git {
			remote {
				url 'https://itsubproject@bitbucket.org/itsubproject/taxsubmission.git'
				credentials 'itid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/phase3.1'
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



	publishers {
		downstreamParameterized {
			trigger('Tax-Portal-Service-Publish(Phase3.1)') {
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
		wrappers { colorizeOutput() }
	}
}


job('Tax-Portal-Service-Publish(Phase3.1)') {
	scm {
		git {
			remote {
				url 'https://itsubproject@bitbucket.org/itsubproject/taxsubmission.git'
				credentials 'itid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/phase3.1'
		}
	}
	
	steps {
		gradle {
			tasks('clean')
			tasks('build')
			tasks('uploadArchives')
			switches('-i -Pversion=${GIT_COMMIT}')
			useWrapper()
		}
	}
	
	publishers {
		downstreamParameterized {
			trigger('Tax-Portal-Service-Deploy(Phase3.1)') {
				condition('SUCCESS')
				parameters { gitRevision() }
			}
		}
	}
	wrappers { colorizeOutput() }
	
}

job('Tax-Portal-Service-Deploy(Phase3.1)') {
	scm {
		git {
			remote {
				url 'https://itsubproject@bitbucket.org/itsubproject/taxsubmission.git'
				credentials 'itid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/phase3.1'
		}
	}

	steps {
		gradle {
			tasks('clean')
			tasks('downloadFile')
			tasks('startServer')
			switches('-i -Pversion=${GIT_COMMIT}')
			useWrapper()
		}
	}
	wrappers { colorizeOutput() }
	
}

listView('Tax Master Jobs') {
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
		name('Tax-Portal-Build-and-Test(Phase3.1)')
		name('Tax-Portal-Service-Publish(Phase3.1)')
		name('Tax-Portal-Service-Deploy(Phase3.1)')
		
	}
}

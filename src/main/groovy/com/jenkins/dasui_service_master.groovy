job('DAS-Ui-Build-(Master)') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/dasui.git'
				credentials 'bbid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/master'
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
			trigger('DAS-Ui-Sonar-(Master)') {
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

job('DAS-Ui-Sonar-(Master)') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/dasui.git'
				credentials 'bbid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/master'
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
			trigger('DAS-Ui-Publish-(Master)') {
				condition('SUCCESS')
				parameters {
					gitRevision()
				}
			}
		}
	}
	
	wrappers { colorizeOutput() }
}


job('DAS-Ui-Publish-(Master)') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/dasui.git'
				credentials 'bbid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/master'
		}
	}

	steps {
		gradle {
			tasks('clean')
			tasks('uploadArchives')
			switches('-i -Pversion=${GIT_COMMIT}')
			useWrapper()
		}
	}
	
	publishers {
		downstreamParameterized {
			trigger('DAS-Ui-Deploy-(Master)') {
				condition('SUCCESS')
				parameters {
					gitRevision()
				}
			}
		}
	}
	wrappers { colorizeOutput() }
}

job('DAS-Ui-Deploy-(Master)') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/dasui.git'
				credentials 'bbid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/master'
		}
	}

	steps {
		gradle {
			tasks('clean')
			tasks('downloadFile')
			switches('-i -Pversion=${GIT_COMMIT}')
			useWrapper()
		}
		shell( "fuser -k 7080/tcp &" )
		shell( "sh /var/www/clients/demos/jars/*.jar --JASYPT_ENCRYPTOR_PASSWORD=secret > log.txt 2>&1 &")
	}
	wrappers { colorizeOutput() }
}
job('DAS-Ui-e2e-(Master)') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/dasui.git'
				credentials 'bbid'
			}
			extensions { wipeOutWorkspace() }
			branch '*/master'
		}
	}

	steps {
		gradle {
			tasks('clean')
			tasks('clientIntegrationTest')
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
		name('DAS-Ui-Build-(Master)')
		name('DAS-Ui-Sonar-(Master)')
		name('DAS-Ui-Publish-(Master)')
		name('DAS-Ui-Deploy-(Master)')
		name('DAS-Ui-e2e-(Master)')
//		name('iRecruit Service Performance Deploy')
//		name('iRecruit Service Isolation Test')
//		name('iRecruit Service Performance Test')
//		name('iRecruit Service Promote Artifact')
		
	}
}
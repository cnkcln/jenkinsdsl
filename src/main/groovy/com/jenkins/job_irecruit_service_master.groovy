job('iRecruit-Service-Build-(Master)') {
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
			trigger('iRecruit-Service-Sonar-(Master)') {
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

job('iRecruit-Service-Sonar-(Master)') {
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
			trigger('iRecruit-Service-Publish-(Master)') {
				condition('SUCCESS')
				parameters {
					gitRevision()
				}
			}
		}
	}
	
	wrappers { colorizeOutput() }
}

job('iRecruit-Service-Publish-(Master)') {
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
			tasks('uploadArchives')
			switches('-i -Pversion=${GIT_COMMIT}')
			useWrapper()
		}
	}
	
	publishers {
		downstreamParameterized {
			trigger('iRecruit-Service-Deploy-(Master)') {
				condition('SUCCESS')
				parameters {
					gitRevision()
				}
			}
		}
	}
	wrappers { colorizeOutput() }
}

job('iRecruit-Service-Deploy-(Master)') {
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
			tasks('downloadFile')
			switches('-i -Pversion=${GIT_COMMIT}')
			useWrapper()
		}
		shell( "fuser -k 7080/tcp &" )
		shell( "sh /var/www/clients/demos/jars/*.jar --JASYPT_ENCRYPTOR_PASSWORD=secret > log.txt 2>&1 &")
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
		name('iRecruit-Service-Build-(Master)')
		name('iRecruit-Service-Sonar-(Master)')
		name('iRecruit-Service-Publish-(Master)')
		name('iRecruit-Service-Deploy-(Master)')
//		name('iRecruit Service Performance Deploy')
//		name('iRecruit Service Isolation Test')
//		name('iRecruit Service Performance Test')
//		name('iRecruit Service Promote Artifact')
		
	}
}
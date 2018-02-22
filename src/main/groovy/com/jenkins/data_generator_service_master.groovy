job('DAS-Service-Build-(Master)') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/dasdatagenerator.git'
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
			trigger('DAS-Service-Sonar-(Master)') {
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

job('DAS-Service-Sonar-(Master)') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/dasdatagenerator.git'
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
			trigger('DAS-Service-Publish-(Master)') {
				condition('SUCCESS')
				parameters {
					gitRevision()
				}
			}
		}
	}
	
	wrappers { colorizeOutput() }
}

job('DAS-Service-Publish-(Master)') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/dasdatagenerator.git'
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
			trigger('DAS-Service-Deploy-(Master)') {
				condition('SUCCESS')
				parameters {
					gitRevision()
				}
			}
		}
	}
	wrappers { colorizeOutput() }
}

job('DAS-Service-Deploy-(Master)') {
	scm {
		git {
			remote {
				url 'https://ositechportal@bitbucket.org/ositechportal/dasdatagenerator.git'
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
		name('DAS-Service-Build-(Master)')
		name('DAS-Service-Sonar-(Master)')
		name('DAS-Service-Publish-(Master)')
		name('DAS-Service-Deploy-(Master)')
//		name('DAS-Service Performance Deploy') added this to test commit
//		name('DAS-Service Isolation Test')
//		name('DAS-Service Performance Test')
//		name('DAS-Service Promote Artifact')
		
	}
}
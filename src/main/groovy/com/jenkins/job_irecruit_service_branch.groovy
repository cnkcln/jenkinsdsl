job('iRecruit Service Unit And Integration Test - Branch') {
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
		downstreamParameterized {
			trigger('iRecruit Service Sonar -- Branch') {
				condition('SUCCESS')
				parameters { gitRevision() }
			}
		}
		
		buildPipelineTrigger('iRecruit Service Sonar -- Branch') {
			parameters {
				predefinedProp('GIT_COMMIT', gitRevision())
				//predefinedProp('ARTIFACT_BUILD_NUMBER', '$BUILD_NUMBER')
			}
		}
	}
}

job('iRecruit Service Sonar -- Branch') {
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

listView('RS Branch Jobs') {
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
		name('iRecruit Service Unit And Integration Test - Branch')
		name('iRecruit Service Sonar -- Branch ')
	}
}

buildPipelineView('Recruit Service Build Pipeline') {
	filterBuildQueue()
	filterExecutors()
	title('Recruit Service CI Pipeline')
	displayedBuilds(3)
	selectedJob('iRecruit-Service-Build-(Master)')
	selectedJob('iRecruit-Service-Sonar-(Master)')
	selectedJob('iRecruit-Service-Publish-(Master)')
	selectedJob('iRecruit-Service-Deploy-(Master)')
	alwaysAllowManualTrigger()
	showPipelineParameters()
	refreshFrequency(180)
}

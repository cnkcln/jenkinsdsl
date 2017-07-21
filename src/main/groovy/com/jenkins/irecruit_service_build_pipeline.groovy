buildPipelineView('Recruit Service Build Pipeline') {
	filterBuildQueue()
	filterExecutors()
	title('Recruit Service CI Pipeline')
	displayedBuilds(3)
	selectedJob('iRecruit Service Build and Test -- Master')
	alwaysAllowManualTrigger()
	showPipelineParameters()
	refreshFrequency(180)
}

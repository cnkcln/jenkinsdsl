buildMonitorView('Radiator View') {
    description('Radiator for all Services')
    jobs {
		// iRecruit Services Build
		name('iRecruit-Service-Build-(Master)')
		name('iRecruit-Service-Sonar-(Master)')
		name('iRecruit-Service-Publish-(Master)')
		name('iRecruit-Service-Deploy-(Master)')

		//name('iRecruit Service Publish')
		name('iRecruit Service Unit And Integration Test - Branch')
		name('iRecruit Service Sonar -- Branch ')
		
		// Recruitment Portal Build
        name('Recruitment Portal Test - Build')
		// Tax Portal Build
        name('Tax Portal Build and Test')
//        name('Recruit Portal Build and Test')
    }
}


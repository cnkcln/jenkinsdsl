buildMonitorView('Radiator View') {
    description('Radiator for all Services')
    jobs {
		// iRecruit Services Build
		name('iRecruit Service Build and Test -- Master')
		name('iRecruit Service Branch Sonar -- Master')
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

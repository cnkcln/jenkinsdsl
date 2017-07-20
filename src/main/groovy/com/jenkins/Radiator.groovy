buildMonitorView('Radiator View') {
    description('Radiator for all Services')
    jobs {
        name('Recruitment Portal Build and Test')
        name('Tax Portal Build and Test')
        name('Recruit Portal Build and Test')
        name('iRecruit Service Build and Test')
		name('iRecruit Service Branch Sonar')
    }
}

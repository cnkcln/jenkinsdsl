buildMonitorView('Radiator View') {
    description('Radiator for all Services')
    jobs {
		name('Tax-Portal-Build-and-Test(Phase3.1)')
		name('Tax-Portal-Service-Publish(Phase3.1)')
		name('Tax-Portal-Service-Deploy(Phase3.1)')
    }
}

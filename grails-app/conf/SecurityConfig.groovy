security {
	// see DefaultSecurityConfig.groovy for all settable/overridable properties

	active = true

	loginUserDomainClass = "com.stehno.mymdb.domain.User"
	authorityDomainClass = "com.stehno.mymdb.domain.Role"
	requestMapClass = "com.stehno.mymdb.domain.Requesetmap"
	
	useRequestMapDomainClass = false
	useControllerAnnotations = true 	
}

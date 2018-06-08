// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.logout.afterLogoutUrl = "/login/index"
grails.plugin.springsecurity.auth.loginFormUrl = "/login/index"
grails.plugin.springsecurity.failureHandler.defaultFailureUrl = "/login/failedLogin"
grails.plugin.springsecurity.userLookup.userDomainClassName = 'stock.market.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'stock.market.UserRole'
grails.plugin.springsecurity.authority.className = 'stock.market.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/login/**',       access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
    [pattern: '/**',             access: ['ROLE_NO_ROLES']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

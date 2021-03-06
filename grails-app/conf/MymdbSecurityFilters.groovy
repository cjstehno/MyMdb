import org.apache.shiro.SecurityUtils

/**
 * Generated by the Shiro plugin. This filters class protects all URLs
 * via access control by convention.
 */
class MymdbSecurityFilters {
    def filters = {
        mobile(controller:'api', action:'*'){
            before = {
                if( !SecurityUtils.subject.isAuthenticated() && actionName != 'login' ){
                    render( status:401, text:'Unauthorized' )
                    return false
                }
            }
        }
        all(uri: "/**") {
            before = {
                // Ignore direct views (e.g. the default main index page).
                if (!controllerName) return true

                // Access control by convention.
                if( controllerName != 'api' && actionName != 'login' ){
                    accessControl()
                }
            }
        }
    }
}

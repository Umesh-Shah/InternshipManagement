package ca.uwindsor.ims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Forwards all React Router client-side routes to index.html so that
 * deep-links (e.g. /admin/companies) work when served from the JAR.
 *
 * Spring MVC routes more-specific handlers first, so /api/** is never
 * intercepted here. Static files with extensions are served by the
 * ResourceHandler before this controller is ever reached.
 */
@Controller
public class SpaController {

    /** Single-segment routes: /, /admin, /login, /student */
    @GetMapping("/{path:[^\\.]*}")
    public String forwardRoot() {
        return "forward:/index.html";
    }

    /** Multi-segment routes: /admin/companies, /student/profile/edit, etc. */
    @GetMapping("/{path:[^\\.]*}/**")
    public String forwardDeep() {
        return "forward:/index.html";
    }
}

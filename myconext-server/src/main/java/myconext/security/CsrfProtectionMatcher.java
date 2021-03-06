package myconext.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

public class CsrfProtectionMatcher implements RequestMatcher {

    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

    @Override
    public boolean matches(HttpServletRequest request) {
        return request.getServletPath().startsWith("/myconext/api/sp")
                && !allowedMethods.matcher(request.getMethod().toUpperCase()).matches();
    }

}

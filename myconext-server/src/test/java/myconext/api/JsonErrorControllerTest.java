package myconext.api;

import myconext.exceptions.DuplicateUserEmailException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class JsonErrorControllerTest {

    private JsonErrorController subject;

    @Before
    public void before() {
        DefaultErrorAttributes errorAttributes = new DefaultErrorAttributes(true);
        subject = new JsonErrorController(errorAttributes, "http://redirect");
    }

    @Test
    public void getErrorPath() {
        assertEquals("/error", subject.getErrorPath());
    }

    @Test
    public void noError() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity responseEntity = subject.error(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) responseEntity.getBody();
        assertEquals("None", body.get("error"));
    }

    @Test
    public void errorAnnotated() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR",
                new DuplicateUserEmailException());

        ResponseEntity responseEntity = subject.error(request);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) responseEntity.getBody();
        assertEquals(409, body.get("status"));
        assertEquals("myconext.exceptions.DuplicateUserEmailException", body.get("exception"));
    }

    @Test
    public void errorNotAnnotated() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR",
                new IllegalArgumentException("dope"));
        request.setAttribute("javax.servlet.error.status_code", 409);

        ResponseEntity responseEntity = subject.error(request);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Map<String, Object> body = (Map<String, Object>) responseEntity.getBody();
        assertEquals(400, body.get("status"));
        assertEquals("Conflict", body.get("error"));
    }
}
package myconext.manage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.assertEquals;

public class ServiceNameResolverTest {

    private ServiceNameResolver subject = new ServiceNameResolver(new ClassPathResource("sp_names.json"), new ObjectMapper(), false);

    @Test
    public void resolveLocally() throws InterruptedException {
        //Will not change
        assertEquals("SURFconext Manage | SURFconext", subject.resolve("https://manage.surfconext.nl/shibboleth"));
    }
}
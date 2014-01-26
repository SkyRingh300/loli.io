package io.loli.sc.server.service;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import io.loli.sc.server.entity.ClientToken;
import io.loli.sc.server.entity.User;

import java.util.Date;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ClientTokenServiceTest extends
        AbstractTransactionalJUnit4SpringContextTests {
    @Inject
    private ClientTokenService cts;
    @Inject
    private UserService us;

    @Test
    public void testSave() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("test");
        user.setRegDate(new Date());
        us.save(user);
        assertThat(user.getId(), not(0));
        ClientToken ct = new ClientToken();
        ct.setToken("testtoken");
        ct.setUser(user);
        cts.save(ct);
        assertThat(ct.getId(), not(0));
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("test");
        user.setRegDate(new Date());
        us.save(user);
        assertThat(user.getId(), not(0));
        ClientToken ct = new ClientToken();
        ct.setToken("testtoken");
        ct.setUser(user);
        cts.save(ct);
        assertThat(ct.getId(), not(0));

        ClientToken result = cts.findByEmail(user.getEmail());
        assertEquals(ct, result);
    }

}

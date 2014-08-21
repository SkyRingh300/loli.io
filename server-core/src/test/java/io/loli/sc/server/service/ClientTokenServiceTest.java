package io.loli.sc.server.service;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import io.loli.sc.server.entity.ClientToken;
import io.loli.sc.server.entity.User;

import javax.inject.Inject;

import org.junit.Test;

public class ClientTokenServiceTest extends SpringBaseTest {
    @Inject
    private ClientTokenService cts;
    @Inject
    private UserService us;

    @Test
    public void testSave() {
        ClientToken ct = newInstence();
        us.save(ct.getUser());
        assertThat(ct.getUser().getId(), not(0));
        cts.save(ct);
        assertThat(ct.getId(), not(0));
    }

    @Test
    public void testFindByEmail() {
        ClientToken ct = newInstence();
        User user = ct.getUser();
        us.save(user);
        assertThat(user.getId(), not(0));
        cts.save(ct);
        assertThat(ct.getId(), not(0));

        ClientToken result = cts.findByEmail(user.getEmail());
        assertEquals(ct, result);
    }

    @Test
    public void testFindByToken() {
        ClientToken ct = newInstence();
        User user = ct.getUser();
        us.save(user);
        assertThat(user.getId(), not(0));
        cts.save(ct);
        assertThat(ct.getId(), not(0));

        ClientToken result = cts.findByToken("testtoken");
        assertEquals(ct, result);
    }
    public static ClientToken newInstence(){
        ClientToken ct = new ClientToken();
        ct.setToken("testtoken");
        User user = UserServiceTest.newInstence();
        ct.setUser(user);
        
        return ct;
    }
}

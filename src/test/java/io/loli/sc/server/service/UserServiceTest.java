package io.loli.sc.server.service;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import io.loli.sc.server.entity.User;

import java.util.Date;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserServiceTest extends
        AbstractTransactionalJUnit4SpringContextTests {

    @Inject
    private UserService userService;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRegDate(new Date());
    }

    @Test
    public void testSave() {
        userService.save(user);
        assertThat(user.getId(), not(0));
    }

    @Test
    public void testFindById() {
        userService.save(user);
        assertEquals(userService.findById(user.getId()), user);
    }

    @Test
    public void testUpdate() {
        userService.save(user);
        user.setPassword("newpassword");
        userService.update(user);
        assertEquals("newpassword", userService.findById(user.getId())
                .getPassword());

    }

    @Test
    public void testFindByEmail() {
        userService.save(user);
        assertEquals(user, userService.findByEmail(user.getEmail()));
    }
}

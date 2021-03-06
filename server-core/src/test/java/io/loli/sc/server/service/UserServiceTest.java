package io.loli.sc.server.service;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import io.loli.sc.server.entity.User;
import io.loli.util.string.MD5Util;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.inject.Inject;

import org.junit.Test;

public class UserServiceTest extends SpringBaseTest {

    @Inject
    private UserService userService;

    @Test
    public void testSave() {
        User user = newInstence();
        userService.save(user);
        assertThat(user.getId(), not(0));
    }

    @Test
    public void testFindById() {
        User user = newInstence();
        userService.save(user);
        assertEquals(userService.findById(user.getId()), user);
    }

    @Test
    public void testUpdate() {
        User user = newInstence();

        userService.save(user);
        user.setPassword("newpassword");
        userService.update(user);
        assertEquals("newpassword", userService.findById(user.getId())
                .getPassword());

    }

    @Test
    public void testFindByEmail() {
        User user = newInstence();

        userService.save(user);
        assertEquals(user, userService.findByEmail(user.getEmail()));
    }
    
    public static User newInstence(){
        User user = new User();
        try {
            user.setEmail(MD5Util.hash(String.valueOf(new Date().getTime())).substring(27)+"@test.com");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        user.setPassword("password");
        user.setRegDate(new Date());
        
        return user;
    }
}

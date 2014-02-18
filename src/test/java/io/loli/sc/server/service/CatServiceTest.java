package io.loli.sc.server.service;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import io.loli.sc.server.entity.Cat;

import java.util.Date;

import javax.inject.Inject;

import org.junit.Test;
public class CatServiceTest extends SpringBaseTest {
    @Inject
    private CatService cs;

    @Test
    public void testSave() {

        Cat cat = newInstence();
        cs.save(cat);
        assertThat(cat.getId(),not(0));
    }

    public static Cat newInstence() {
        Cat cat = new Cat();
        cat.setName(String.valueOf(new Date().getTime()));
        cat.setUser(UserServiceTest.newInstence());
        return cat;
    }

}

package io.loli.sc.server.service;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.io.File;

import javax.inject.Inject;

import org.junit.Test;

public class FileFetchServiceTest extends SpringBaseTest {

    @Inject
    private FileFetchService fileFetchService;

    @Test
    public void testFetch() {
        File file = fileFetchService.fetch("http://1.loli.io/vqmMNf.jpg");
        assertNotNull(file);
        assertThat(file.length(), not(0l));
    }

}

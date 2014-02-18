package io.loli.sc.server.action;

import static org.junit.Assert.assertThat;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import io.loli.sc.server.entity.Cat;
import io.loli.sc.server.entity.ClientToken;
import io.loli.sc.server.entity.UploadedImage;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.CatService;
import io.loli.sc.server.service.CatServiceTest;
import io.loli.sc.server.service.UserService;
import io.loli.sc.server.service.UserServiceTest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Inject;
import static org.hamcrest.CoreMatchers.not;

import org.hamcrest.CustomMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * User: choco(loli@linux.com) <br/>
 * Date: 2014年1月26日 <br/>
 * Time: 下午11:23:51 <br/>
 * 
 * @author choco
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@WebAppConfiguration
public class ImageClientUploadTest extends
        AbstractTransactionalJUnit4SpringContextTests {

    private MockMvc mockMvc;
    @Inject
    private WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Inject
    private UserService us;

    @Test
    public void testGetToken() throws Exception {
        User user = UserServiceTest.newInstence();
        us.save(user);
        mockMvc.perform(
                post("/api/token")
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .accept(MediaType
                                .parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(
                        content()
                                .contentType(
                                        MediaType
                                                .parseMediaType("application/json;charset=UTF-8")))
                .andExpect(jsonPath("$.token").value(
                // 匿名内部类，自定义Matcher，判断返回的json文的token属性是否是32个字符长的md5密文
                        new CustomMatcher<String>("长度为32的字符串") {
                            public boolean matches(Object object) {
                                return ((object instanceof String)
                                        && !((String) object).isEmpty() && ((String) object)
                                        .length() == 32);
                            }
                        }));
    }

    @Inject
    private ImageClientUpload imageClientUpload;

    @Inject
    private CatService cs;

    @Test
    public void testUpload() throws FileNotFoundException, IOException {
        Cat cat = CatServiceTest.newInstence();
        User user = cat.getUser();
        us.save(user);
        cs.save(cat);

        ClientToken ct = imageClientUpload.requestToken(user.getEmail(),
                user.getPassword());

        File fileToUpload = new File("src/test/resources/" + "imgToUpload.jpg");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image",
                fileToUpload.getName(), "image/jpg", new BufferedInputStream(
                        new FileInputStream(fileToUpload)));

        // 无法模拟上传文件，所以直接调用upload方法
        UploadedImage img = imageClientUpload.upload(ct.getToken(),
                cat.getId(), "This is a test file", mockMultipartFile);
        assertNotNull(img.getPath());
        assertThat(img.getId(), not(0));
    }
}

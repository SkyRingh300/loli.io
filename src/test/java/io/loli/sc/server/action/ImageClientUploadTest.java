package io.loli.sc.server.action;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import io.loli.util.MD5Util;

import javax.inject.Inject;

import org.hamcrest.CustomMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
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
@ContextConfiguration(locations = "classpath:applicationContext.xml")
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

    @Test
    public void testGetToken() throws Exception {
        mockMvc.perform(
                post("/api/token")
                        .param("email", "admin@admin.com")
                        .param("password", MD5Util.hash("admin"))
                        .accept(MediaType
                                .parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType
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

    // @Inject
    // private ImageClientUpload action;
    // @Test
    // public void testWithInjectOnly(){
    // String s = action.requestToken("admin@admin.com", MD5Util.hash("admin"));
    // assertEquals(32,s.length());
    // }
}

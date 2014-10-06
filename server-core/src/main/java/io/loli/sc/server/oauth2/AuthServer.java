package io.loli.sc.server.oauth2;

import io.loli.sc.server.entity.User;
import io.loli.sc.server.entity.oauth2.AccessToken;
import io.loli.sc.server.entity.oauth2.Application;
import io.loli.sc.server.service.UserService;
import io.loli.sc.server.service.oauth2.AppService;
import io.loli.sc.server.service.oauth2.TokenService;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Named
@RequestMapping(value = "oauth2")
@Singleton
public class AuthServer {

    private OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

    @Inject
    private AppService appService;

    @Inject
    private TokenService tokenService;

    @Inject
    private UserService userService;

    // WeakMap 会自动进行垃圾回收
    private Map<String, User> codeMap = Collections.synchronizedMap(new WeakHashMap<String, User>());

    @RequestMapping(value = "auth", method = RequestMethod.GET)
    @ResponseBody
    public String executeGet(HttpServletRequest request, HttpServletResponse response, HttpSession session)
        throws OAuthSystemException, IOException {
        OAuthResponse resp = null;
        try {
            // dynamically recognize an OAuth profile based on request
            // characteristic (params,
            // method, content type etc.), perform validation
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

            if (appService.checkExist(oauthRequest.getClientId())) {
                String username = request.getParameter("username");
                String passwd = request.getParameter("password");
                if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(passwd)) {
                    User user = null;
                    if ((user = userService.findByEmail(username)) != null && user.getPassword().equals(passwd)) {
                        String code = oauthIssuerImpl.authorizationCode();
                        // build OAuth response
                        resp = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)
                            .setCode(code).location(oauthRequest.getRedirectURI()).buildJSONMessage();
                        codeMap.put(code, user);
                    } else {
                        throw OAuthProblemException.error("username or password is incorrect");
                    }
                } else {
                    throw OAuthProblemException.error("username or password is blank");
                }

            } else {
                throw OAuthProblemException.error("App key is invalid");
            }
        } catch (OAuthProblemException ex) {
            resp = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(ex).buildJSONMessage();

        }
        return resp.getBody();
    }

    @RequestMapping(value = "code", method = RequestMethod.POST)
    @ResponseBody
    public String executePost(HttpServletRequest request, HttpServletResponse response, HttpSession session)
        throws OAuthSystemException, IOException {

        OAuthTokenRequest oauthRequest = null;

        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        OAuthResponse r = null;
        try {
            oauthRequest = new OAuthTokenRequest(request);

            // validateClient(oauthRequest);

            String authzCode = oauthRequest.getCode();
            // validate code
            if (codeMap.containsKey(authzCode)) {

                // validate id and secret
                if (appService.verify(oauthRequest)) {
                    // some code
                    String accessToken = oauthIssuerImpl.accessToken();
                    // some code
                    r = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).setAccessToken(accessToken)
                        .buildJSONMessage();

                    Application app = appService.findByKey(oauthRequest.getClientId());
                    AccessToken token = new AccessToken();
                    token.setApp(app);
                    token.setToken(accessToken);
                    token.setExpired(Long.MAX_VALUE);
                    token.setUser((User) session.getAttribute("user"));
                    tokenService.save(token);
                    codeMap.remove(authzCode);
                } else {
                    throw OAuthProblemException.error("app_key or app_secret is incorrect");
                }

            } else {
                throw OAuthProblemException.error("code does not exist");
            }
            // if something goes wrong
        } catch (OAuthProblemException ex) {
            r = OAuthResponse.errorResponse(401).error(ex).buildJSONMessage();
        }
        return r.getBody();
    }
}

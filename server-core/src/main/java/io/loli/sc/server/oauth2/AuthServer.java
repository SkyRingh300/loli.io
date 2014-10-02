package io.loli.sc.server.oauth2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

@Named
@RequestMapping(value = "oauth2")
public class AuthServer {

    OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

    @RequestMapping(value = "auth", method = RequestMethod.GET)
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException,
        IOException {
        try {
            // dynamically recognize an OAuth profile based on request
            // characteristic (params,
            // method, content type etc.), perform validation
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

            // validateRedirectionURI(oauthRequest);

            // build OAuth response
            OAuthResponse resp = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)
                .setCode(oauthIssuerImpl.authorizationCode()).location("http://localhost:8080/server-core/oauth2/code")
                .buildQueryMessage();

            response.sendRedirect(resp.getLocationUri());

            // if something goes wrong
        } catch (OAuthProblemException ex) {
            final OAuthResponse resp = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(ex)
                .location("").buildQueryMessage();

            response.sendRedirect(resp.getLocationUri());
        }
    }

    @RequestMapping(value = "code", method = RequestMethod.POST)
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException,
        IOException {

        OAuthTokenRequest oauthRequest = null;

        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

        try {
            oauthRequest = new OAuthTokenRequest(request);

            // validateClient(oauthRequest);

            String authzCode = oauthRequest.getCode();

            // some code

            String accessToken = oauthIssuerImpl.accessToken();
            String refreshToken = oauthIssuerImpl.refreshToken();

            // some code

            OAuthResponse r = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).setAccessToken(accessToken)
                .setExpiresIn("3600").setRefreshToken(refreshToken).buildJSONMessage();

            response.setStatus(r.getResponseStatus());
            PrintWriter pw = response.getWriter();
            pw.print(r.getBody());
            pw.flush();
            pw.close();

            // if something goes wrong
        } catch (OAuthProblemException ex) {

            OAuthResponse r = OAuthResponse.errorResponse(401).error(ex).buildJSONMessage();

            response.setStatus(r.getResponseStatus());

            PrintWriter pw = response.getWriter();
            pw.print(r.getBody());
            pw.flush();
            pw.close();

            response.sendError(401);
        }
    }

}

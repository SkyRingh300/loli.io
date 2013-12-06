package io.loli.jsr330;

import org.springframework.context.annotation.Jsr330ScopeMetadataResolver;
import org.springframework.web.context.WebApplicationContext;

public class CustomScopeMetadataResolver extends Jsr330ScopeMetadataResolver {

    public CustomScopeMetadataResolver() {
        registerScope(RequestScoped.class, WebApplicationContext.SCOPE_REQUEST);
        registerScope(SessionScoped.class, WebApplicationContext.SCOPE_SESSION);
    }

}
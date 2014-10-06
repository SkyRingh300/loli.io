package io.loli.sc.server.service.oauth2;

import io.loli.sc.server.dao.oauth2.AppDao;
import io.loli.sc.server.entity.oauth2.Application;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.oltu.oauth2.as.request.OAuthRequest;

@Named
public class AppService {
    @Inject
    private AppDao appDao;

    public boolean verify(String key, String secret) {
        return appDao.verify(key, secret);
    }

    public boolean verify(OAuthRequest request) {
        String key = request.getClientId();
        String secret = request.getClientSecret();
        return appDao.verify(key, secret);
    }

    public boolean checkExist(String key) {
        return appDao.checkExist(key);
    }

    public Application findByKey(String clientId) {
        return appDao.findbyKey(clientId);
    }
}

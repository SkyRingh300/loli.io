package io.loli.sc.server.service.oauth2;

import io.loli.sc.server.dao.oauth2.TokenDao;
import io.loli.sc.server.entity.oauth2.AccessToken;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

@Named
public class TokenService {

    @Inject
    private TokenDao tokenDao;

    @Transactional
    public void save(AccessToken token) {
        token.setCreateDate(new Date());
        tokenDao.save(token);
    }
}

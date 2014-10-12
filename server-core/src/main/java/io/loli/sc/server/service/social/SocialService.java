package io.loli.sc.server.service.social;

import io.loli.sc.server.dao.UserDao;
import io.loli.sc.server.dao.social.SocialDao;
import io.loli.sc.server.entity.Social;
import io.loli.sc.server.entity.User;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;

@Named
public class SocialService {

    private static final Logger logger = Logger.getLogger(SocialService.class);

    @Inject
    private SocialDao sd;

    @Inject
    private UserDao ud;

    @Transactional
    public void save(Social s) {
        s.setCreateDate(new Date());
        sd.save(s);
    }

    @Transactional
    public Social save(User user, String userId, String token, String name, String type, long expried) {
        Social s = null;
        try {
            s = sd.findByUserIdAndType(userId, type);
            this.updateToken(userId, token, type, expried);
        } catch (NoResultException e) {
            if (user == null) {
                user = new User();
                user.setName(name);
                user.setRegDate(new Date());
                user.setVip(false);
                user.setType(type);
                ud.save(user);
            }
            s = new Social();
            s.setAccessToken(token);
            s.setUid(userId);
            s.setCreateDate(new Date());
            s.setExpired(expried);
            s.setName(name);
            s.setType(type);
            this.save(s);

            s.setUser(user);

        }
        return s;

    }

    public Social findByUserIdAndType(String userId, String type) {
        return sd.findByUserIdAndType(userId, type);
    }

    @Transactional
    public Social updateToken(String userId, String token, String type, long expried) {
        Social s = sd.findByUserIdAndType(userId, type);
        s.setCreateDate(new Date());
        s.setAccessToken(token);
        return s;
    }

    public List<Social> listByUserId(int id) {
        return sd.listByUserId(id);
    }

}

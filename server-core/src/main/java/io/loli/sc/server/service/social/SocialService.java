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

@Named
public class SocialService {

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

    // 和该方法的另一个重载方法不同，另一个方法的userId是指该social帐号所返回的id，比如你的qq号，而这里是User实体类的id
    public Social findByUserIdAndType(int userId, String type) {
        return sd.findByUserId(userId, type);
    }

    @Transactional
    public void delete(Social social) {
        sd.delete(social);
    }

    public boolean checkExists(String id, String type) {
        try {
            findByUserIdAndType(id, type);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

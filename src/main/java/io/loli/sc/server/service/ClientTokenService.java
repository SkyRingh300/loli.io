package io.loli.sc.server.service;

import io.loli.sc.server.dao.ClientTokenDao;
import io.loli.sc.server.entity.ClientToken;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Named
public class ClientTokenService {
    @Inject
    private ClientTokenDao ctd;

    @Inject
    private UserService us;

    /**
     * 查询此email是否已经有过token了
     * 
     * @param email
     * @return ClientToken对象
     */
    public ClientToken findByEmail(String email) {
        return ctd.findByUId(us.findByEmail(email).getId());
    }

    /**
     * 将ClientToken对象持久化
     * 
     * @param ct
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(ClientToken ct) {
        ctd.save(ct);
    }

    /**
     * 通过token的md5值查找出此token
     * 
     * @param token
     * @return 当查找不到时，返回null
     */
    public ClientToken findByToken(String token) {
        return ctd.findByToken(token);
    }

    /**
     * 判断某个token是否属于指定的用户id
     * @param token
     * @param u_id 用户id
     * @return 如果此token不存在或者不属于指定用户返回false，否则返回true
     */
    public boolean checkTokenBelongToUser(String token, int u_id) {
        ClientToken clientToken = this.findByToken(token);
        return token!=null||clientToken.getUser().getId() == u_id;
    }
    
    /**
     * 判断某个token是否属于指定的用户email
     * @param token
     * @param u_id 用户id
     * @return 如果此token不存在或者不属于指定用户返回false，否则返回true
     */
    public boolean checkTokenBelongToUser(String token, String email) {
        ClientToken clientToken = this.findByToken(token);
        return token!=null||clientToken.getUser().getEmail().equals(email);
    }

}

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

    public ClientToken findByEmail(String email) {
        return ctd.findByUId(us.findByEmail(email).getId());
    }
    
    
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(ClientToken ct) {
        ctd.save(ct);
    }

}

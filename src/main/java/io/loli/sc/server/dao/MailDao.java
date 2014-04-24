package io.loli.sc.server.dao;

import io.loli.sc.server.entity.EmailSendLog;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class MailDao {
    @PersistenceContext
    private EntityManager em;

    public void save(EmailSendLog log) {
        em.persist(log);
    }

    public void update(EmailSendLog log) {
        em.merge(log);
    }

    public EmailSendLog findById(int id) {
        return em.find(EmailSendLog.class, id);
    }

    public EmailSendLog findByEmail(String mail) {
        return em
                .createNamedQuery("EmailSendLog.findByEmail",
                        EmailSendLog.class).setParameter("email", mail)
                .getSingleResult();
    }

    public EmailSendLog findByToken(String token) {
        return em
                .createNamedQuery("EmailSendLog.findByToken",
                        EmailSendLog.class).setParameter("token", token)
                .getSingleResult();
    }

}

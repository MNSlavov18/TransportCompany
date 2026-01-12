package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Client;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class ClientDao {
    public static void save(Client client) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(client);
            tx.commit();
        }
    }
    public static Client getById(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Client.class, id);
        }
    }
    public static List<Client> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Client", Client.class).getResultList();
        }
    }
    public static void update(Client client) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(client);
            tx.commit();
        }
    }

    public static void delete(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Long count = session.createQuery("SELECT COUNT(t) FROM Transport t WHERE t.client.id = :id", Long.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (count > 0) {
                throw new RuntimeException("Не може да изтриете клиента! Той има " + count + " поръчки.");
            }

            Client client = session.get(Client.class, id);
            if (client != null) {
                session.remove(client);
                tx.commit();
            }
        }
    }
}
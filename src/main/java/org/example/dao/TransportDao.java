package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Company;
import org.example.entity.Transport;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class TransportDao {
    public static void save(Transport transport) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(transport);
            if (transport.getCompany() != null && transport.getPrice() != null) {
                Company c = session.get(Company.class, transport.getCompany().getId());
                c.setRevenue(c.getRevenue().add(transport.getPrice()));
                session.merge(c);
            }
            tx.commit();
        }
    }
    public static Transport getById(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Transport.class, id);
        }
    }
    public static List<Transport> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String hql = "SELECT t FROM Transport t LEFT JOIN FETCH t.company LEFT JOIN FETCH t.client LEFT JOIN FETCH t.driver LEFT JOIN FETCH t.vehicle";
            return session.createQuery(hql, Transport.class).getResultList();
        }
    }
    public static List<Transport> getByCompanyId(String companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String hql = "SELECT t FROM Transport t LEFT JOIN FETCH t.company LEFT JOIN FETCH t.client LEFT JOIN FETCH t.driver LEFT JOIN FETCH t.vehicle WHERE t.company.id = :cid";
            return session.createQuery(hql, Transport.class).setParameter("cid", companyId).getResultList();
        }
    }
    public static void update(Transport transport) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(transport);
            tx.commit();
        }
    }
    public static void delete(String id) {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Transport transport = session.get(Transport.class, id);
            if (transport != null) {
                session.remove(transport);
                tx.commit();
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Грешка при изтриване на транспорта.", e);
        } finally {
            session.close();
        }
    }
}
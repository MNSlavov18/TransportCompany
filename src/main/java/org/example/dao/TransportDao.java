package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.TransportDto;
import org.example.entity.Company;
import org.example.entity.Transport;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.stream.Collectors;

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
    public static List<TransportDto> getByCompany(String companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String hql = "SELECT t FROM Transport t LEFT JOIN FETCH t.company LEFT JOIN FETCH t.client LEFT JOIN FETCH t.driver LEFT JOIN FETCH t.vehicle WHERE t.company.id = :cid";
            List<Transport> list = session.createQuery(hql, Transport.class).setParameter("cid", companyId).getResultList();
            return list.stream().map(t -> new TransportDto(
                    t.getId(), t.getStartPoint(), t.getEndPoint(), t.getPrice(),
                    t.getDriver() != null ? t.getDriver().getName() : "N/A",
                    t.getCompany() != null ? t.getCompany().getName() : "N/A",
                    t.getDepartureDate())).collect(Collectors.toList());
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
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Transport transport = session.get(Transport.class, id);
            if (transport != null) session.remove(transport);
            tx.commit();
        }
    }
}
package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.CompanyDto;
import org.example.entity.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyDao {
    public static void save(Company company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(company);
            tx.commit();
        }
    }
    public static Company getById(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Company.class, id);
        }
    }
    public static List<Company> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Company", Company.class).getResultList();
        }
    }
    public static void update(Company company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(company);
            tx.commit();
        }
    }
    public static void delete(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Company company = session.get(Company.class, id);
            if (company != null) session.remove(company);
            tx.commit();
        }
    }
    public static List<CompanyDto> getSorted(boolean byRevenue) {
        return getAll().stream()
                .map(c -> new CompanyDto(c.getName(), c.getRevenue()))
                .sorted(byRevenue
                    ? Comparator.comparing(CompanyDto::getRevenue).reversed()
                    : Comparator.comparing(CompanyDto::getName))
                .collect(Collectors.toList());
    }
    public static BigDecimal getRevenueForPeriod(String companyId, LocalDate start, LocalDate end) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            String hql = "SELECT SUM(t.price) FROM Transport t WHERE t.company.id = :cid AND t.departureDate BETWEEN :start AND :end";
            Query<BigDecimal> query = session.createQuery(hql, BigDecimal.class);
            query.setParameter("cid", companyId);
            query.setParameter("start", start);
            query.setParameter("end", end);
            BigDecimal result = query.uniqueResult();
            return result != null ? result : BigDecimal.ZERO;
        }
    }
}
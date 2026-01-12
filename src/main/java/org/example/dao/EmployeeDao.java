package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class EmployeeDao {
    public static void save(Employee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(employee);
            tx.commit();
        }
    }
    public static Employee getById(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        }
    }
    public static List<Employee> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT e FROM Employee e LEFT JOIN FETCH e.company", Employee.class).getResultList();
        }
    }
    public static void update(Employee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(employee);
            tx.commit();
        }
    }

    public static void delete(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Long count = session.createQuery("SELECT COUNT(t) FROM Transport t WHERE t.driver.id = :id", Long.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (count > 0) {
                throw new RuntimeException("Не може да изтриете този служител! Той има " + count + " записани превоза.");
            }

            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.remove(employee);
                tx.commit();
            }
        }
    }
}
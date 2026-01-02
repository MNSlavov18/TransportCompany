package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.EmployeeDto;
import org.example.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class EmployeeDao {

    public static void save(Employee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(employee);
            tx.commit();
        }
    }

    public static Employee getById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        }
    }

    public static List<Employee> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Employee", Employee.class).getResultList();
        }
    }

    public static void update(Employee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(employee);
            tx.commit();
        }
    }

    public static void delete(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) session.remove(employee);
            tx.commit();
        }
    }

    public static List<EmployeeDto> getSorted(boolean bySalary) {
        return getAll().stream()
                .map(e -> new EmployeeDto(e.getName(), e.getQualification(), e.getSalary()))
                .sorted(bySalary
                    ? Comparator.comparing(EmployeeDto::getSalary).reversed()
                    : Comparator.comparing(EmployeeDto::getQualification))
                .collect(Collectors.toList());
    }
}
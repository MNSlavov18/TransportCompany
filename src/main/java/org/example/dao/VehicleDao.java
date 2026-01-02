package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Vehicle;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class VehicleDao {

    public static void save(Vehicle vehicle) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(vehicle);
            tx.commit();
        }
    }

    public static Vehicle getById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Vehicle.class, id);
        }
    }

    public static List<Vehicle> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Vehicle", Vehicle.class).getResultList();
        }
    }

    public static void update(Vehicle vehicle) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(vehicle);
            tx.commit();
        }
    }

    public static void delete(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, id);
            if (vehicle != null) session.remove(vehicle);
            tx.commit();
        }
    }
}
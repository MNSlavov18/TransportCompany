package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.dto.TransportDto;
import org.example.entity.Company;
import org.example.entity.Transport;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransportDao {

    public static void save(Transport transport) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(transport);

            // Автоматично обновяване на прихода на компанията
            if (transport.getCompany() != null && transport.getPrice() != null) {
                Company c = session.get(Company.class, transport.getCompany().getId());
                c.setRevenue(c.getRevenue().add(transport.getPrice()));
                session.merge(c);
            }
            tx.commit();
        }
    }

    public static Transport getById(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Transport.class, id);
        }
    }

    public static List<Transport> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Transport", Transport.class).getResultList();
        }
    }

    public static void update(Transport transport) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(transport);
            tx.commit();
        }
    }

    public static void delete(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Transport transport = session.get(Transport.class, id);
            if (transport != null) session.remove(transport);
            tx.commit();
        }
    }

    public static List<TransportDto> getSortedByDestination() {
        return getAll().stream()
                .map(t -> new TransportDto(
                        t.getId(), t.getStartPoint(), t.getEndPoint(), t.getPrice(),
                        t.getDriver() != null ? t.getDriver().getName() : "N/A",
                        t.getCompany() != null ? t.getCompany().getName() : "N/A",
                        t.getDepartureDate()))
                .sorted(Comparator.comparing(TransportDto::getEndPoint))
                .collect(Collectors.toList());
    }

    public static void printStats() {
        List<Transport> all = getAll();

        System.out.println("Общ брой превози: " + all.size());

        BigDecimal totalSum = all.stream()
                .map(Transport::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Обща сума: " + totalSum);

        System.out.println("\n--- Брой превози по шофьор ---");
        Map<String, Long> byDriver = all.stream()
            .filter(t -> t.getDriver() != null)
            .collect(Collectors.groupingBy(t -> t.getDriver().getName(), Collectors.counting()));
        byDriver.forEach((d, c) -> System.out.println(d + ": " + c));

        System.out.println("\n--- Приход по шофьор ---");
        Map<String, BigDecimal> revByDriver = all.stream()
            .filter(t -> t.getDriver() != null)
            .collect(Collectors.groupingBy(
                t -> t.getDriver().getName(),
                Collectors.reducing(BigDecimal.ZERO, Transport::getPrice, BigDecimal::add)
            ));
        revByDriver.forEach((d, r) -> System.out.println(d + ": " + r));
    }
}
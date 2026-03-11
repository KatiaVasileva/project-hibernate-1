package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static com.game.entity.Player.GET_TOTAL_NUMBER_OF_PLAYERS;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private final SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/rpg");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "mysql");
        properties.put(Environment.HBM2DDL_AUTO, "update");

        this.sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(Player.class)
                .buildSessionFactory();
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            int offset = pageNumber * pageSize;

            Query<Player> query = session.createNativeQuery(
                    "SELECT * FROM rpg.player LIMIT :pageSize OFFSET :offset",
                    Player.class);

            query.setParameter("pageSize", pageSize);
            query.setParameter("offset", offset);

            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error when getting al players: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getAllCount() {
        try(Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createNamedQuery(GET_TOTAL_NUMBER_OF_PLAYERS, Long.class);
            Long count = query.getSingleResult();
            return count.intValue();
        } catch (Exception e) {
            System.err.println("Error when getting error count: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Player save(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Error when saving player with id = : " + player.getId() + ": " + e.getMessage());
            throw new RuntimeException();
        } finally {
            session.close();
        }
    }

    @Override
    public Player update(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Error when updating player with id = : " + player.getId() + ": " + e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Player> query = session.createQuery("SELECT p FROM Player p WHERE id = :id", Player.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            System.err.println("Error when finding player with id : " + id + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Player player) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(player);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.out.println("Error when deleting player with id = : " + player.getId() + ": " + e.getMessage());
        }
    }

    @PreDestroy
    public void beforeStop() {
        sessionFactory.close();
    }
}
package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();

            return user;
        } catch (Exception e) {
            session.getTransaction().rollback();
            return null;
        }
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                    "UPDATE User SET login = :login, password = :password WHERE id = :id")
                    .setParameter("login", user.getLogin())
                    .setParameter("password", user.getPassword())
                    .setParameter("id", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(Integer userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                    "DELETE User WHERE id = :id")
                    .setParameter("id", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        Session session = sf.openSession();
        try {
            Query<User> query = session.createQuery("FROM User ORDER BY id", User.class);
            return query.getResultList().stream().toList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(Integer userId) {
        Session session = sf.openSession();
        User user = session.get(User.class, userId);
        return Optional.ofNullable(user);
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        Session session = sf.openSession();
        try {
            Query<User> query = session.createQuery(
                    "FROM User WHERE login LIKE :key", User.class);
            query.setParameter("key", "%" + key + "%");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            session.close();
        }
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Session session = sf.openSession();
        try {
            Query<User> query = session.createQuery(
                    "FROM User WHERE login = :login", User.class);
            query.setParameter("login", login);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            session.close();
        }
    }

}
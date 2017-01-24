package com.technologyconversations.books;

import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jMarshall on 1/23/17.
 */
public class BookDao {
    /* check this out for info on volatile variables
     * https://www.ibm.com/developerworks/java/library/j-jtp06197/
     */
    private static volatile BookDao instance = null; //https://www.ibm.com/developerworks/java/library/j-jtp06197/

    public static synchronized BookDao getInstance() {
        if (instance == null) {
            instance = new BookDao();
        }
        return instance;
    }


    public void deleteAllBooks() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.createQuery("delete from Book").executeUpdate();
        session.close();
    }

    public void saveOrUpdateBook(Book book) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.saveOrUpdate(book);
        session.flush();
        session.close();
    }

//    public List<Book> getAllBooks() {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        @SuppressWarnings("unchecked")
//        List<Book> books = session.createQuery("from Book").list();
//        session.close();
//        return books;
//    }

    public List<Book> getAllBooks() {
        return getAllBooks(0, 0);
    }

    public List<Book> getAllBooks(int firstResult, int maxResult) {
        List<Book> books = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("select id, title from Book");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);
        @SuppressWarnings("unchecked")
        List allUsers = query.list();
        for (Object allUser : allUsers) {
            Object[] bookObject = (Object[]) allUser;
            Book book = new Book((Integer) bookObject[0]);
            book.setTitle((String) bookObject[1]);
            books.add(book);
        }
        session.close();
        return books;
    }

    public Book getBook(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Book book = (Book) session.get(Book.class, id);
        session.close();
        return book;
    }

    public Book deleteBook(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Book book = getBook(id);
        if (book != null) {
            session.delete(book);
            session.flush();
        }
        session.close();
        return book;
    }
}

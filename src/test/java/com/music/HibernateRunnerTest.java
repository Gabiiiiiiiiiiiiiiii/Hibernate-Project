package com.music;

import com.music.entity.Birthday;
import com.music.entity.Company;
import com.music.entity.Profile;
import com.music.entity.User;
import javax.persistence.Column;
import javax.persistence.Table;

import com.music.util.HibernateUtil;
import lombok.Cleanup;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.*;

class HibernateRunnerTest {

    @Test
    void checkOneToOne(){
        try( var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession();){
            session.beginTransaction();

            var user = session.get(User.class, 7L);
            System.out.println();
//            var user = User.builder()
//                    .username("test2@gmail.com")
//                            .build();
//            var profile = Profile.builder()
//                    .language("ru")
//                    .street("Romaniv 13")
//                    .build();
//
//            session.save(user);
//            profile.setUser(user);
//            session.save(profile);

            session.getTransaction().commit();
        }
    }

//    @Test
//    void checkOrphanRemoval(){
//        try( var sessionFactory = HibernateUtil.buildSessionFactory();
//             var session = sessionFactory.openSession();){
//            session.beginTransaction();
//
//            Company company = session.getReference(Company.class, 4);
//            company.getUsers().removeIf(user -> user.getId().equals(3L));
//
//            session.getTransaction().commit();
//        }
//    }

//    @Test
//    void checkLazyInitialization(){
//        Company company = null;
//        try( var sessionFactory = HibernateUtil.buildSessionFactory();
//               var session = sessionFactory.openSession();){
//            session.beginTransaction();
//
//            company = session.get(Company.class, 4);
//            var users = company.getUsers();
//            System.out.println(users.size());
//
//            session.getTransaction().commit();
//        }
//
//
//    }

//    @Test
//    void deleteCompany() {
//        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
//        @Cleanup var session = sessionFactory.openSession();
//        session.beginTransaction();
//
//        var user = session.get(User.class, 1L);
//        session.delete(user);
//
//        session.getTransaction().commit();
//    }

//    @Test
//    void addUserToNewCompany(){
//        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
//        @Cleanup var session = sessionFactory.openSession();
//        session.beginTransaction();
//
//        var company = Company.builder()
//                        .name("facebook")
//                        .build();
//
//        var user = User.builder()
//                        .username("sveta@gmail.com")
//                        .build();
//
////        Вместо этого используем addUsers
////        user.setCompany(company);
////        company.getUsers().add(user);
////
//        company.addUser(user);
//
//        session.save(company);
//
//        session.getTransaction().commit();
//    }

//    @Test
//    void oneToMany(){
//        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
//        @Cleanup var session = sessionFactory.openSession();
//        session.beginTransaction();
//
//        var company = session.get(Company.class, 4);
//        System.out.println(company.getUsers());
//
//        session.getTransaction().commit();
//    }

//    @Test
//    void getReflectionApi() throws SQLException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        PreparedStatement ps = null;
//        ResultSet rs = ps.executeQuery();
//
//        rs.getString("username");
//        rs.getString("firstname");
//        rs.getString("lastname");
//
//        Class<User> classs = User.class;
//        Constructor<User> constructor = classs.getConstructor();
//        User user = constructor.newInstance();
//        Field usernameField = classs.getDeclaredField("username");
//        usernameField.setAccessible(true);
//        usernameField.set(user, rs.getString("username"));
//    }

    @Test
    void checkReflectionApi() throws SQLException, IllegalAccessException {
        User user = User.builder()
//                .username("Ivan@tt.com")
//                .firstname("Ivan")
//                .lastname("Ivanov")
//                .birthDate(new Birthday(LocalDate.of(2004, 01, 29)))
                .build();


        String sql = """
                insert
                into
                    %s
                    (%s)    
                values
                (%s)
                """;
        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + tableAnnotation.name())
                .orElse(user.getClass().getName());

        Field[] fields = user.getClass().getDeclaredFields();

        String columnNames = Arrays.stream(user.getClass().getDeclaredFields())
                .map(field -> ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(joining(", "));

        String columnValues = Arrays.stream(fields)
                .map(field -> "?")
                .collect(joining(", "));

        System.out.println(sql.formatted(tableName, columnNames, columnValues));

        Connection connection = null;
        PreparedStatement preparedStatement = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues));
        for(Field field : fields) {
            field.setAccessible(true);
            preparedStatement.setObject(1, field.get(user));
        }
    }

}
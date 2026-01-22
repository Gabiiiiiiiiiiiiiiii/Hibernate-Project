package com.music;

import com.music.converter.BirthdayConverter;
import com.music.entity.*;
import com.music.util.HibernateUtil;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.BlockingDeque;

@Slf4j
public class HibernateRunnerLifecyclePractice {

    //Вместо этой строчки можно написать аннотацию от Lombok
//    private static final Logger log = LoggerFactory.getLogger(HibernateRunnerLifecyclePractice.class );

    public static void main(String[] args) throws SQLException {

        Company company = Company.builder()
                .name("Amazonnn")
                .build();

        //по отношению к обеим сессиям наш юзер в состоянии transient
        User user = User.builder()
                .username("inga@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Petrov")
                        .lastname("Petr")
                        .birthDate(new Birthday(LocalDate.of(2000, 2, 23)))
                        .build())
//                .firstname("Ivan")
//                .lastname("Ivanov")
                .company(company)
                .build();

//        log.info("User is in transient state, object: " + user);
//        log.info("User is in transient state, object: {}", user);

//Обратились к HibernateUtil, поместили туда метод для работы с конфигурацией


//      Надо закрывать SessionFactory
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();

            try (session1) {
                Transaction transaction = session1.beginTransaction();
//                log.trace("Transaction is created {}", transaction);

//                User user1 = session1.get(User.class, 1L);
//                session1.evict(user1);

                session1.save(user);

//                Company company1 = user1.getCompany();
//                String name = company1.getName();

                //Метод для получения реального экземпляра сущности Company
//                Object unproxy = Hibernate.unproxy(company1);

                //Юзер в персистэнт состоянии по отношению к session1
                // но в transient - к session2
                //Обязательно сохраняем в начале эту сущность, тк она часть сущеость юзер
//                session1.save(company);
//                session1.save(user);
//                log.trace("User is in persistent state: {}, session: {}", user, session1);

//Здесь после закрытия сессии1 юзер в детач состоянии по отношению к session1
                //После закрытия уже session1 не существует, переходим ко второй
                session1.getTransaction().commit();
            }

//            log.warn("User is in detached state: {}, session is closed: {}", user, session1);

//            try (Session session2 = sessionFactory.openSession()) {
//                PersonalInfo key = PersonalInfo.builder()
//                                        .firstname("Petrov")
//                                        .lastname("Petr")
//                                        .birthDate(new Birthday(LocalDate.of(2000, 2, 23)))
//                                        .build();
////
//                User user2 = session2.get(User.class, key);
//                System.out.println();
//            }

        }
//        catch (Exception e) {
//            log.error("Exception occured", e);
//            throw e;
//        }

//            try (Session session2 = sessionFactory.openSession()) {
//                session2.beginTransaction();
//
//                //Переход через persistante без появлении в transient, через делит
//                //тк делит предполаает в начале get, чтобф проассоциировать юзера с session2, а это уже managed state
//                //и как результат - состояние removed
////                session2.delete(user);
//
//                user.setFirstname("Ignat");
//                //refresh игнорирует изменения, а обновляет запись в таблице по id, обнуляя изменения
////                session2.refresh(user);
//
//                //Возвращает нового юзера сливая изменения с исходной записью
//                Object mergedUser = session2.merge(user);
//
//                session2.getTransaction().commit();
//            }

        }



    }


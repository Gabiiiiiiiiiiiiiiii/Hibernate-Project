package com.music;

import com.music.converter.BirthdayConverter;
import com.music.entity.Birthday;
import com.music.entity.PersonalInfo;
import com.music.entity.Role;
import com.music.entity.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.BlockingDeque;

public class HibernateRunner {
    public static void main(String[] args) throws SQLException {

        //Connection оборачиваем в класс Session
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
//        Deprecated
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        //Добавляем конвертер, который хибернэйт должен использовать всегда при работе с датой рождения
        //чтобы не писать это как аннотацию над каждой колонкой, которую это касается

        configuration.addAttributeConverter(new BirthdayConverter(), true);

        //Добавление любого созданного типа данных, которых нет в джава sql,
        //Но в более новых версиях джэйсон встроен в ядро хибернэйт
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();

//      Надо закрывать SessionFactory
        try (SessionFactory sessionFactory = configuration.buildSessionFactory();

//Создание обертки вокруг Connection, для добавления большего функционала для работы с сущностями
             //Создание изолированного места в памяти, для работы с конкретными объектоми
             //теперь объект user може быть managed только в рамках этой сессии, др ничего не знают о нем
             //и
             //Это принцип ИЗОЛИРОВАННОСТИ и ОТВЕТСТВЕННОСТИ каждой сессии
             Session session = sessionFactory.openSession()) {
            System.out.println("OK");

            session.beginTransaction();

             User user = User.builder()
                     .username("test@tt.com")
                     .personalInfo(PersonalInfo.builder()
                             .firstname("Petrov")
                             .lastname("Petr")
                             .build())
                     .info("""
                             {
                                 "name":"Ivan",
                                 "id": 25
                             }
                             """)
//                     .birthDate(new Birthday(LocalDate.of(2004, 01, 29)))
                     .role(Role.ADMIN)
                     .build();

//Сохраняем сущность юзер в сессии, переводя сущность из состояния transient в managed
//             session.save(user);

            //Пробрасывает исключение, если такого юзера нет для апдэйта
//             session.update(user);

//             session.saveOrUpdate(user);

//             session.delete(user);

            //Преобразует sql тип  в java тип,
             User user1 = session.get(User.class, "Iva10n@tt.com");
//             User user2 = session.get(User.class, "Iva12n@tt.com");
//             user1.setLastname("Petrov2");
//Проверяем есть ли изменения и сливаем состояние L1 кэша в бд с помощью соответствующих sql-запросов
             session.flush();

             //Проверяем, есть ли изменения в записи, которые нужно залить в бд(true/false)
            System.out.println(session.isDirty());
//Удаление сущности из кэша, то есть из Persistence Context
//            session.evict(user1);

            //Чистим весь кэш, всю хэшмапу
//            session.clear();

            //Удаление кэша, когда выходим из трай виз ресорсис
//            session.close();

             session.getTransaction().commit();
        }



    }
}

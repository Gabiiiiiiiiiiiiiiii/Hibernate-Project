package com.music.util;

import com.music.converter.BirthdayConverter;
import com.music.entity.Company;
import com.music.entity.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        //Connection оборачиваем в класс Session
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Company.class);//Это не совсем сработало для этой сущности
//        Deprecated
//        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        //Добавляем конвертер, который хибернэйт должен использовать всегда при работе с датой рождения
        //чтобы не писать это как аннотацию над каждой колонкой, которую это касается

        configuration.addAttributeConverter(new BirthdayConverter(), true);

        //Добавление любого созданного типа данных, которых нет в джава sql,
        //Но в более новых версиях джэйсон встроен в ядро хибернэйт
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();



        return configuration.buildSessionFactory();
    }
}

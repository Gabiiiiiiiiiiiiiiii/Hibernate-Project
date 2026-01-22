package com.music;

import com.music.entity.Company;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.hibernate.proxy.ProxyConfiguration;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor;


//Обертка вокруг настоящего объекта Company
//Ленивый наслденик сущности
//После закрытия сущности - запросы дадут LazyИсключение
//Имеет только айди компании
public class CompanyProxy extends Company implements HibernateProxy, ProxyConfiguration {

    //Перехватчик, собирает запросы(getName()) и идет в бд за ответом
    private ByteBuddyInterceptor byteBuddyInterceptor;

    @Override
    public Object writeReplace() {
        return null;
    }

    @Override
    public LazyInitializer getHibernateLazyInitializer() {
        return byteBuddyInterceptor;
    }

    @Override
    public void $$_hibernate_set_interceptor(Interceptor interceptor) {

    }
}

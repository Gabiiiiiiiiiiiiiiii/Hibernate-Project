package com.music;

import com.music.entity.Company;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Proxy;

//Динамический прокси, где в параметре обязательно нужны интерфейсы, но у нашего Company их нет
//поэтому второй вариант создания прокси, это создать джава класс с extend от Company в обход интерфейсов
public class ProxyTest {
    @Test
    void testDynamic(){
        Company company = new Company();
        Proxy.newProxyInstance(company.getClass().getClassLoader(), company.getClass().getInterfaces(),
                (proxy, method, args) -> method.invoke(company, args));
    }
}

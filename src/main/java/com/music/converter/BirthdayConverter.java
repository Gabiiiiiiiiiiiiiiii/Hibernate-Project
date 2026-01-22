package com.music.converter;

import com.music.entity.Birthday;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.sql.Date;
import java.util.Optional;

@Converter(autoApply = true) //либо так, либо в HibernateRunner как 2-й параметр при addAttributeConverter
public class BirthdayConverter implements AttributeConverter <Birthday, Date> {

    @Override
    public Date convertToDatabaseColumn(Birthday attribute) {
        return Optional.ofNullable(attribute)
                .map(Birthday::birthDate)
                .map(Date::valueOf)
                .orElse(null);
    }

    @Override
    public Birthday convertToEntityAttribute(Date dbDate) {
        //
        return Optional.ofNullable(dbDate)
                //преобразуем все в локалдэйт
                .map(Date::toLocalDate)
                //преобразуем все в берздэй класс
                .map(Birthday::new)
                .orElse(null);
    }
}

package com.music.entity;

import com.music.converter.BirthdayConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Embeddable
// Embeddable компонент
public class PersonalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String firstname;
    private String lastname;

//        @Convert(converter = BirthdayConverter.class)
    //Вместо этого можно в самом юзер классе использовать @AttributeOverride, чтобы поля в юзере и в персоналИнфо совпадали
    @Column(name="birth_date")
    private Birthday birthDate;

}

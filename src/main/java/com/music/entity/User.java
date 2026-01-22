package com.music.entity;

import com.music.converter.BirthdayConverter;
import javax.persistence.*;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
//import org.hibernate.annotations.JdbcTypeCode;
//import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Objects;


@Data
@ToString(exclude={"company","profile"})
@EqualsAndHashCode(of="username")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@TypeDef(name="info", typeClass= JsonBinaryType.class)
@Table(name="users", schema="public")

//@Access(AccessType.FIELD) - стоит по умолчанию для получения напрямую значений полей, поэтому его не пишем
//Второй вариант - непредпочтителен
//@Access(AccessType.PROPERTY) - привязка к геттерам полей без ломбока
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @EmbeddedId
//    @Embedded //необязательнная аннотация
//    @AttributeOverride(name="birthDate", column =@Column(name="birth_date"))
    private PersonalInfo personalInfo;


//    @Id
//    //Есть еще AUTO: IDENTITY, SEQUENSE, TABLE
//    //IDENTITY - выигрывает по производительности, используется чаще
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//
//    //Если в бд не поддерживается идентити
////    @GeneratedValue(generator = "user_gen", strategy = GenerationType.SEQUENCE)
////    @SequenceGenerator(name = "user_gen", sequenceName = "users_id_seq", allocationSize = 1)
//
//
//    //ЕСли нет ни идентити, ни сиквэнс
//    @GeneratedValue(generator = "user_gen", strategy = GenerationType.TABLE)
//    @TableGenerator(name = "user_gen", table = "all_sequence",
//            pkColumnName = "table_name", valueColumnName = "pk_value", allocationSize = 1)
//    private Long id;

    @Column(unique = true)
    private String username;


    //Убрали, так как сгруппировали их в персоналинфо, как embeddable
//    private String firstname;
//    private String lastname;
//
//    //Подсказка хибернэйту, какой класс использовать для конвертации Birthday в джава sql дэйт и наоборот
//    @Convert(converter = BirthdayConverter.class)
//    @Column(name="birth_date")
//    private Birthday birthDate;

    //Три варианта для указания нового типа данных
//    @Type(type = "jsonb")
//    @Type(type = "info")
    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType")
    @Column(columnDefinition="jsonb")
    private String info;

    //Redundant значение, тк его можно вычислить через дату
    //private Integer age;

//    @Transient - если мы не хотим сохранять это поле в бд
    //Лучше в сущности хранить только те поля, которые действительно мапятся на конкретные поля в бд
    @Enumerated(EnumType.STRING)
    private Role role;

    //default FetchType = Eager
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="company_id")
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;
    //@ColumnTransformer - если необходимо изменить значение поля при записи либо при чтении
    //Пример, для write - encrypt(?), для read - decrypt

//    @Formula - то же, что и с ColumnTransformer, но здесь только sql для чтения, на запись нельзя
}

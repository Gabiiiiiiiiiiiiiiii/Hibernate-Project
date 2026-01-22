package com.music.entity;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//POJO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@ToString(exclude="users")
@EqualsAndHashCode(of="name")

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder.Default
    //Eager - нежелательно, тащит очень много записей,
    //default FetchType = Lazy
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    //Указываем эту аннотацию, если у нас нет обратной аннотации @ManyToOne
    //Иначе в параметре указываем mappedBy со значением поля во второй сущности
//    @JoinColumn(name="company_id")
    private Set<User> users = new HashSet<>();


    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }
}

package ru.practicum.shareit.user;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "schema")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;

}

package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Builder
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer ID;

    @Column(name = "name")
    private String userName;

    private Integer age;

    private String gender;

    private String email;

    private String phone;

    private Integer votes;
}

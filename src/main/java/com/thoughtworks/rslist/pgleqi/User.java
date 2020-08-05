package com.thoughtworks.rslist.pgleqi;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class User {
    @NotNull
    @Size(max = 8)
    @JsonProperty("user_name")
    @JsonAlias("userName")
    private String userName;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty("user_age")
    @JsonAlias("age")
    private Integer age;

    @NotNull
    @Pattern(regexp = "male|female|lesbian|gay|bisexual|transgender")
    @JsonProperty("user_gender")
    @JsonAlias("gender")
    private String gender;

    @Email
    @JsonProperty("user_email")
    @JsonAlias("email")
    private String email;

    @NotNull
    @Pattern(regexp = "1\\d{10}")
    @JsonProperty("user_phone")
    @JsonAlias("phone")
    private String phone;

    public User(@NotNull @Size(max = 8) String userName,
                @NotNull @Min(18) @Max(100) int age,
                @NotNull @Pattern(regexp = "male|female|lesbian|gay|Bisexual|Transgender") String gender,
                @Email String email,
                @Pattern(regexp = "1\\d{10}") String phone)
    {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        User user = (User) obj;
        return user.userName.equals(this.userName);
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

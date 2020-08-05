package com.thoughtworks.rslist.pgleqi;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class User {
    @NotNull
    @Size(max = 8)
    private String userName;

    @Min(18)
    @Max(100)
    private int age;

    @NotNull
    @Pattern(regexp = "male|female|lesbian|gay|Bisexual|Transgender")
    private String gender;

    @Email
    private String email;

    @Pattern(regexp = "1\\d{10}")
    private String phone;

    public User(@NotNull @Size(max = 8) String userName,
                @Min(18) @Max(100) int age,
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
}

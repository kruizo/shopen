package com.bkr.shopen.model;

import java.time.Instant;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid") 
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    private String address;
    private String phoneNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    public User(String phoneNumber, String address, String password, String email, String name) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
               Objects.equals(name, user.name) &&
               Objects.equals(password, user.password) &&
               Objects.equals(address, user.address) &&
               Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password, address, phoneNumber);
    }


}

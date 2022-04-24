package com.example.demo.entity;

import com.example.demo.entity.enums.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

@Entity(name = "UserEntity")
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    @Positive(message = "Id must be positive value")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false)
    private Boolean enabled = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getEnabled(), that.getEnabled()) && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPassword(), getEmail(), getEnabled(), getStatus());
    }
}

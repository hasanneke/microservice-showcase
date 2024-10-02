package com.mentoapp.mentoapp.Entity.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mentoapp.mentoapp.Entity.Category.Category;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email", unique = true)
    private String email;

    @ElementCollection(targetClass = UserRole.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role")
    private Set<UserRole> roles = new HashSet<>();

    @Column(name = "url_avatar")
    private String urlAvatar;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "user_category",
//            joinColumns = {
//                    @JoinColumn(name = "user_id", referencedColumnName = "id")
//            },
//            inverseJoinColumns = {
//                    @JoinColumn(name = "category_id", referencedColumnName = "id")
//            }
//    )
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Category> categories = new HashSet<>();
    @OneToMany(mappedBy = "mentor")
    private Set<MentorSession> mentorSessions = new HashSet<>();
    @OneToMany(mappedBy = "mentee")
    private Set<MentorSession> menteeSessions = new HashSet<>();

    @Column(name = "description")
    private String description;

    @Column(name = "review_count", columnDefinition = "integer default 0")
    private Integer reviewCount = 0;

    @Column(name = "review_score", columnDefinition = "double precision default 0.0")
    private Double reviewScore = 0.0;

    @Column(name = "session_count", columnDefinition = "integer default 0")
    private Integer sessionCount = 0;

    @Column(name = "website")
    private String website;

    @Column(name = "linkedIn")
    private String linkedIn;

    public void addRole(UserRole userRole){
        roles.add(userRole);
    }
}



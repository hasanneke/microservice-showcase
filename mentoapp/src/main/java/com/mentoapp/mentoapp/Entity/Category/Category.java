package com.mentoapp.mentoapp.Entity.Category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mentoapp.mentoapp.Entity.MentorApplication.MentorApplication;
import com.mentoapp.mentoapp.Entity.Session.MentorSession;
import com.mentoapp.mentoapp.Entity.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private int level;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "user_category",
            joinColumns = {
                    @JoinColumn(name = "category_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id")
            }
    )
    @JsonIgnore
    private List<User> users = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "mentor_application_category",
            joinColumns = {
                    @JoinColumn(name = "category_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "mentor_application_id", referencedColumnName = "id")
            }
    )
    @JsonIgnore
    private List<MentorApplication> applications = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "mentor_session_category",
            joinColumns = {
                    @JoinColumn(name = "category_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "mentor_session_id", referencedColumnName = "id")
            }
    )
    @JsonIgnore
    private List<MentorSession> sessions = new ArrayList<>();

    public void addUser(User user){
        users.add(user);
    }

    public void addSession(MentorSession session){
        sessions.add(session);
    }

    public void addApplication(MentorApplication application){
        applications.add(application);
    }
}

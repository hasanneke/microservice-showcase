package com.mentoapp.mentoauth.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Entry(objectClasses = { "person", "top","organizationalPerson","inetOrgPerson"},  base="ou=users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class People {
    @Id
    private Name dn;

    @Attribute(name = "uid")
    private String email;

    @Attribute(name="cn")
    @DnAttribute(value="cn", index=1)
    private String commonName;

    @Attribute(name="sn")
    private String surname;

    @Attribute(name = "userPassword")
    @JsonIgnore
    private String password;
}
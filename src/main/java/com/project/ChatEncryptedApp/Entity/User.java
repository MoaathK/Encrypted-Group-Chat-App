package com.project.ChatEncryptedApp.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table (name="Users")
@AllArgsConstructor
@RequiredArgsConstructor

public class User {
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)
@Column (name="ID")
private int ID;


@Column (name = "UserID")
private String userID;

@Column (name = "N")
private int n;

@Column (name="PrivateKey")
private int privateKey;

@Column (name="PublicKey")
private int publicKey;

@Column (name="Name")
private String name;

@Column (name="Password")
private String password;

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", userID='" + userID + '\'' +
                ", n=" + n +
                ", privateKey=" + privateKey +
                ", publicKey=" + publicKey +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

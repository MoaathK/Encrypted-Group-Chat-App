package com.project.ChatEncryptedApp.Repository;

import com.project.ChatEncryptedApp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public interface UserRepository extends JpaRepository <User,Integer>{
User findByUserID(String userID);


}


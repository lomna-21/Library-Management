package com.example.LibraryManagement.Security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SecuredUser,Integer> {

    SecuredUser findByUsername(String name);
}

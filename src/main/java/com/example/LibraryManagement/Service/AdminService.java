package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.DAO.AdminCacheRepository;
import com.example.LibraryManagement.DAO.AdminRepository;
import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Security.SecuredUser;
import com.example.LibraryManagement.Security.SecurityService;
import com.example.LibraryManagement.Utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    AdminCacheRepository admincacheRepository;

    @Autowired
    SecurityService service;

    public void create(Admin admin){
        SecuredUser securedUser = admin.getSecuredUser();
        securedUser = service.save(securedUser, Constants.ADMIN_USER);

        admin.setSecuredUser(securedUser);
        adminRepository.save(admin);
    }

    public Admin find(Integer adminId) {
        Admin admin =admincacheRepository.get(adminId);

        if(admin != null){
            return admin;
        }
        admin = adminRepository.findById(adminId).orElse(null);
        if(admin != null){
        admincacheRepository.set(admin);
        }
        return admin;
    }
    }
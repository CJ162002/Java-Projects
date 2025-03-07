package com.manager.smartcontactmanager.Repositry;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.manager.smartcontactmanager.Entity.Contacts;
import com.manager.smartcontactmanager.Entity.User;

@Repository
public interface ContactsRepositry extends JpaRepository<Contacts, Long> {
    boolean existsByName(String Name);
    boolean existsByPhone(String Phone);
    
    @Query("select c from Contacts c where c.user = :id")
    //Pageable takes number of contacts in the current page
    //and also takes number of pages
    public Page<Contacts> getAllContacts(@Param("id") User id, Pageable pageable);
}

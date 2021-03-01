package com.scma.anilg.dao;

import javax.websocket.server.PathParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scma.anilg.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
		
		@Query("from Contact as c WHERE c.user.id= :userId")
		public Page<Contact> getContactsByUser(@PathParam("userId") int userId, Pageable pagiable);
}

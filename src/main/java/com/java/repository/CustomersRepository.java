package com.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.entity.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, String>{
	
	@Query(value = "select * from customers where customerId = ?", nativeQuery = true)
    public Customer findCustomersLogin (String customerId);

//    @Query(value = "select * from customers where roleId != '1' ", nativeQuery = true)
	@Query(value = "select * from customers where roleId != '1'", nativeQuery = true)
    List<Customer> findAllRoleUser();

    Optional<Customer> findByEmail(String email);
}

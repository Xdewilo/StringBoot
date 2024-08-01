package com.rutaoptimaspring.cargadatosbig.Repository;

import com.rutaoptimaspring.cargadatosbig.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}


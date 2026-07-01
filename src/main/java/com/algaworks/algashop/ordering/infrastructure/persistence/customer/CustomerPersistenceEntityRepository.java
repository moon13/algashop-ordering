package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface CustomerPersistenceEntityRepository
        extends JpaRepository<CustomerPersistenceEntity, UUID> ,
            CustomerPersistenceEntityQueries{

      Optional<CustomerPersistenceEntity> findByEmail(String value);
      boolean existsByEmailAndIdNot(String email, UUID customerId);
}

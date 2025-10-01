package com.codewithmosh.store.carts;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID>, JpaSpecificationExecutor<Cart> {}
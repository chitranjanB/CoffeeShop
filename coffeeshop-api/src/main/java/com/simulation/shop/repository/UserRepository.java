package com.simulation.shop.repository;

import com.coffee.shared.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByEmailIdIgnoreCaseAndPassword(String emailId, String password);
}
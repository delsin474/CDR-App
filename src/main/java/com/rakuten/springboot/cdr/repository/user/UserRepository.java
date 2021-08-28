package com.rakuten.springboot.cdr.repository.user;

import com.rakuten.springboot.cdr.model.user.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Mohit Chandak
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

}

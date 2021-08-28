package com.rakuten.springboot.cdr.repository.user;

import com.rakuten.springboot.cdr.model.user.Role;
import com.rakuten.springboot.cdr.model.user.UserRoles;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Mohit Chandak
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByRole(UserRoles role);

}

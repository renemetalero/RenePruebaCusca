package backend.apiscart.dao;

import backend.apiscart.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsersDao extends JpaRepository<Users, Long> {
    Optional<Users> findByUserName(String userName);
}

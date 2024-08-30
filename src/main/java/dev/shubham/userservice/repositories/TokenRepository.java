package dev.shubham.userservice.repositories;

import dev.shubham.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    @Override
   Token save(Token token);

    Optional<Token> findByValueAndAndDeleted(String token,boolean deleted);
    Optional<Token> findByValueAndDeletedAndExpireAtGreaterThan(String token, boolean deleted, Date expireAt);
}

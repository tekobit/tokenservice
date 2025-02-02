package com.zufarov.tokenservice.repositories;

import com.zufarov.tokenservice.models.UniqueToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniqueTokenRepository extends JpaRepository<UniqueToken, Long> {


    @Query(value = "SELECT NEXTVAL('unique_tokens') FROM generate_series(1,1)", nativeQuery = true)
    List<Long> getNextSequenceValue();
}
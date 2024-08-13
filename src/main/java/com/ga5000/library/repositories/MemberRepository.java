package com.ga5000.library.repositories;

import com.ga5000.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    UserDetails findByUsername(String username);
}

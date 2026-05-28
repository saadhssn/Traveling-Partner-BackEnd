package com.travelpartner.user.repository;

import com.travelpartner.user.enums.UserStatus;
import com.travelpartner.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByMobileNumber(String mobileNumber);

    boolean existsByEmail(String email);

    List<User> findByIsDeletedFalse();

    List<User> findAll();

    Optional<User> findByReferralCode(String referralCode);

    List<User> findByReferredByAgent(User agent);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByCnicNumber(String cnicNumber);

    // For update (ignore current user)
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByMobileNumberAndIdNot(String mobileNumber, Long id);
    boolean existsByCnicNumberAndIdNot(String cnicNumber, Long id);

    long countByRoles_SlugIgnoreCaseAndIsDeletedFalse(String roleSlug);

    long countByRoles_SlugIgnoreCaseAndStatusAndIsDeletedFalse(String role, UserStatus status);
}


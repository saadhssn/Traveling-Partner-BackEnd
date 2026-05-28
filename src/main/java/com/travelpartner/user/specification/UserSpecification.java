package com.travelpartner.user.specification;

import com.travelpartner.basicinformation.model.BasicInformation;
import com.travelpartner.role.model.Role;
import com.travelpartner.user.dto.SaleAgentFilterRequest;
import com.travelpartner.user.dto.UserFilterRequest;
import com.travelpartner.user.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filterUsers(UserFilterRequest filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // ===== ROLE FILTER =====
            if (filter.getRole() != null) {
                Join<User, Role> rolesJoin = root.join("roles");
                predicates.add(cb.equal(rolesJoin.get("slug"), filter.getRole()));
            }

            // ===== STATUS FILTER =====
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            // ===== CITY FILTER =====
            if (filter.getCity() != null && !filter.getCity().isEmpty()) {
                Join<User, BasicInformation> basicJoin = root.join("basicInformation", JoinType.LEFT);
                predicates.add(cb.like(
                        cb.lower(basicJoin.get("city")),
                        "%" + filter.getCity().toLowerCase() + "%"
                ));
            }

            // ===== SEARCH (NAME / MOBILE / EMAIL) =====
            if (filter.getSearch() != null && !filter.getSearch().trim().isEmpty()) {

                String search = "%" + filter.getSearch().toLowerCase() + "%";

                Join<User, BasicInformation> basicJoin = root.join("basicInformation", JoinType.LEFT);

                Predicate nameMatch = cb.like(
                        cb.lower(root.get("name")), search
                );

                Predicate mobileMatch = cb.like(
                        cb.lower(root.get("mobileNumber")), search
                );

                Predicate emailMatch = cb.like(
                        cb.lower(root.get("email")), search
                );

                // Optional: basic info name
                Predicate firstNameMatch = cb.like(
                        cb.lower(basicJoin.get("firstName")), search
                );

                Predicate lastNameMatch = cb.like(
                        cb.lower(basicJoin.get("lastName")), search
                );

                predicates.add(cb.or(
                        nameMatch,
                        mobileMatch,
                        emailMatch,
                        firstNameMatch,
                        lastNameMatch
                ));
            }

            // ===== INCLUDE DELETED =====
            if (!Boolean.TRUE.equals(filter.getIncludeDeleted())) {
                predicates.add(cb.isFalse(root.get("isDeleted")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<User> filterSaleAgents(SaleAgentFilterRequest filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // ROLE = SALES_AGENT
            Join<User, Role> roleJoin = root.join("roles");
            predicates.add(cb.equal(roleJoin.get("slug"), "SALES_AGENT"));

            // STATUS
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            // SEARCH
            if (filter.getSearch() != null && !filter.getSearch().isEmpty()) {

                String search = "%" + filter.getSearch().toLowerCase() + "%";

                Predicate name = cb.like(cb.lower(root.get("name")), search);
                Predicate email = cb.like(cb.lower(root.get("email")), search);
                Predicate mobile = cb.like(cb.lower(root.get("mobileNumber")), search);

                predicates.add(cb.or(name, email, mobile));
            }

            // DELETE FILTER
            if (!Boolean.TRUE.equals(filter.getIncludeDeleted())) {
                predicates.add(cb.isFalse(root.get("isDeleted")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
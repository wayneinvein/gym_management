package com.gym.management.system.repository;

import com.gym.management.system.entity.MembershipPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
    boolean existsByName(String name);
}

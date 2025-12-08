package com.gym.management.system.repository;

import com.gym.management.system.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

    List<Members> findByTrainerTrainerId(Long trainerId);

}

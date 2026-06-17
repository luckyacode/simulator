package com.praveen.simulator.repository;

import com.praveen.simulator.entity.DlqTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DlqTopicRepository extends JpaRepository<DlqTopic,Integer> {
}

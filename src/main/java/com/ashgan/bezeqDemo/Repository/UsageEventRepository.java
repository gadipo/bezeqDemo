package com.ashgan.bezeqDemo.Repository;

import com.ashgan.bezeqDemo.Model.UsageEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsageEventRepository extends JpaRepository<UsageEvent, Long> {


}


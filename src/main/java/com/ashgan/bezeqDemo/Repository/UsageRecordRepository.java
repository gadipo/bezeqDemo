package com.ashgan.bezeqDemo.Repository;

import com.ashgan.bezeqDemo.Entity.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {

    List<UsageRecord> findByCustomerId(String customerId);

    List<UsageRecord> findByServiceType(String serviceType);

    List<UsageRecord> findByCustomerIdAndServiceType(String customerId, String serviceType);
}


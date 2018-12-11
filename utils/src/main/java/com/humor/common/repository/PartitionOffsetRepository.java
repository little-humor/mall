package com.humor.common.repository;

import com.humor.common.entity.PartitionOffset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author zhangshaoze
 * @create 2018-11-18 3:21 PM
 */
public interface PartitionOffsetRepository extends JpaRepository<PartitionOffset,Integer> {

    /**
     * 获取主题下的特定分区偏移量
     * @param topic
     * @param partition
     * @return
     */
    PartitionOffset findByTopicsAndPartitions(String topic, Integer partition);

    /**
     * 更新主题下的特定分区偏移量
     * @param topic
     * @param partition
     * @param offset
     */
    @Modifying
    @Query("update PartitionOffset set offsets = ?3 where topics = ?1 and partitions = ?2")
    void updateOffsetsByTopicsAndPartitions(String topic, Integer partition, Long offset);

}

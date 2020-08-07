package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VoteEntity, Integer> {
    List<VoteEntity> findAll();

    @Query(value = "SELECT * FROM vote AS v where v.vote_time between :startDate and :endDate", nativeQuery = true)
    List<VoteEntity> findByStartDateAndEndDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
}

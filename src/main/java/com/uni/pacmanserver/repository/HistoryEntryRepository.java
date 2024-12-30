package com.uni.pacmanserver.repository;

import java.util.Set;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uni.pacmanserver.entity.HistoryEntry;

@Repository
public interface HistoryEntryRepository extends JpaRepository<HistoryEntry, Integer> {

    Set<HistoryEntry> findAllByUserId(int userId);

    List<HistoryEntry> findAll();

}

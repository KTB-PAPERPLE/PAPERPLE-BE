package com.ktb.paperplebe.paper.repository;

import com.ktb.paperplebe.paper.entity.Paper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaperRepository extends JpaRepository<Paper, Long>, PaperRepositoryQuerydsl{
    List<Paper> findByContentContaining(String keyword , Pageable pageable);
    List<Paper> findByUserId(Long userId);
}

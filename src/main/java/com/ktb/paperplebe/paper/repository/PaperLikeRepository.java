package com.ktb.paperplebe.paper.repository;

import com.ktb.paperplebe.paper.entity.PaperLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaperLikeRepository extends JpaRepository<PaperLike, Long> {

    boolean existsByPaper_Id(Long paperId);

    // Paper 엔티티의 id를 기준으로 PaperLike 엔티티 조회
    Optional<PaperLike> findByPaper_Id(Long paperId);

    // 여러 Paper 엔티티의 id를 기준으로 PaperLike 엔티티 목록 조회
    List<PaperLike> findByPaper_IdInAndUser_Id(List<Long> paperIds, Long userId);

    Optional<PaperLike> findByPaper_IdAndUser_Id(Long paperId, Long userId);
}

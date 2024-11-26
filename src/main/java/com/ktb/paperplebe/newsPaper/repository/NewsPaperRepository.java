package com.ktb.paperplebe.newsPaper.repository;

import com.ktb.paperplebe.newsPaper.entity.NewsPaper;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsPaperRepository extends JpaRepository<NewsPaper, Long> {

  Optional<NewsPaper> findById(Long id);

  Page<NewsPaper> findAll(Pageable pageable);
}

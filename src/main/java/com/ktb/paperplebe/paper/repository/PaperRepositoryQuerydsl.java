package com.ktb.paperplebe.paper.repository;

import com.ktb.paperplebe.paper.entity.Paper;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PaperRepositoryQuerydsl {
  List<Paper> findAllOrderByLikesOrCreatedAt(Pageable pageable, String orderBy);
}

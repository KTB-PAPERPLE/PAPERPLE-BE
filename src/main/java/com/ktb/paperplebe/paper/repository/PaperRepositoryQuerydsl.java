package com.ktb.paperplebe.paper.repository;

import com.ktb.paperplebe.paper.entity.Paper;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PaperRepositoryQuerydsl {
    List<Paper> findAllOrderByLikesOrCreatedAt(Pageable pageable, String orderBy);
}

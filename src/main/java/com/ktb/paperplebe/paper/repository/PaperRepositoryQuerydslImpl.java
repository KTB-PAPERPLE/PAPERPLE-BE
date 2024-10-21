package com.ktb.paperplebe.paper.repository;

import com.ktb.paperplebe.paper.entity.Paper;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Pageable;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ktb.paperplebe.paper.entity.QPaper;
import com.ktb.paperplebe.paper.entity.QPaperLike;

@RequiredArgsConstructor
@Repository
public class PaperRepositoryQuerydslImpl implements PaperRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Paper> findAllOrderByLikesOrCreatedAt(Pageable pageable, String orderBy) {
        QPaper paper = QPaper.paper;
        QPaperLike paperLike = QPaperLike.paperLike;

        OrderSpecifier<?> orderSpecifier1, orderSpecifier2 = null;
        if ("like".equalsIgnoreCase(orderBy)) {
            orderSpecifier1 = paper.likes.size().desc(); // 좋아요 수로 내림차순
            orderSpecifier2 = paper.createdAt.desc(); // 동일할 경우 최신순으로 정렬
        } else {
            // 기본적으로 생성일자 순으로 내림차순 정렬
            orderSpecifier1 = paper.createdAt.desc();
        }

        if (orderSpecifier2 != null) {
            return queryFactory.selectFrom(paper)
                    .leftJoin(paper.likes, paperLike)
                    .groupBy(paper.id)
                    .orderBy(orderSpecifier1, orderSpecifier2) // 두 가지 정렬 조건 적용
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else {
            return queryFactory.selectFrom(paper)
                    .leftJoin(paper.likes, paperLike)
                    .groupBy(paper.id)
                    .orderBy(orderSpecifier1) // 하나의 정렬 조건만 적용
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }
    }
}

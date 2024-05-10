package kr.co.lotte.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.dto.PointsPageRequestDTO;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.custom.OrdersRepositoryCustom;
import kr.co.lotte.repository.custom.PointsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PointsRepositoryImpl implements PointsRepositoryCustom {
    private final  QPoints qPoints = QPoints.points;
    @Autowired
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Points> searchAllPointsForList(PointsPageRequestDTO requestDTO, Pageable pageable , String uid) {
        int currentMonth= 0;

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        QueryResults<Points> results =null;
        try {
            currentMonth = Integer.valueOf(requestDTO.getCurrentMonth());

            if (currentMonth <= 0) {
                currentMonth = currentMonth + 12;
                startDate = LocalDateTime.of(LocalDate.now().getYear() - 1, currentMonth, 1, 0, 0);
                endDate = LocalDateTime.of(LocalDate.now().getYear() - 1, currentMonth, 1, 23, 59, 59)
                        .with(TemporalAdjusters.lastDayOfMonth());
            } else {
                startDate = LocalDateTime.of(LocalDate.now().getYear(), currentMonth, 1, 0, 0);
                endDate = LocalDateTime.of(LocalDate.now().getYear(), currentMonth, 1, 23, 59, 59)
                        .with(TemporalAdjusters.lastDayOfMonth());
            }
        } catch (Exception e) {
            log.info("여기오나?");
            currentMonth = LocalDate.now().getMonthValue();
            startDate = LocalDateTime.of(LocalDate.now().getYear(), currentMonth, 1, 0, 0);
            endDate = LocalDateTime.of(LocalDate.now().getYear(), currentMonth, 1, 23, 59, 59)
                    .with(TemporalAdjusters.lastDayOfMonth());
        }


        LocalDateTime dateBegin = requestDTO.getDateBegin();
        LocalDateTime dateEnd = requestDTO.getDateEnd();
      if(dateBegin!=null && dateEnd!=null){
          log.info("설마여기?");
          results = jpaQueryFactory.select(qPoints)
                  .from(qPoints)
                  .where(qPoints.pointDate.between(dateBegin, dateEnd).and(qPoints.userId.eq(uid)))
                  .orderBy(qPoints.pointNo.desc())
                  .offset(pageable.getOffset())
                  .limit(pageable.getPageSize())
                  .fetchResults();
      }else if(requestDTO.getFifteen() != null && requestDTO.getFifteen() != ""){
          LocalDateTime fifteenDaysAgo = LocalDateTime.now().minus(15, ChronoUnit.DAYS);
          results = jpaQueryFactory.select(qPoints)
                  .from(qPoints)
                  .where(qPoints.pointDate.between(fifteenDaysAgo, LocalDateTime.now()).and(qPoints.userId.eq(uid)))
                  .orderBy(qPoints.pointNo.desc())
                  .offset(pageable.getOffset())
                  .limit(pageable.getPageSize())
                  .fetchResults();
      }else if(requestDTO.getOneWeek() != null && requestDTO.getOneWeek() != ""){
          LocalDateTime fifteenDaysAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
          results = jpaQueryFactory.select(qPoints)
                  .from(qPoints)
                  .where(qPoints.pointDate.between(fifteenDaysAgo, LocalDateTime.now()).and(qPoints.userId.eq(uid)))
                  .orderBy(qPoints.pointNo.desc())
                  .offset(pageable.getOffset())
                  .limit(pageable.getPageSize())
                  .fetchResults();
      }
      else{
          results = jpaQueryFactory.select(qPoints)
                  .from(qPoints)
                  .where(qPoints.pointDate.between(startDate, endDate).and(qPoints.userId.eq(uid)))
                  .orderBy(qPoints.pointNo.desc())
                  .offset(pageable.getOffset())
                  .limit(pageable.getPageSize())
                  .fetchResults();
      }
        requestDTO.setCurrentMonth(String.valueOf(currentMonth));
        List<Points> content = results.getResults();
      long total = results.getTotal();

      return new PageImpl<>(content, pageable, total);
    }
}

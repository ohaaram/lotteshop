package kr.co.lotte.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.dto.MainProductsPageRequestDTO;
import kr.co.lotte.dto.OrdersPageRequestDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.custom.OrdersRepositoryCustom;
import kr.co.lotte.repository.custom.ProductsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrdersRepositoryImpl implements OrdersRepositoryCustom {
    private  QProducts qProducts = QProducts.products;
    private  QSubProducts subProducts = QSubProducts.subProducts;
    private  QOrders orders = QOrders.orders;
    private QOrderItems orderItems = QOrderItems.orderItems;

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Orders> searchAllOrders(OrdersPageRequestDTO requestDTO, Pageable pageable, String uid) throws ParseException {
        QueryResults<Orders> results = null;
        LocalDateTime startDate;
        LocalDateTime endDate;
            //선택 날짜 조회

        if(requestDTO.getDateBegin() != null && requestDTO.getDateBegin() != ""){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateBegin =  dateFormat.parse(requestDTO.getDateBegin());
            Date dateEnd = dateFormat.parse(requestDTO.getDateEnd());
            dateEnd.setHours(23);
            dateEnd.setMinutes(59);
            dateEnd.setSeconds(59);

            results = jpaQueryFactory.select(orders)
                    .from(orders)
                    .where(orders.userId.eq(uid).and(orders.orderDate.between(dateBegin,dateEnd)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
        }
        //달 조회

       else if(requestDTO.getCurrentMonth() != null && requestDTO.getCurrentMonth() != ""){
           int  currentMonth = Integer.valueOf(requestDTO.getCurrentMonth());
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
            Date startDateAsDate = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            Date endDateAsDate = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

            results = jpaQueryFactory.select(orders)
                    .from(orders)
                    .where(orders.userId.eq(uid).and(orders.orderDate.between(startDateAsDate,endDateAsDate )))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

       }
        //일주일 조회
        else if(requestDTO.getOneWeek() != null && requestDTO.getOneWeek() != ""){
            LocalDate currentDate = LocalDate.now();
            LocalDate dateEndLocal = currentDate.minusDays(7);

// 시작일은 00:00:00, 종료일은 23:59:59로 설정합니다.
            LocalDateTime startDateTime = dateEndLocal.atStartOfDay();
            LocalDateTime endDateTime = currentDate.atTime(LocalTime.MAX);

// LocalDateTime을 Date로 변환합니다.
            Date start = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

            results = jpaQueryFactory.select(orders)
                    .from(orders)
                    .where(orders.userId.eq(uid).and(orders.orderDate.between(start, end)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

        }

        //15일 조회
       else if(requestDTO.getFifteen() != null && requestDTO.getFifteen() != ""){
            LocalDate currentDate = LocalDate.now();
            LocalDate dateEndLocal = currentDate.minusDays(15);

            LocalDateTime startDateTime = dateEndLocal.atStartOfDay();
            LocalDateTime endDateTime = currentDate.atTime(LocalTime.MAX);

            Date start = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

            results = jpaQueryFactory.select(orders)
                    .from(orders)
                    .where(orders.userId.eq(uid).and(orders.orderDate.between(start, end)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

        }else{
            int currentMonth = LocalDate.now().getMonthValue(); // 현재 달 가져오기

           startDate = LocalDateTime.of(LocalDate.now().getYear(), currentMonth, 1, 0, 0); // 현재 달의 1일
          endDate = LocalDateTime.of(LocalDate.now().getYear(), currentMonth, 1, 23, 59, 59)
                    .with(TemporalAdjusters.lastDayOfMonth()); // 현재 달의 마지막 날

            Date startDateAsDate = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            Date endDateAsDate = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

            results = jpaQueryFactory.select(orders)
                    .from(orders)
                    .where(orders.userId.eq(uid).and(orders.orderDate.between(startDateAsDate, endDateAsDate)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

        }
        List<Orders> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}

package kr.co.lotte.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotte.dto.MainProductsPageRequestDTO;
import kr.co.lotte.dto.OrdersPageRequestDTO;
import kr.co.lotte.dto.ProductsPageRequestDTO;
import kr.co.lotte.entity.*;
import kr.co.lotte.repository.ProductsRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.querydsl.core.types.ExpressionUtils.count;

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
                    .orderBy(orders.orderNo.desc())
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
                    .orderBy(orders.orderNo.desc())
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
                    .orderBy(orders.orderNo.desc())
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
                    .orderBy(orders.orderNo.desc())
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
                    .orderBy(orders.orderNo.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

        }
        List<Orders> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<OrderItems> searchAllOrdersForManager(OrdersPageRequestDTO requestDTO, Pageable pageable, String uid) throws ParseException {
        QueryResults<OrderItems> results = null;
        LocalDateTime startDate;
        LocalDateTime endDate;
        //선택 날짜 조회
        if(!requestDTO.getState().equals("모든")) {
            if (requestDTO.getDateBegin() != null && requestDTO.getDateBegin() != "") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateBegin = dateFormat.parse(requestDTO.getDateBegin());
                Date dateEnd = dateFormat.parse(requestDTO.getDateEnd());
                dateEnd.setHours(23);
                dateEnd.setMinutes(59);
                dateEnd.setSeconds(59);

                results = jpaQueryFactory.select(orderItems)
                        .from(orderItems)
                        .leftJoin(qProducts)
                        .on(qProducts.prodNo.eq(orderItems.prodNo))
                        .where(qProducts.sellerUid.eq(uid).and(orderItems.orderDate.between(dateBegin, dateEnd)).and(orderItems.orderState.eq(requestDTO.getState())))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
            } else {

                results = jpaQueryFactory.select(orderItems)
                        .from(orderItems)
                        .leftJoin(qProducts)
                        .on(qProducts.prodNo.eq(orderItems.prodNo))
                        .where(qProducts.sellerUid.eq(uid).and(orderItems.orderState.eq(requestDTO.getState())))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();

            }
        }else{
            if (requestDTO.getDateBegin() != null && requestDTO.getDateBegin() != "") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateBegin = dateFormat.parse(requestDTO.getDateBegin());
                Date dateEnd = dateFormat.parse(requestDTO.getDateEnd());
                dateEnd.setHours(23);
                dateEnd.setMinutes(59);
                dateEnd.setSeconds(59);

                results = jpaQueryFactory.select(orderItems)
                        .from(orderItems)
                        .leftJoin(qProducts)
                        .on(qProducts.prodNo.eq(orderItems.prodNo))
                        .where(qProducts.sellerUid.eq(uid).and(orderItems.orderDate.between(dateBegin, dateEnd)))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
            } else {

                results = jpaQueryFactory.select(orderItems)
                        .from(orderItems)
                        .leftJoin(qProducts)
                        .on(qProducts.prodNo.eq(orderItems.prodNo))
                        .where(qProducts.sellerUid.eq(uid))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();

            }
        }
        List<OrderItems> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<OrderItems> searchAllOrdersForAdmin(OrdersPageRequestDTO requestDTO, Pageable pageable) throws ParseException {
        QueryResults<OrderItems> results = null;
        LocalDateTime startDate;
        LocalDateTime endDate;
        //선택 날짜 조회
        if(!requestDTO.getState().equals("모든")) {
            if (requestDTO.getDateBegin() != null && requestDTO.getDateBegin() != "") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateBegin = dateFormat.parse(requestDTO.getDateBegin());
                Date dateEnd = dateFormat.parse(requestDTO.getDateEnd());
                dateEnd.setHours(23);
                dateEnd.setMinutes(59);
                dateEnd.setSeconds(59);

                results = jpaQueryFactory.select(orderItems)
                        .from(orderItems)
                        .where(orderItems.orderDate.between(dateBegin, dateEnd).and(orderItems.orderState.eq(requestDTO.getState())))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
            } else {
                results = jpaQueryFactory.select(orderItems)
                        .from(orderItems)
                        .where(orderItems.orderState.eq(requestDTO.getState()))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();

            }
        }else{

            if (requestDTO.getDateBegin() != null && requestDTO.getDateBegin() != "") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateBegin = dateFormat.parse(requestDTO.getDateBegin());
                Date dateEnd = dateFormat.parse(requestDTO.getDateEnd());
                dateEnd.setHours(23);
                dateEnd.setMinutes(59);
                dateEnd.setSeconds(59);

                results = jpaQueryFactory.select(orderItems)
                        .from(orderItems)
                        .where(orderItems.orderDate.between(dateBegin, dateEnd))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
            } else {
                results = jpaQueryFactory.select(orderItems)
                        .from(orderItems)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();

            }

        }
        List<OrderItems> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Integer> searchOrdersWeekForAdmin() {
      List<Integer> result = new ArrayList<>();
      //현재 날짜를 가져오고
        LocalDate currentDate = LocalDate.now();
        for(int i = 6 ; i >= 0; i--){
            LocalDate date = currentDate.minusDays(i);
            Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Date startDate = sqlDate;
            Date endDate = new Date(sqlDate.getTime() + TimeUnit.DAYS.toMillis(1));

            log.info(sqlDate.toString() + "먼데?");

                long count = jpaQueryFactory.select(orderItems.count())
                        .from(orderItems)
                        .where(orderItems.orderDate.between(startDate, endDate))
                        .fetchOne();
                result.add((int) count);

      }
      return result;
    }

    @Override
    public List<Integer> searchOrdersMonthForAdmin() {
        List<Integer> result = new ArrayList<>();
        //현재 날짜를 가져오고
        LocalDate currentDate = LocalDate.now();

        // 한 달 전의 날짜를 계산
        LocalDate oneMonthAgo = currentDate.minusMonths(1);

        for (LocalDate date = oneMonthAgo; date.compareTo(currentDate) <= 0; date = date.plusDays(1)){
            log.info(date.toString() + "이거!");
            Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Date startDate = sqlDate;
            Date endDate = new Date(sqlDate.getTime() + TimeUnit.DAYS.toMillis(1));

            log.info(sqlDate.toString() + "먼데?");

            long count = jpaQueryFactory.select(orderItems.count())
                    .from(orderItems)
                    .where(orderItems.orderDate.between(startDate, endDate))
                    .fetchOne();
            result.add((int) count);
        }

        return result;
    }

    @Override
    public List<Integer> searchOrdersYearForAdmin() {
        List<Integer> result = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();

        LocalDate oneYearAgo = currentDate.minusYears(1);

        for (LocalDate date = oneYearAgo; !date.isAfter(currentDate); date = date.plusMonths(1)) {
            LocalDate startOfMonth = date.withDayOfMonth(1);
            LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

            Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusNanos(1).toInstant());

            long count = jpaQueryFactory.select(orderItems.count())
                    .from(orderItems)
                    .where(orderItems.orderDate.between(startDate, endDate))
                    .fetchOne();
            result.add((int) count);
        }

        return result;
    }

    @Override
    public List<Integer> searchPriceWeekForAdmin() {
        List<Integer> result = new ArrayList<>();
        //현재 날짜를 가져오고
        LocalDate currentDate = LocalDate.now();
        for(int i = 6 ; i >= 0; i--){
            LocalDate date = currentDate.minusDays(i);
            Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Date startDate = sqlDate;
            Date endDate = new Date(sqlDate.getTime() + TimeUnit.DAYS.toMillis(1));

            log.info(sqlDate.toString() + "먼데?");
            try {
                long count = jpaQueryFactory.select(orders.orderTotalPrice.sum())
                        .from(orders)
                        .where(orders.orderDate.between(startDate, endDate))
                        .fetchOne();
                result.add((int) count);
            }catch (Exception e){
                log.info(e.getMessage());
                result.add(0);
            }
        }
        return result;
    }

    @Override
    public List<Integer> searchPriceMonthForAdmin() {
        List<Integer> result = new ArrayList<>();
        //현재 날짜를 가져오고
        LocalDate currentDate = LocalDate.now();

        // 한 달 전의 날짜를 계산
        LocalDate oneMonthAgo = currentDate.minusMonths(1);

        for (LocalDate date = oneMonthAgo; date.compareTo(currentDate) <= 0; date = date.plusDays(1)){
            log.info(date.toString() + "이거!");
            Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Date startDate = sqlDate;
            Date endDate = new Date(sqlDate.getTime() + TimeUnit.DAYS.toMillis(1));

            log.info(sqlDate.toString() + "먼데?");

            try {
                long count = jpaQueryFactory.select(orders.orderTotalPrice.sum())
                        .from(orders)
                        .where(orders.orderDate.between(startDate, endDate))
                        .fetchOne();
                result.add((int) count);
            }catch (Exception e){
                log.info(e.getMessage());
                result.add(0);
            }
        }

        return result;
    }

    @Override
    public List<Integer> searchPriceYearForAdmin() {
        List<Integer> result = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();

        LocalDate oneYearAgo = currentDate.minusYears(1);

        for (LocalDate date = oneYearAgo; !date.isAfter(currentDate); date = date.plusMonths(1)) {
            LocalDate startOfMonth = date.withDayOfMonth(1);
            LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

            Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusNanos(1).toInstant());

            try {
                long count = jpaQueryFactory.select(orders.orderTotalPrice.sum())
                        .from(orders)
                        .where(orders.orderDate.between(startDate, endDate))
                        .fetchOne();
                result.add((int) count);
            }catch (Exception e){
                log.info(e.getMessage());
                result.add(0);
            }
        }

        return result;
    }

    @Override
    public List<Integer> searchOrdersWeekForManager(String uid) {
        List<Integer> result = new ArrayList<>();
        //현재 날짜를 가져오고
        LocalDate currentDate = LocalDate.now();
        for(int i = 6 ; i >= 0; i--){
            LocalDate date = currentDate.minusDays(i);
            Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Date startDate = sqlDate;
            Date endDate = new Date(sqlDate.getTime() + TimeUnit.DAYS.toMillis(1));


            long count = jpaQueryFactory.select(orderItems.count())
                    .from(orderItems)
                    .join(subProducts)
                    .on(subProducts.subProdNo.eq(orderItems.prodNo))
                    .join(qProducts)
                    .on(qProducts.prodNo.eq(subProducts.prodNo))
                    .where(orderItems.orderDate.between(startDate, endDate).and(qProducts.sellerUid.eq(uid)))
                    .fetchOne();
            result.add((int) count);

        }
        return result;
    }

    @Override
    public List<Integer> searchOrdersMonthForManager(String uid) {
        List<Integer> result = new ArrayList<>();
        //현재 날짜를 가져오고
        LocalDate currentDate = LocalDate.now();

        // 한 달 전의 날짜를 계산
        LocalDate oneMonthAgo = currentDate.minusMonths(1);

        for (LocalDate date = oneMonthAgo; date.compareTo(currentDate) <= 0; date = date.plusDays(1)){
            log.info(date.toString() + "이거!");
            Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Date startDate = sqlDate;
            Date endDate = new Date(sqlDate.getTime() + TimeUnit.DAYS.toMillis(1));

            log.info(sqlDate.toString() + "먼데?");

            long count = jpaQueryFactory.select(orderItems.count())
                    .from(orderItems)
                    .join(subProducts)
                    .on(subProducts.subProdNo.eq(orderItems.prodNo))
                    .join(qProducts)
                    .on(qProducts.prodNo.eq(subProducts.prodNo))
                    .where(orderItems.orderDate.between(startDate, endDate).and(qProducts.sellerUid.eq(uid)))
                    .fetchOne();
            result.add((int) count);
        }

        return result;
    }

    @Override
    public List<Integer> searchOrdersYearForManager(String uid) {
        List<Integer> result = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();

        LocalDate oneYearAgo = currentDate.minusYears(1);

        for (LocalDate date = oneYearAgo; !date.isAfter(currentDate); date = date.plusMonths(1)) {
            LocalDate startOfMonth = date.withDayOfMonth(1);
            LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

            Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusNanos(1).toInstant());

            long count = jpaQueryFactory.select(orderItems.count())
                    .from(orderItems)
                    .join(subProducts)
                    .on(subProducts.subProdNo.eq(orderItems.prodNo))
                    .join(qProducts)
                    .on(qProducts.prodNo.eq(subProducts.prodNo))
                    .where(orderItems.orderDate.between(startDate, endDate).and(qProducts.sellerUid.eq(uid)))
                    .fetchOne();
            result.add((int) count);
        }

        return result;
    }

    @Override
    public List<Integer> searchPriceWeekForManager(String uid) {
        List<Integer> result = new ArrayList<>();
        //현재 날짜를 가져오고
        LocalDate currentDate = LocalDate.now();
        for(int i = 6 ; i >= 0; i--){
            LocalDate date = currentDate.minusDays(i);
            Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Date startDate = sqlDate;
            Date endDate = new Date(sqlDate.getTime() + TimeUnit.DAYS.toMillis(1));

            log.info(sqlDate.toString() + "먼데?");
            try {
              List<OrderItems> orderItems1 = jpaQueryFactory.select(orderItems)
                        .from(orderItems)
                        .join(subProducts)
                        .on(subProducts.subProdNo.eq(orderItems.prodNo))
                        .join(qProducts)
                        .on(qProducts.prodNo.eq(subProducts.prodNo))
                        .where(orderItems.orderDate.between(startDate, endDate).and(qProducts.sellerUid.eq(uid)))
                        .fetch();
              int count = 0;
              for(OrderItems order : orderItems1){
                    count += (int) (order.getItemCount() * order.getItemPrice() * (100 - order.getItemDiscount()) *0.01);
              }

                result.add((int) count);
            }catch (Exception e){
                log.info(e.getMessage());
                result.add(0);
            }
        }
        return result;
    }

    @Override
    public List<Integer> searchPriceMonthForManager(String uid) {
        List<Integer> result = new ArrayList<>();
        //현재 날짜를 가져오고
        LocalDate currentDate = LocalDate.now();

        // 한 달 전의 날짜를 계산
        LocalDate oneMonthAgo = currentDate.minusMonths(1);

        for (LocalDate date = oneMonthAgo; date.compareTo(currentDate) <= 0; date = date.plusDays(1)){
            log.info(date.toString() + "이거!");
            Date utilDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            Date startDate = sqlDate;
            Date endDate = new Date(sqlDate.getTime() + TimeUnit.DAYS.toMillis(1));

            log.info(sqlDate.toString() + "먼데?");

            try {
                List<OrderItems> orderItems1 = jpaQueryFactory.select(orderItems)
                        .from(orderItems)
                        .join(subProducts)
                        .on(subProducts.subProdNo.eq(orderItems.prodNo))
                        .join(qProducts)
                        .on(qProducts.prodNo.eq(subProducts.prodNo))
                        .where(orderItems.orderDate.between(startDate, endDate).and(qProducts.sellerUid.eq(uid)))
                        .fetch();
                int count = 0;
                for(OrderItems order : orderItems1){
                    count += (int) (order.getItemCount() * order.getItemPrice() * (100 - order.getItemDiscount()) *0.01);
                }
                result.add((int) count);
            }catch (Exception e){
                log.info(e.getMessage());
                result.add(0);
            }
        }

        return result;
    }

    @Override
    public List<Integer> searchPriceYearForManager(String uid) {
        List<Integer> result = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();

        LocalDate oneYearAgo = currentDate.minusYears(1);

        for (LocalDate date = oneYearAgo; !date.isAfter(currentDate); date = date.plusMonths(1)) {
            LocalDate startOfMonth = date.withDayOfMonth(1);
            LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());

            Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).plusDays(1).minusNanos(1).toInstant());

            try {
                List<OrderItems> orderItems1 = jpaQueryFactory.select(orderItems)
                        .from(orderItems)
                        .join(subProducts)
                        .on(subProducts.subProdNo.eq(orderItems.prodNo))
                        .join(qProducts)
                        .on(qProducts.prodNo.eq(subProducts.prodNo))
                        .where(orderItems.orderDate.between(startDate, endDate).and(qProducts.sellerUid.eq(uid)))
                        .fetch();
                int count = 0;
                for(OrderItems order : orderItems1){
                    count += (int) (order.getItemCount() * order.getItemPrice() * (100 - order.getItemDiscount()) *0.01);
                }
                result.add((int) count);
            }catch (Exception e){
                log.info(e.getMessage());
                result.add(0);
            }
        }

        return result;
    }
    }



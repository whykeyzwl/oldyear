package com.wishes.yearOld.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.wishes.yearOld.model.ActivitySupporter;
import com.wishes.yearOld.model.Order;
import com.wishes.yearOld.model.OrderDetail;
import com.wishes.yearOld.model.OrderListVO;

import java.util.List;

@Component("orderMapper")
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    int updateForStatus(Order record);

    List<Order> queryOrders(Order record);

    Integer queryMaxSaleLevel(Order record);

    List<OrderListVO> queryOrderList(Order record);
    
    List<OrderListVO> queryOrderQdList(Order record);
    
    Integer queryOrderQdListSize(Order record);


    List<OrderDetail> queryOrderDetail(Order record);

    OrderDetail loadOrderDetail(Integer id);

    List<ActivitySupporter> querySupporterTotalRank(@Param("curUserId") Integer curUserId, @Param("curUserGroup") Integer curUserGroup,  @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    public Integer selectHaveBuy(@Param("userId") Integer userId);

}
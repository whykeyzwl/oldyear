package com.wishes.yearOld.service;

import com.wishes.yearOld.Exception.PaymentAliRefundException;
import com.wishes.yearOld.model.GoodsVO;
import com.wishes.yearOld.model.Order;
import com.wishes.yearOld.model.OrderDetail;
import com.wishes.yearOld.model.OrderListVO;

import java.util.List;
import java.util.Map;

/**
 * Created by tmg-yesky on 2016/10/21.
 */
public interface OrderService {

    int saveOrder(Order record);

    int updateForStatus(Order record);

    List<Order> queryOrders(Order record);

    int updateOrderAndSaveRecord(Order record);

    List<OrderListVO> queryOrderList(Order record);
    
    List<OrderListVO> queryOrderQdList(Order record);

    List<OrderDetail> queryOrderDetail(Order record);

    OrderDetail loadOrderDetail(Integer id);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    /**
     * 单笔交易全部退款申请，正常返回申请信息成功，抛出异常退款申请不成功
     * @param order
     * @return
     * @throws PaymentAliRefundException
     */
    String alipay_refund(Order order) throws PaymentAliRefundException;

    public Integer selectHaveBuy(Integer userId);

	Map<String, Object> validateAndReturnPrice(GoodsVO good);
	
	public Integer queryOrderQdListSize(Order record);
}

package com.wishes.yearOld.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.wishes.yearOld.Exception.PaymentAliRefundException;
import com.wishes.yearOld.apppay.AppPayConfig;
import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.StringUtils;
import com.wishes.yearOld.dao.OrderMapper;
import com.wishes.yearOld.dao.PhotoAlbumDao;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tmg-yesky on 2016/10/21.
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    SalesRecordService salesRecordService;

    @Autowired
    ActivityService activityService;

    @Autowired
    PhotoAlbumService photoAlbumService;

    @Autowired
    IUserService userService;

    @Autowired
    private PhotoAlbumDao photoAlbumDao;


    @Override
    public int saveOrder(Order record) {
        return orderMapper.insertSelective(record);
    }

    @Override
    public int updateForStatus(Order status) {
        return orderMapper.updateForStatus(status);
    }

    @Override
    public List<Order> queryOrders(Order record) {
        return orderMapper.queryOrders(record);
    }

    @Override
    public int updateOrderAndSaveRecord(Order record) {
        if(record.getId() == null || record.getId() <= 0){
            return 0;
        }
        Order updateOrder = new Order(record);
        //更新订单状态
        int result = orderMapper.updateByPrimaryKeySelective(updateOrder);
        //分配资源
        if(record.getOrderType() == Constant.GOODS_TYPE_ALBUM){//图集
            PhotoAlbum photoAlbum = photoAlbumService.loadPhotoAlbum(record.getAlbumId());
            SalesRecord salesRecord = new SalesRecord(record,photoAlbum);
            result = salesRecordService.saveSalesRecord(salesRecord);
        }else if(record.getOrderType() == Constant.GOODS_TYPE_ACTIVITY){//活动
            User user = userService.detail(record.getBuyerId(),null);
            ActivitySupporter activitySupporter = new ActivitySupporter(record,user);
            result = activityService.updateAsSupportSuccess(activitySupporter);
        }else{
            return 0;
        }
        return result;
    }

    @Override
    public List<OrderListVO> queryOrderList(Order record) {
        List<OrderListVO> orderListVOList = orderMapper.queryOrderList(record);
        if (orderListVOList != null && orderListVOList.size() > 0) {
            for (OrderListVO listVO : orderListVOList) {
                if (listVO.getPhotoAlbumID() != null &&
                        listVO.getPhotoAlbumID().intValue() > 0 &&
                        StringUtils.isEmpty(listVO.getPhotoAlbumModelCover())) {
                    Photo photo = photoAlbumDao.photoinfo(listVO.getPhotoAlbumID(),0);
                    if(photo!=null)
                        listVO.setPhotoAlbumModelCover(photo.getFullPath());

                }
            }
        }
        return orderListVOList;
    }

    @Override
    public List<OrderDetail> queryOrderDetail(Order record) {
        return orderMapper.queryOrderDetail(record);
    }

    @Override
    public OrderDetail loadOrderDetail(Integer id) {
        return orderMapper.loadOrderDetail(id);
    }

    @Override
    public Order selectByPrimaryKey(Integer id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Order record) {
        return orderMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public String alipay_refund(Order order) throws PaymentAliRefundException {
//        if (orderId == null && orderId.intValue() <= 0) {//订单id不正确
//            throw new PaymentAliRefundException(PaymentAliRefundCode.NO_ORDERID);
//        }
        //Order order = selectByPrimaryKey(orderId);
        if (order == null) {//订单错误
            throw new PaymentAliRefundException(PaymentAliRefundCode.NO_ORDER);
        }
        AlipayClient alipayClient = new DefaultAlipayClient(
                AppPayConfig.ALIPAY_APP_REFUND_METHOD,
                AppPayConfig.ALIPAY_APP_ID,
                AppPayConfig.ALIPAY_APP_PRIVATE_KEY,
                AppPayConfig.ALIPAY_APP_FORMAT,
                AppPayConfig.ALIPAY_APP_CHASET,
                AppPayConfig.ALIPAY_APP_ALI_PUBLIC_KEY);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+order.getOrderNumber()+"\"," +
                "    \"trade_no\":\""+order.getTradeNo()+"\"," +
                "    \"refund_amount\":"+order.getTotalAmount()+"," +
                "    \"refund_reason\":\"正常退款\"" +
                "  }");
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return "正常退款_订单号"+response.getOutTradeNo()+"_退款金额"+response.getRefundFee()+"_实际退回金额"+response.getSendBackFee();
            } else {
                int num = 1;
                //支付宝服务器可能繁忙需要再次请求，请求3次后依然不成功则抛出异常
                while (num < 3 && response.getSubCode().equals(PaymentAliRefundCode.SYSTEM_ERROR.getCode())) {
                    response = alipayClient.execute(request);
                    if (response.isSuccess()) {
                        return "正常退款_订单号"+response.getOutTradeNo()+"_退款金额"+response.getRefundFee()+"_实际退回金额"+response.getSendBackFee();
                    }
                    num ++;
                }
                PaymentAliRefundCode code = PaymentAliRefundCode.getPaymentAliRefundCode(response.getSubCode());
                throw new PaymentAliRefundException(code);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new PaymentAliRefundException(PaymentAliRefundCode.REQ_EXCEPTION);
        }
    }

    public Integer selectHaveBuy(Integer userId){
        return orderMapper.selectHaveBuy(userId);
    }
    
    /**
     *  校验商品是否存在并返回价钱，返回0则不存在该商品
     *  @author zwl
     *  @date 2017/5/18
     * @param goodsVO
     * @return
     */
    @Override
    public Map<String,Object> validateAndReturnPrice(GoodsVO goodsVO) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "";
        BigDecimal price = new BigDecimal(0.00);
        try {
            //购买图集
            if (goodsVO.getGoodsType()!=null&&goodsVO.getGoodsType().byteValue() == Constant.GOODS_TYPE_ALBUM) {
                if (goodsVO.getAlbumId() != null && goodsVO.getAlbumId().intValue() > 0 && goodsVO.getAlbumLevel() > 0) {
                    PhotoAlbum photoAlbum = photoAlbumService.loadPhotoAlbum(goodsVO.getAlbumId());
                    if (photoAlbum == null || photoAlbum.getStatus() != Constant.ALBUM_SALE) {
                        msg = "该图集已不再销售！";
                    } else {
                        msg = "";
                        switch (goodsVO.getAlbumLevel()) {
                            case 1:
                                price = photoAlbum.getPrice3();
                                break;
                            case 2:
                                price = photoAlbum.getDownprice();
                                break;
                            case 3:
                                price = photoAlbum.getViewprice();
                                break;
                            default:
                                msg = "购买图集等级错误！";
                                break;
                        }
                    }
                } else {
                    msg = "订单错误！";
                }
            } else if (goodsVO.getGoodsType()!=null&&goodsVO.getGoodsType().byteValue() == Constant.GOODS_TYPE_ACTIVITY) {
                if (goodsVO.getActivityId() != null && goodsVO.getActivityId() > 0 && goodsVO.getActivityRule() != null &&
                        goodsVO.getActivityRule() > 0 && goodsVO.getActivityModel() != null && goodsVO.getActivityModel() > 0) {
                    Activity activity = activityService.loadActivity(goodsVO.getActivityId());
                    ActivityRule activityRule = activityService.loadActivityRule(goodsVO.getActivityRule());
                    ActivityModel activityModel = null;
                    if (activity != null) {
                        ActivityModel modelParams = new ActivityModel();
                        modelParams.setActivityId(activity.getId());
                        modelParams.setModelId(goodsVO.getActivityModel());
                        modelParams.setPageNo(1);
                        modelParams.setPageSize(1);
                        List<ActivityModel> models = activityService.queryModels(modelParams);
                        if (models != null && models.size() > 0) {
                            activityModel = models.get(0);
                        }
                    }
                    int supportCount = activityService.countSupporterTimes(activity.getId(),goodsVO.getActivityRule(),goodsVO.getActivityModel());
                    if (activity != null && activityRule != null && activityModel != null) {
                        if (activityRule.isRandomFeeSupport()) {
                            if (goodsVO.getActivityFreeSupportMoney()== null || goodsVO.getActivityFreeSupportMoney().compareTo(BigDecimal.ZERO) == 0) {
                                msg = "无偿支持请输入支持金额";
                            } else {
                                price = goodsVO.getActivityFreeSupportMoney().setScale(2,BigDecimal.ROUND_HALF_UP);
                            }

                        } else {
                            if (activityRule.getSuporterLimit()!=0 && supportCount >= activityRule.getSuporterLimit()) {
                                msg = "该规则已超出支持限制";
                            } else {
                                price = activityRule.getMoney();
                            }
                        }
                    } else {
                        msg = "订单错误";
                    }
                } else {
                    msg = "订单错误！";
                }

            } else {
                msg = "订单错误，没有该商品！";
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "订单错误！";
        }
        map.put("msg", msg);
        map.put("price", price);
        return  map;
    }
    /**
     * 包含青豆和订单支付的结果
     */
	@Override
	public List<OrderListVO> queryOrderQdList(Order record) {
		  List<OrderListVO> orderListVOList = orderMapper.queryOrderQdList(record);
	        if (orderListVOList != null && orderListVOList.size() > 0) {
	            for (OrderListVO listVO : orderListVOList) {
	                if (listVO.getPhotoAlbumID() != null &&
	                        listVO.getPhotoAlbumID().intValue() > 0 &&
	                        StringUtils.isEmpty(listVO.getPhotoAlbumModelCover())) {
	                    Photo photo = photoAlbumDao.photoinfo(listVO.getPhotoAlbumID(),0);
	                    if(photo!=null)
	                        listVO.setPhotoAlbumModelCover(photo.getFullPath());

	                }
	            }
	        }
	        return orderListVOList;
	}
	  /**
     * 包含青豆和订单支付的结条数
     */
	@Override
	public Integer queryOrderQdListSize(Order record) {
		Integer orderListVOListSize = orderMapper.queryOrderQdListSize(record);
	        
	        return orderListVOListSize;
	}
}

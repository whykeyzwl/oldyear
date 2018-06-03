package com.wishes.yearOld.dao;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wishes.yearOld.model.TgodAlbumcountinfo;


@Component("tgodAlbumcountinfoMapper")
public interface TgodAlbumcountinfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TgodAlbumcountinfo record);

    int insertSelective(TgodAlbumcountinfo record);

    TgodAlbumcountinfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TgodAlbumcountinfo record);

    int updateByPrimaryKey(TgodAlbumcountinfo record);
    /**
     * 专辑销量列表
     * @date 2017-6-15 下午8：33
     * @param TgodAlbumcountinfo
     * @return
     */
    LinkedList<TgodAlbumcountinfo> queryAlbumStatistics(TgodAlbumcountinfo tgodPhotoalbum);
    
    /**
     * 单个专辑销量列表
     * @date 2017-6-15 下午8：33
     * @param TgodAlbumcountinfo
     * @return
     */
    LinkedList<TgodAlbumcountinfo> queryOneAlbumSalesTotal(TgodAlbumcountinfo tgodPhotoalbum);
    
    /**
     * 传入对象查询总条数
     * @date 2017-6-15 下午9：33
     * @param tgodPhotoalbum
     * @return
     */
    Integer queryAlbumInfoCount(TgodAlbumcountinfo tgodPhotoalbum);
    
	/**
	    * 动态发布列表查询
	    *@author zwl
	    * @date 2017-6-16下午6：05
	    */
	 List<TgodAlbumcountinfo> queryDynamicAlbum(TgodAlbumcountinfo tgodPhotoalbum);
	     
	 	/**
	      * 动态销量列表查询
	      *@author zwl
	      * @date 2017-6-16下午6：05
	      */
	 List<TgodAlbumcountinfo> queryDynamicSalesAlbum(TgodAlbumcountinfo countinfo);
	 /**
	     * 动态发布列表总条数
	     * @date 2017-6-20 下午4：13
	     * @param TgodAlbumcountinfo
	     * @return
	     */
	 Integer queryDynamicAlbumCount(TgodAlbumcountinfo countinfo);
	    
		/**
		    * 动态销量列表总条数
		    *@author zwl
		    * @date2017-6-20 下午4：13
		    */
	 Integer queryDynamicSalesAlbumCount(TgodAlbumcountinfo countinfo);
		     
		/**
		      * 红包动态记录
		      *@author zwl
		      * @date 2017-6-20 下午4：19
		 */
	   List<TgodAlbumcountinfo> queryRedAlbumRecord(TgodAlbumcountinfo countinfo);
		/**
	      * 红包动态记录总条数
	      *@author zwl
	      * @date 2017-6-20 下午4：43
	  */
	   Integer queryRedAlbumRecordCount(TgodAlbumcountinfo countinfo);
		/**
	      * 图文动态记录
	      *@author zwl
	      * @date 2017-6-20 下午4：49
	      */
       List<TgodAlbumcountinfo> queryDynamicAlbumRecord(TgodAlbumcountinfo countinfo);
   	/**
	      * 图文动态记录条数
	      *@author zwl
	      * @date 2017-6-20 下午4：19
	 */
       Integer queryDynamicAlbumRecordCount(TgodAlbumcountinfo countinfo);
       
       /**
	      *财务列表
	      *@author zwl
	      * @date 2017-7-21 上午11:08
	      */
     List<TgodAlbumcountinfo> queryFinancialRecord(TgodAlbumcountinfo countinfo);
 	/**
	      * 财务列表总条数
	      *@author zwl
	      * @date 2017-7-21 上午11:08
	 */
     Integer queryFinancialRecordCount(TgodAlbumcountinfo countinfo);
	 
	 
}
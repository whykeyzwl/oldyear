package com.wishes.yearOld.service.impl;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.*;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import com.tmg.utils.StringUtils;
import com.wishes.yearOld.common.Constant;
import com.wishes.yearOld.common.DateUtil;
import com.wishes.yearOld.dao.*;
import com.wishes.yearOld.model.*;
import com.wishes.yearOld.model.Tag;
import com.wishes.yearOld.service.PhotoAlbumService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.*;

import javax.annotation.Resource;

/**
 * Created by tmg-yesky on 2016/9/19.
 */
@Service("photoAlbumService")
public class PhotoAlbumServiceImpl implements PhotoAlbumService {

    @Autowired
    private PhotoAlbumDao photoAlbumDao;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RecordDownLimitMapper recordDownLimitMapper;

    @Autowired
    private RecordQingdouMapper recordQingdouMapper;

    @Autowired
    private IUserMapper userMapper;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private UserAlbumLogMapper userAlbumLogMapper;
    
    @Autowired
    private TgodAlbumcountinfoMapper tgodAlbumcountinfoMapper;//保存图集统计表数据


    @Override
    public List<PhotoAlbum> search(String keyword, Integer modelId, Integer sortType, Integer pageNo, Integer pageSize, User user) {
        if (pageNo == null || pageNo <= 0) {
            pageNo = Constant.DEF_PAGE_NUM;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = Constant.DEF_PAGE_SIZE;
        }
        Integer start = (pageNo - 1) * pageSize;
        List<PhotoAlbum> list = photoAlbumDao.search(keyword, modelId, sortType, start, pageSize);
        Date today = DateUtil.getCurrentDateBegin();
        Date yesterday = DateUtil.getYesterDayBegin();
        if (list != null && list.size() > 0) {
            for (PhotoAlbum album : list) {
                //如果封面url为空，则选第一张图为封面
                if (StringUtils.isEmpty(album.getCoverUrl())) {
                    Photo photo = photoAlbumDao.photoinfo(album.getId(), 0);
                    if (photo != null)
                        album.setCover(photo.getFullPath());
                }
                if (user != null && (user.getGroupId() == Constant.USER_GROUP_MODEL || user.getGroupId() == Constant.USER_GROUP_PHOTOGRAPHER)) {
                    //今日收入
                    BigDecimal todayIncome = saleMapper.getAlbumIncome(user.getId(), album.getId(), today, null);
                    //昨日收入
                    BigDecimal yesterdayIncome = saleMapper.getAlbumIncome(user.getId(), album.getId(), yesterday, today);
                    //总收入
                    BigDecimal amountIncome = saleMapper.getAlbumIncome(user.getId(), album.getId(), null, null);
                    Integer count = saleMapper.getBuyAlbumCount(album.getId());

                    AlbumIncome albumIncome = new AlbumIncome();
                    albumIncome.setTodayIncome(todayIncome);
                    albumIncome.setYesterdayIncome(yesterdayIncome);
                    albumIncome.setAmountIncome(amountIncome);
                    albumIncome.setBuynum(count);
                }
            }
        }
        return list;
    }

    @Override
    public List<PhotoAlbum> list(PhotoAlbum photoAlbum) {
        List<PhotoAlbum> list = photoAlbumDao.queryPhotoAlbums(photoAlbum);
        if (list != null && list.size() > 0) {
            for (PhotoAlbum album : list) {
                //如果封面url为空，则选第一张图为封面
                if (StringUtils.isEmpty(album.getCoverUrl())) {
                    Photo photo = photoAlbumDao.photoinfo(album.getId(), 0);
                    if (photo != null)
                        album.setCover(photo.getFullPath());
                }
            }

        }
        return list;
    }

    @Override
    public LinkedList<PhotoAlbum> getHomeAlbums(PhotoAlbum photoAlbum) {
        LinkedList<PhotoAlbum> albums = photoAlbumDao.getHomeAlbums(photoAlbum);
        for(PhotoAlbum album:albums){
            if(StringUtils.isTrimEmpty(album.getCover())){
                List<Photo> photos = photoAlbumDao.photos(album.getId());
                if(photos != null && photos.size() > 0) {
                    album.setCover(photos.get(0).getFullPath());
                }
            }
        }
        return albums;
    }

    /* 随机取一条推荐数据 */
    public PhotoAlbum getHomeAlbumsRand(PhotoAlbum album){
        PhotoAlbum _photo = photoAlbumDao.getHomeAlbumsRand(album);
        //Map<String,Object> coverMap = photoAlbumDao.queryCoverByAlbumId(_photo.getId());
        //_photo.setCover(coverMap.get("cover").toString());
        if(StringUtils.isTrimEmpty(_photo.getCover())){
            List<Photo> photos = photoAlbumDao.photos(_photo.getId());
            if(photos != null && photos.size() > 0) {
                _photo.setCover(photos.get(0).getFullPath());
            }
        }
        return _photo;
    }


    @Override
    public PhotoAlbum findById(Integer id) {
        return photoAlbumDao.detail(id);
    }

    @Override
    public PhotoAlbum detail(Integer id, User viewUser, boolean updateViewCount) {
        List<Tag> tags = photoAlbumDao.tags(id);
        List<Photo> photos = photoAlbumDao.photos(id);
        PhotoAlbum p = photoAlbumDao.detail(id);
        if (p != null) {
            //如果封面url为空，则选第一张图为封面
            if (StringUtils.isEmpty(p.getCoverUrl())) {
                Photo photo = photoAlbumDao.photoinfo(p.getId(), 0);
                if (photo != null)
                    p.setCover(photo.getFullPath());
            }
            p.setTags(tags);
            p.setPhotos(photos);
            List<Photo> lv1Photos = new ArrayList<Photo>();
            if (StringUtils.isNotEmpty(p.getUnLockLV1())) {
                for (int i = 0; i < 4; i++)
                    if (i < photos.size())
                        lv1Photos.add(photos.get(i));
                String[] str = StringUtils.split(p.getUnLockLV1(), ',');
                for (int i = 0; i < str.length; i++) {
                    try {
                        Integer page = Integer.parseInt(str[i]);
                        if (page > 0 && page <= photos.size())
                            lv1Photos.add(photos.get(page - 1));
                    } catch (Exception e) {

                    }
                }
                if (photos.size() > 4)
                    lv1Photos.add(photos.get(4));
                else
                    lv1Photos.add(photos.get(photos.size() - 1));
            } else {
                for (int i = 0; i < 15; i++)
                    if (i < photos.size())
                        lv1Photos.add(photos.get(i));
            }
            p.setLevelOnePhotos(lv1Photos);

            Integer curUserId = viewUser == null ? null : viewUser.getId();
            p.setPermission(calUserAlbumPermission(p, curUserId));
            if (updateViewCount)
                updateViewCount(p, viewUser);
        }
        return p;
    }

    @Override
    public AlbumPermission calUserAlbumPermission(PhotoAlbum p, Integer userId) {
        if (p == null) return null;

        AlbumPermission permission = new AlbumPermission();
        permission.setAlbumId(p.getId());
        //判断权限
        if (userId == null) {
            //未登录
            permission.setPermitType(1);
            permission.setPermitDesp("立即购买");
            permission.setUserLevel(0);
            return permission;

        }

        permission.setUserId(userId);
        if (p.getModelId() == userId || p.getPhotoGrapherId() == userId) {
            // 我的作品
            permission.setPermitType(2);
            permission.setPermitDesp("我的作品");
            permission.setUserLevel(3);
            return permission;
        }

        if (p.getLimitFree() == 1) {
            // 限免作品
            permission.setPermitType(5);
            permission.setPermitDesp("限免作品");
            permission.setUserLevel(2);
            return permission;
        }

        int level = getUserSaleLevel(userId, p.getId());
        /*if (p.getActivityId() != null && level == 0) {
            // 未购买过的活动作品
            RecordDownLimit record = new RecordDownLimit();
            record.setAlbumId(p.getId());
            record.setActivityId(p.getActivityId());
            record.setUserId(userId);
            record.setModelId(p.getModelId());
            record.setStatus(Constant.DOWN_LIMIT_USED);
            Integer count = recordDownLimitMapper.checkAlbumPermission(record);
            if (count > 0) {//如果已兑换
                permission.setPermitType(5);
                permission.setPermitDesp("预售作品");
                permission.setUserLevel(2);
                return permission;
            }
            record.setStatus(Constant.DOWN_LIMIT_UNUSED);
            count = recordDownLimitMapper.getDownLimitCount(record);
            if (count > 0) {//如果有权限且未兑换
                permission.setPermitType(3);
                permission.setPermitDesp("预售领取");
                permission.setUserLevel(0);
                permission.setUserDownlimit(count);
                permission.setUserTotalDownlimit(recordDownLimitMapper.getTotalDownLimitCount(record));
                return permission;
            }
        }
*/
        //普通作品
        permission.setUserLevel(level);
        if (level >= 2) {
            //已全部购买
            permission.setPermitType(5);
            permission.setPermitDesp("已购买");
        } else if (level == 1) {
            //购买过1级或用青豆兑换过1级
            permission.setPermitType(4);
            permission.setPermitDesp("补差价");
            permission.setBuyPrice1(p.getDownprice()); //二级解锁价格
        } else {
            permission.setPermitType(1);
            permission.setPermitDesp("立即购买");
            permission.setBuyPrice1(p.getViewprice()); //一级解锁价格
            permission.setBuyPrice2(p.getViewprice()); //全部套图价格
            if (p.getQingdou() > 0) {
                permission.setQingdouPrice(p.getQingdou()); //青豆价格
            }
        }

        return permission;
    }

    @Override
    public Photo photoinfo(Integer albumId, Integer page) {
        return photoAlbumDao.photoinfo(albumId, page);
    }

    @Override
    public Object buy(Integer albumId, Integer salesType) {
        //todo
        return null;
    }

    @Override
    public List<List<AlbumViewLog>> lastestView(Integer userId, Integer pageNo, Integer pageSize) {
        if (pageNo == null)
            pageNo = Constant.DEF_PAGE_NUM;
        if (pageSize == null || pageSize <= 0)
            pageSize = Constant.DEF_PAGE_SIZE;
        int start = (pageNo - 1) * pageSize;
        List<AlbumViewLog> list = photoAlbumDao.lastestView(userId, start, pageSize);
        if (list == null || list.isEmpty())
            return null;
        List<List<AlbumViewLog>> result = new ArrayList<List<AlbumViewLog>>();
        String curDate = null;
        List<AlbumViewLog> curList = null;
        for (AlbumViewLog log : list) {
            if (!StringUtils.equals(curDate, log.getViewDate())) {
                curList = new ArrayList<AlbumViewLog>();
                result.add(curList);
                curDate = log.getViewDate();
            }
            curList.add(log);
        }
        return result;
    }

    private void updateViewCount(PhotoAlbum album, User viewUser) {
        photoAlbumDao.updateViewCount(album.getId());
        System.out.println("去掉浏览详情");
        //album.incViewCount();
        System.out.println("album.getId()"+album.getId());
        if (viewUser != null) {
            AlbumViewLog log = new AlbumViewLog();
            log.setAlbumId(album.getId());
            log.setModelId(album.getModelId());
            log.setPhotoGrapherId(album.getPhotoGrapherId());
            log.setUserId(viewUser.getId());
            log.setViewTime(new Date());
            photoAlbumDao.insertViewLog(log);
        }
    }

    @Override
    public Integer selectViewCount(Integer albumId) {
        return photoAlbumDao.selectViewCount(albumId);
    }

    @Override
    public GodViewLikeVO selectIsLikeAlbum(GodViewLikeVO likeVO) {
        return photoAlbumDao.selectIsLikeAlbum(likeVO);
    }

    @Override
    public GodViewLikeVO selectIsCollectionAlbum(GodViewLikeVO likeVO) {
        return photoAlbumDao.selectIsCollectionAlbum(likeVO);
    }

    /**
     * 点赞图集
     *
     * @param likeVO
     */
    @Override
    public void giveUpAlbum(GodViewLikeVO likeVO) {

        photoAlbumDao.insertViewLike(likeVO);
        photoAlbumDao.updateAlbumUp(likeVO);
    }

    /**
     * 取消点赞图集
     *
     * @param likeVO
     */
    @Override
    public void giveDownAlbum(GodViewLikeVO likeVO) {

        photoAlbumDao.deleteViewLike(likeVO);
        photoAlbumDao.updateAlbumDown(likeVO);
    }

    @Override
    public void collectionDownAlbum(GodViewLikeVO likeVO) {
        photoAlbumDao.collectionDownAlbum(likeVO);
    }

    @Override
    public void collectionUpAlbum(GodViewLikeVO likeVO) {
        photoAlbumDao.collectionUpAlbum(likeVO);
    }

    @Override
    public Integer selectAlbumLikeCount(Integer albumId) {
        return photoAlbumDao.selectAlbumLikeCount(albumId);
    }

    @Override
    public PhotoAlbum loadPhotoAlbum(Integer id) {
        PhotoAlbum p = photoAlbumDao.detail(id);
        if (p != null) {
            if (StringUtils.isEmpty(p.getCoverUrl())) {
                Photo photo = photoAlbumDao.photoinfo(p.getId(), 0);
                if (photo != null)
                    p.setCover(photo.getFullPath());
            }
        }
        return p;
    }

    @Override
    public List<Tag> randomTags(Integer start, Integer pageSize) {
        return photoAlbumDao.randomTags(start, pageSize);
    }

    @Override
    public void submitAlbumTag(Integer albumId, Integer tagId) {
        List<AlbumTag> list = photoAlbumDao.queryAlbumTag(albumId, tagId);
        if (list != null && list.size() > 0)
            photoAlbumDao.incAlbumTagCount(list.get(0).getId());
        else {
            AlbumTag albumTag = new AlbumTag();
            albumTag.setAlbumId(albumId);
            albumTag.setTagId(tagId);
            photoAlbumDao.insertAlbumTag(albumTag);
        }
    }

    public Integer getUserSaleLevel(Integer userId, Integer albumId) {
        Order order = new Order();
        order.setAlbumId(albumId);
        order.setBuyerId(userId);
        order.setOrderType(Constant.GOODS_TYPE_ALBUM);
        order.setStatus(Constant.ORDER_FINISHED);
        Integer maxLevel = orderMapper.queryMaxSaleLevel(order);
        if (maxLevel != null && maxLevel >= 2) {
            //已购买全集
            return 2;
        }

        if (maxLevel != null && maxLevel == 1) {
            //已解锁1级
            return 1;
        }
        //查看用户是否是vip用户
        User Usery = new User();
    	Usery.setId(userId);
        User userOne =	userMapper.selectUser(Usery);//得到用户
        if(userOne.getType()!=null){
        if(userOne.getType()==1){
        	//已购买全集（会员vip独享）
            return 2;
        	}
        }
        //查看青豆支付记录
        RecordQingdou record = new RecordQingdou();
        record.setAlbumId(albumId);
        record.setUserId(userId);
        record.setRecType(Constant.QD_PAY_ALBUM);
        Integer count = recordQingdouMapper.checkAlbumPermission(record);
        if (count > 0)
            return 2;

        return 0;
    }

    @Override
    public int updateQDPayForAlbum(User user, PhotoAlbum album) {
        if (user != null && album != null) {
            if (album.getQingdou() > 0) {
                RecordQingdou record = new RecordQingdou();
                record.setUserId(user.getId());
                record.setAlbumId(album.getId());
                record.setRecType(Constant.QD_PAY_ALBUM);
                record.setQingdouAmount(0 - album.getQingdou());
                recordQingdouMapper.insert(record);
                userMapper.updateValue("qingdou", user.getQingdou() - album.getQingdou(), user.getId());
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int updateDownLimitForAlbum(User user, PhotoAlbum album) {
        if (user != null && album != null) {
            if (album.getActivityId() != null) {
                // 未购买过的活动作品
                RecordDownLimit record = new RecordDownLimit();
                record.setActivityId(album.getActivityId());
                record.setUserId(user.getId());
                record.setModelId(album.getModelId());
                record.setStatus(Constant.DOWN_LIMIT_UNUSED);
                RecordDownLimit limit = recordDownLimitMapper.getDownLimit(record);
                if (limit == null)
                    return -1;

                limit.setAlbumId(album.getId());
                limit.setConsumeTime(new Date());
                limit.setStatus(Constant.DOWN_LIMIT_USED);
                recordDownLimitMapper.updateByPrimaryKey(limit);
                return 1;
            }
        }
        return 0;
    }

    public int saveUserAlbumLog(Integer userId, Integer albumId, String tags, Integer starCount, String userComment) {
        UserAlbumLog userAlbumLog = new UserAlbumLog();
        userAlbumLog.setAlbumid(albumId);
        userAlbumLog.setUserid(userId);
        int ret = 0;
        if (StringUtils.isNotEmpty(userComment)) {
            userAlbumLog.setComment(userComment);
            Tag tag = photoAlbumDao.loadTagByName(userComment);
            if (tag == null) {
                tag = new Tag();
                tag.setTagName(userComment);
                photoAlbumDao.insertTag(tag);
            }

            submitAlbumTag(albumId, tag.getId());
            ret++;
        }
        userAlbumLog.setCreateTime(new Date());
        userAlbumLog.setStar(starCount);
        userAlbumLog.setTags(tags);
        userAlbumLogMapper.insertSelective(userAlbumLog);

        String[] tagIds = StringUtils.split(tags, ',');
        for (String strId : tagIds) {
            if (StringUtils.isNumeric(strId)) {
                submitAlbumTag(albumId, Integer.parseInt(strId));
                ret++;
            }
        }
        return ret;
    }

    @Override
    public Double queryAlbumStar(Integer albumId) {
        return photoAlbumDao.queryAlbumStar(albumId);
    }

    @Override
    public int queryAlbumStarByUserId(Integer albumId, Integer userId) {
        return photoAlbumDao.queryAlbumStarByUserId(albumId, userId);
    }

    @Override
    public UserAlbumLog queryUserAlbumLog(Integer albumId, Integer userId) {
        UserAlbumLog userAlbumLog = new UserAlbumLog();
        userAlbumLog.setUserid(userId);
        userAlbumLog.setAlbumid(albumId);
        return userAlbumLogMapper.queryUserAlbumLog(userAlbumLog);
    }

    @Override
    public PhotoAlbum queryAlbumByPrimaryId(int albumId,User user ,boolean updateViewCount) throws Exception {
        PhotoAlbum album = photoAlbumDao.queryAlbumByPrimaryId(albumId,user==null?0:user.getId());
        List<Photo> photo = photoAlbumDao.photos(albumId);
        
        if(photo != null && photo.size()>0) {
            Metadata metadata = null;
          System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
            for (Photo _p : photo) {
                String path = _p.getPaths();
                String _fullPath = new String(("/home/" + path).getBytes("gbk"));
                String fullPath = com.wishes.yearOld.common.StringUtils.replaceChinese(_fullPath, "??");
                File file = new File(fullPath);
                if (!file.exists()) {
                    file = new File(_fullPath);
                }
                _p.setMake("");
                _p.setModel("");
                _p.setFocalLength("");
                _p.setfNumber("");
                _p.setShutterSpeed("");
                _p.setiSOSpeedRatings("");
                _p.setLensSerial("");
                if (!file.exists()) {
                    continue;
                }
                if(album.getIsQdk()!=0){
                metadata = JpegMetadataReader.readMetadata(file);
                if(metadata!=null){
                Iterable<Directory> iterable = metadata.getDirectories();
                for (Iterator<Directory> iter = iterable.iterator(); iter.hasNext(); ) {
                    Directory dr = iter.next();
                    Collection<com.drew.metadata.Tag> tags = dr.getTags();
                    for (com.drew.metadata.Tag tag : tags) {
                        if ("Make".equals(tag.getTagName())) {
                            _p.setMake(tag.getDescription());
                        }
                        if ("Model".equals(tag.getTagName())) {
                            _p.setModel(tag.getDescription());
                        }
                        if ("Focal Length".equals(tag.getTagName())) {
                            _p.setFocalLength(tag.getDescription());
                        }
                        if ("F-Number".equals(tag.getTagName())) {
                            _p.setfNumber(tag.getDescription());
                        }
                        if ("Shutter Speed Value".equals(tag.getTagName())) {
                            _p.setShutterSpeed(tag.getDescription());
                        }
                        if ("ISO Speed Ratings".equals(tag.getTagName())) {
                            _p.setiSOSpeedRatings(tag.getDescription());
                        }
                        if ("Lens Serial Number".equals(tag.getTagName())) {
                            _p.setLensSerial(tag.getDescription());
                        }
                    }
                }
               }
              }
            }
            if (StringUtils.isTrimEmpty(album.getCover())) {
                album.setCover(photo.get(0).getFullPath());
            }
            album.setPhotos(photo);
        }
        
        //增加判断是否是青豆客官网发布1是青豆客官网发布2用户自定义发布
        if(album.getIsQdk()==1){
        Integer curUserId = user == null ? null : user.getId();
        album.setPermission(calUserAlbumPermission(album, curUserId));
        }else{
        AlbumPermission permissions = new AlbumPermission();
        permissions.setAlbumId(album.getId());
        permissions.setPermitType(5);
        permissions.setPermitDesp("限免作品");
        permissions.setUserLevel(2);
        album.setPermission(permissions);
        }
        if (updateViewCount)
            updateViewCount(album, user);
        return album;
    }
    /**
     * 查询用户图集
     * @author tmg-zwl
     * @param userId
     * @return
     */
	@Override
	public ArrayList<Photoalbuminfo> queryBuminfo(Photoalbuminfo photoalbuminfo,Integer start,Integer pageSize) {
	     // photoalbuminfo.setStart(start);
         //   photoalbuminfo.setPageSize(pageSize);
		ArrayList<Photoalbuminfo> lstBuminfo = photoAlbumDao.queryBuminfo(photoalbuminfo);
		return lstBuminfo;
	}
	/**
     * 查询用户图片
     * @author tmg-zwl
     * @param photoalbumId
     * @return
     */
	@Override
	public ArrayList<Piclist> queryPiclist(Integer photoalbumId) {
		ArrayList<Piclist> Piclists = photoAlbumDao.queryPiclist(photoalbumId);
		return Piclists;
	}
	/**
     * 查询用户图片
     * @author tmg-zwl
     * @param photoalbumId
     * @return
     */
	@Override
	public ArrayList<Piclist> queryPiclistAll(Integer photoalbumId) {
		ArrayList<Piclist> Piclists = photoAlbumDao.queryPiclistAll(photoalbumId);
		return Piclists;
	}
	/**
     * 插入图集数据
     * @author tmg-zwl
     * @param PhotoAlbum
     * @return
     */
	 @Override
	  public int insertSelective(PhotoAlbum record) {
	         photoAlbumDao.insertSelective(record);
	         TgodAlbumcountinfo albumcountinfo = new TgodAlbumcountinfo();
			 albumcountinfo.setId(record.getId());
			return	tgodAlbumcountinfoMapper.insertSelective(albumcountinfo);//保存图集统计记录表数据
	  }
	 /**
	     * 更改图集数据
	     * @author tmg-zwl
	     * @param PhotoAlbum
	     * @return
	     */
	 @Override
	    public int updateByPrimaryKeySelective(PhotoAlbum record) {
	        return photoAlbumDao.updateByPrimaryKeySelective(record);
	    }
    /**
     * 返回首页最热列表
     */
	@Override
	public LinkedList<PhotoAlbum> getHomeAlbumsHot(PhotoAlbum albumy) {
		  LinkedList<PhotoAlbum> albums = photoAlbumDao.getHomeAlbums(albumy);
	        for(PhotoAlbum album:albums){
	            if(StringUtils.isTrimEmpty(album.getCover())){
	                List<Photo> photos = photoAlbumDao.photos(album.getId());
	                if(photos != null && photos.size() > 0) {
	                    album.setCover(photos.get(0).getFullPath());
	                }
	            }
	        }
	        return albums;
	}




}

package com.wishes.yearOld.common;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by IntelliJ IDEA.
 * User:    zwl
 * Company: 初开（重庆）有限公司
 * DateTime:2017-5-04 19:37:23
 * Description: 数组进行排序
 */
public class StringSort {
	// private static final Log _log = LogFactory.getLog(StringSort.class);
	    /**
	     * 对文件数组进行排序
	     * @param CommonsMultipartFile[]
	     * @return
	     * */
	    public static CommonsMultipartFile[] getUrlParam(CommonsMultipartFile[] files){
	        
	        for (int i = 0; i < files.length - 1; i++) {
	            for (int j = 0; j < files.length - i -1; j++) {
	                String pres = files[j].getOriginalFilename().substring(0,files[j].getOriginalFilename().lastIndexOf("."));
	                String nexts = files[j+1].getOriginalFilename().substring(0,files[j+1].getOriginalFilename().lastIndexOf("."));
	                CommonsMultipartFile presy=files[j];
	                CommonsMultipartFile nextsy=files[j+1];
	                int  pre=Integer.parseInt(pres);
	                int  next=Integer.parseInt(nexts);
	                if(pre>next){
	                	CommonsMultipartFile temp = presy;
	                    files[j] = nextsy;
	                    files[j+1] = temp;
	                }
	            }
	        }
	        return files;
	    }

	    /**
	     * 比较两个字符串的大小，按字母的ASCII码比较
	     * @param pre
	     * @param next
	     * @return
	     * */
	    private static boolean isMoreThan(String pre, String next){
	        if(null == pre || null == next || "".equals(pre) || "".equals(next)){
	         //   _log.error("字符串比较数据不能为空！");
	            return false;
	        }
	        
	        char[] c_pre = pre.toCharArray();
	        char[] c_next = next.toCharArray();
	        
	        int minSize = Math.min(c_pre.length, c_next.length);
	        
	        for (int i = 0; i < minSize; i++) {
	            if((int)c_pre[i] > (int)c_next[i]){
	                return true;
	            }else if((int)c_pre[i] < (int)c_next[i]){
	                return false;
	            }
	        }
	        if(c_pre.length > c_next.length){
	            return true;
	        }
	        
	        return false;
	    }
	    
	    
	    public static void main(String[] args) {
	        
	       /* String[] keys = getUrlParam(new String[]{"fin","abc","shidema","shide","bushi"});
	        
	        for (String key : keys) {
	            System.out.println(key);
	        }*/

	    }
}

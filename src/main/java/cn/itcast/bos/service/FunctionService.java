package cn.itcast.bos.service;

import cn.itcast.bos.domain.Function;

import java.util.List;

/**
 * 权限操作
 * 
 * @author seawind
 * 
 */
public interface FunctionService {

	/**
	 * 查询用户可以看见的菜单
	 * 
	 * @param username
	 * @return
	 */
	public List<Function> findMenu(String username);

}

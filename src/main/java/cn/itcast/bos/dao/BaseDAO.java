package cn.itcast.bos.dao;

import java.io.Serializable;
import java.util.List;


import org.apache.ibatis.annotations.Param;



/**
 * 基础DAO ，提供通用增删改查方法
 * 
 * @author seawind
 * 
 */

public interface BaseDAO<T> {
	// 添加
	public void insert(T entity);

	// 修改
	public void update(T entity);

	// 删除
	public void delete(T entity);

	// 查询所有
	public List<T> findAll();

	List<T> findByCondition(T entity);

	// 根据id 查询
	// String int 类型都实现 Serializable
	public T findById(Serializable id);

	// 查询记录总数
	public int findTotalCount();

}

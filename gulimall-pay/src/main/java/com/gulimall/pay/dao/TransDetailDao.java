package com.gulimall.pay.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gulimall.pay.entity.TransDetailDo;

public interface TransDetailDao extends BaseMapper<TransDetailDo> {
	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table foreo_trans_detail
	 *
	 * @mbg.generated
	 */
	int deleteByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table foreo_trans_detail
	 *
	 * @mbg.generated
	 */
	int insert(TransDetailDo record);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table foreo_trans_detail
	 *
	 * @mbg.generated
	 */
	int insertSelective(TransDetailDo record);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table foreo_trans_detail
	 *
	 * @mbg.generated
	 */
	TransDetailDo selectByPrimaryKey(String id);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table foreo_trans_detail
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKeySelective(TransDetailDo record);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table foreo_trans_detail
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKeyWithBLOBs(TransDetailDo record);

	/**
	 * This method was generated by MyBatis Generator.
	 * This method corresponds to the database table foreo_trans_detail
	 *
	 * @mbg.generated
	 */
	int updateByPrimaryKey(TransDetailDo record);

	List<TransDetailDo> selectTransDetailListByOrderId(Object obj, RowBounds buildRowBounds);
}

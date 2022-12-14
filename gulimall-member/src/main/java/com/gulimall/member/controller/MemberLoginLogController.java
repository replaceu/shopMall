package com.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gulimall.common.utils.R;
import com.gulimall.member.entity.MemberLoginLogEntity;
import com.gulimall.member.service.MemberLoginLogService;
import com.gulimall.service.utils.PageUtils;

/**
 * 会员登录记录
 *
 * @author aqiang9
 * @email ${email}
 * @date 2020-07-27 10:19:25
 */
@RestController
@RequestMapping("member/memberloginlog")
public class MemberLoginLogController {
	@Autowired
	private MemberLoginLogService memberLoginLogService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("member:memberloginlog:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = memberLoginLogService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("member:memberloginlog:info")
	public R info(@PathVariable("id") Long id) {
		MemberLoginLogEntity memberLoginLog = memberLoginLogService.getById(id);

		return R.ok().put("memberLoginLog", memberLoginLog);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody MemberLoginLogEntity memberLoginLog) {
		memberLoginLogService.save(memberLoginLog);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody MemberLoginLogEntity memberLoginLog) {
		memberLoginLogService.updateById(memberLoginLog);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] ids) {
		memberLoginLogService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}

}

package com.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gulimall.common.utils.R;
import com.gulimall.member.entity.MemberCollectSubjectEntity;
import com.gulimall.member.service.MemberCollectSubjectService;
import com.gulimall.service.utils.PageUtils;

/**
 * 会员收藏的专题活动
 *
 * @author aqiang9
 * @email ${email}
 * @date 2020-07-27 10:19:25
 */
@RestController
@RequestMapping("member/membercollectsubject")
public class MemberCollectSubjectController {
	@Autowired
	private MemberCollectSubjectService memberCollectSubjectService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = memberCollectSubjectService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Long id) {
		MemberCollectSubjectEntity memberCollectSubject = memberCollectSubjectService.getById(id);

		return R.ok().put("memberCollectSubject", memberCollectSubject);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody MemberCollectSubjectEntity memberCollectSubject) {
		memberCollectSubjectService.save(memberCollectSubject);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody MemberCollectSubjectEntity memberCollectSubject) {
		memberCollectSubjectService.updateById(memberCollectSubject);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] ids) {
		memberCollectSubjectService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}

}

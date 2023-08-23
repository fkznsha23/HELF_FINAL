package kr.co.helf.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kr.co.helf.controller.OrderEnum.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.helf.dto.MyMembershipListDto;
import kr.co.helf.dto.MyOrderDetailDto;
import kr.co.helf.dto.OrderJoin;
import kr.co.helf.dto.OrderListDto;
import kr.co.helf.form.AddMembershipForm;
import kr.co.helf.form.OrderSearchForm;
import kr.co.helf.service.MembershipService;
import kr.co.helf.vo.Category;
import kr.co.helf.vo.MyMembership;
import kr.co.helf.vo.MyOption;
import kr.co.helf.vo.Order;
import kr.co.helf.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/membership")
@Slf4j
public class MembershipController {

	private final MembershipService membershipService;
	
	@GetMapping("/list")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String membershipList(@AuthenticationPrincipal User user, Model model) {
		
		List<MyMembershipListDto> myMemberships = membershipService.getMyMembershipById(user.getId());
		
		model.addAttribute("list", myMemberships);
		
		return "membership/list";
	}
	
	@GetMapping("/refound")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String refound(@RequestParam("no") int no, @AuthenticationPrincipal User user) {
		Order order = membershipService.getOrderByMyMembershipNo(no);
		
		if(!user.getId().equals(order.getUser().getId())) {
			return "redirect: list?error=no-authority";
		}
		
		order.setState(WAITREFOUND.getOrderEnum());
		membershipService.updateOrder(order);
		
		MyMembership myMembership = membershipService.getUseMyMembershipByNo(no);
		myMembership.setState(IMPOSSIBILITY.getOrderEnum());
		membershipService.updateMyMembership(myMembership);
		
		return "redirect:list";
	}
	
	@GetMapping("/order-list")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String orderList(@AuthenticationPrincipal User user, Model model,
							@ModelAttribute OrderSearchForm form) {
		
		if(form.getPage() == null) {
			form.setPage(1);
		}
		
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.hasText(form.getState())) {
			map.put("state", form.getState());
		}
		if (StringUtils.hasText(form.getType())) {
			map.put("type", form.getType());
		}
		if (StringUtils.hasText(form.getKeyword())) {
			map.put("keyword", form.getKeyword());
		}
		
		map.put("userId", user.getId());
		
		OrderListDto orderList = membershipService.getOrdersById(form.getPage(), map);
		
		model.addAttribute("dto", orderList);
		
		return "membership/order-list";
	}
	
	@GetMapping("/order-detail")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String orderDetail(@RequestParam("no") int no, Model model, @AuthenticationPrincipal User user) {
		OrderJoin orderJoin = membershipService.getOrderByNo(no);

		if(!user.getId().equals(orderJoin.getUser().getId())) {
			return "redirect: loginform?error=wrong-user";
		}
		
		MyOrderDetailDto dto = new MyOrderDetailDto();
		dto.setOrderJoin(orderJoin);
		
		List<MyOption> myOptions = membershipService.getMyOptions(orderJoin.getMyMembershipNo());
		dto.setMyOptions(myOptions);
		
		model.addAttribute("dto", dto);
		
		return "membership/order-detail";
	}
	
	@GetMapping("/create-form")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String createForm(Model model) {

		List<Category> categorys = membershipService.getAllCategory();
		model.addAttribute("categorys", categorys);
		
		return "membership/create-form";
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String create(@ModelAttribute AddMembershipForm form) {
		
		membershipService.addMembership(form);
		
		return "redirect:list-manager";
	}

	@GetMapping("/list-manager")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String listManager() {
		return "membership/list-manager";
	}

	@GetMapping("/refound-manager")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String refoundManager() {
		return "membership/refound-manager";
	}
}

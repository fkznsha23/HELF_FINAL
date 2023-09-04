package kr.co.helf.controller;

import kr.co.helf.dto.AttendanceList;
import kr.co.helf.dto.CustomerDetailDto;
import kr.co.helf.service.UserService;
import kr.co.helf.vo.Lesson;
import kr.co.helf.vo.MySalary;
import kr.co.helf.vo.TrainerAttendance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/management")
@Slf4j
public class ManagementController {

	@Autowired
	UserService userService;
	
	
	// 전체 고객 목록 화면 요청과 매핑되는 요청핸들러 메소드 
	@GetMapping(value="customer-list")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String customerList(@RequestParam(name="rows", required=false, defaultValue="20") int rows,
			@RequestParam(name="page", required=false, defaultValue = "1") int page,
			@RequestParam(name="opt", required=false, defaultValue= "") String opt, 
			@RequestParam(name="keyword", required=false, defaultValue="") String keyword,
			@RequestParam(name="userStatus", required=false, defaultValue="") String userStatus,
			@RequestParam(name="membershipState", required=false, defaultValue="") String membershipState,
			@RequestParam(name="remainderCnt", required=false) Integer remainderCnt,
			@RequestParam(name="remainingDays1", required=false, defaultValue="") @DateTimeFormat(pattern ="yyyy-MM-dd") Date remainingDays1,
			@RequestParam(name="remainingDays2", required=false, defaultValue="") @DateTimeFormat(pattern ="yyyy-MM-dd") Date remainingDays2,
			Model model) {
		log.info("rows='{}', page='{}', opt='{}' keyword='{}' userStatus='{}' membershipState='{}' remainderCnt='{}' remainingDays1='{}' remainingDays2='{}' ", 
				rows, page, opt, keyword, userStatus, membershipState, remainderCnt, remainingDays1, remainingDays2);
		 	
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("rows", rows);
		param.put("page", page);
		param.put("remainderCnt", remainderCnt);
		param.put("userStatus", userStatus);
		param.put("membershipState", membershipState);
		if(StringUtils.hasText(opt) && StringUtils.hasText(keyword)) {
			param.put("opt", opt);
			param.put("keyword", keyword);
		}
		if(remainingDays1 != null && remainingDays2 != null) {
			param.put("remainingDays1", remainingDays1);
			param.put("remainingDays2", remainingDays2);
		}
		
		Map<String, Object> result = userService.getAllCustomerInfo(param);
		
		model.addAttribute("customers", result.get("customerList"));
		model.addAttribute("pagination", result.get("pagination"));
		model.addAttribute("totalRows", result.get("totalRows"));
		
		return "management/customerList";
	}
	
	// 고객 상세 페이지 요청과 매핑되는 요청핸들러 메소드
	@GetMapping(value="customer-detail")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String customerDetail(@RequestParam("id") String userId, Model model) {
		Map<String, Object> result = userService.getCustomerDetails(userId);
		model.addAttribute("customerInfo", result.get("customerInfo"));
		model.addAttribute("lessonApply", result.get("lessonApply"));
		model.addAttribute("customerOrderDto", result.get("customerOrderDto"));
		model.addAttribute("myMembershipList", result.get("myMembershipList"));
		model.addAttribute("customerAttendance", result.get("customerAttendance"));
		
		return "management/customerDetail";
	}
	
	// 고객 상세 - 최근 방문 내역 페이지 요청과 매핑되는 요청핸들러 메소드 
	@GetMapping(value="customer-recent-visit")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String customerRecentVisit(@RequestParam("id") String userId, 
			@RequestParam(name = "specificDate1", required = false) String specificDate1,
			@RequestParam(name = "specificDate2", required = false) String specificDate2,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			Model model) { 
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("page", page);
		param.put("userId", userId);
		if (StringUtils.hasText(specificDate1)) {
			param.put("specificDate1", specificDate1);			
		}
		if (StringUtils.hasText(specificDate2)) {
			param.put("specificDate2", specificDate2);			
		}
		
		Map<String, Object> result = userService.getCustomerAttendances(param);
		CustomerDetailDto customerDetailDto = userService.getPrivateInfo(userId);
		
		model.addAttribute("recentVisits", result.get("recentVisits"));
		model.addAttribute("pagination", result.get("pagination"));
		model.addAttribute("customerDetailDto", customerDetailDto);
		
		return "management/customerRecentVisits";
	}
	
	// 트레이너 목록 페이지 요청과 매핑되는 요청핸들러 메소드
	@GetMapping(value="trainer-list")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String trainerList(@RequestParam(name="rows", required=false, defaultValue="10") int rows,
			@RequestParam(name="page", required=false, defaultValue="1") int page,
			@RequestParam(name="opt", required=false, defaultValue="") String opt, 
			@RequestParam(name="keyword", required=false, defaultValue="") String keyword,
			@RequestParam(name="trainerStatus", required=false, defaultValue="전체") String trainerStatus,
			@RequestParam(name="trainerTitle", required=false, defaultValue="전체") String trainerTitle,
			Model model) {
		log.info("rows='{}', page='{}', opt='{}' keyword='{}' trainerStatus='{}' trainerTitle='{}' ", 
				rows, page, opt, keyword, trainerStatus, trainerTitle);
		
		Map<String, Object> param = new HashMap<>();
		param.put("rows", rows);
		param.put("page", page);
		param.put("trainerStatus", trainerStatus);
		param.put("trainerTitle", trainerTitle); 
		if(StringUtils.hasText(opt)) {
			param.put("opt", opt);
		}
		if(StringUtils.hasText(keyword)) {
			param.put("keyword", keyword);
		}
		
		Map<String, Object> result = userService.getAllTrainerInfo(param);
		model.addAttribute("trainers", result.get("trainerList"));
		model.addAttribute("pagination", result.get("pagination"));
		model.addAttribute("totalRows", result.get("totalRows"));
		
		return "management/trainerList";
	}
	
	// 트레이너 상세 페이지 - 채경, 준오, 예광 
	@GetMapping(value="trainer-detail")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String trainerDetail(@RequestParam("id") String userId, Model model) {
		
		// 트레이너 개인 정보 
		MySalary trainerInfo = userService.getTrainerDetailById(userId);
		// 트레이너 출결 내역 
		List<TrainerAttendance> attendances = userService.getTrainerThreeAttendances(userId);
		// 그룹레슨 최근 5개 내역 조회(예광)
		List<Lesson> lessons = userService.getRecentLessons(userId);
		
		
		model.addAttribute("trainerInfo", trainerInfo);
		model.addAttribute("attendances", attendances);
		// 그룹레슨 최근 5개 내역 조회(예광)
		model.addAttribute("lessons", lessons);

		return "management/trainerDetail";
	}

	// 그룹수업 자세히보기-(예광)
	@GetMapping("/moreGroupLesson")
	public String moreGroupLesson(@RequestParam(name="page",required = false,defaultValue = "1") int page,
								  @RequestParam("id") String userId,
								  Model model){
		System.out.println(userId);
		// 트레이너 개인 정보
		MySalary trainerInfo = userService.getTrainerDetailById(userId);


		Map<String,Object> param = new HashMap<>();
		param.put("page", page);
		Map<String,Object> result = userService.trainerMyAllLessons(param,userId);

		model.addAttribute("result", result);
		return "management/moreGroupLesson";
	}

	
	// 트레이너 상세 페이지 - 최근 출결 내역 자세히 보기 채경 
	@GetMapping(value="trainer-attendance-list")
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public String trainerAttendances(@RequestParam("id") String userId, 
			@RequestParam(name="state", required=false, defaultValue="") String state,
			@RequestParam(name="page", required=false, defaultValue="1") int page,
			Model model) {
		log.info("userId='{}', state='{}', page='{}'", userId, state, page);
		// 트레이너 개인 정보 
		MySalary trainerInfo = userService.getTrainerDetailById(userId);
		// 트레이너 출결 내역 
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);		
		param.put("page", page);
		if(StringUtils.hasText(state)) {
			param.put("state", state);
		}
		AttendanceList attendance = userService.getTrainerAttendances(param);
		
		model.addAttribute("trainerInfo", trainerInfo);
		model.addAttribute("attendance", attendance.getAttendances());
		model.addAttribute("pagination", attendance.getPagination());
		
		return "management/trainerAttendances";
	}
	
}
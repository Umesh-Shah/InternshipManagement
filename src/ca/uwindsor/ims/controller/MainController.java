/**
 * Purpose: Handling all the user requests
 * @author Jay Raichandani 
 */

package ca.uwindsor.ims.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.LinkedHashMap;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.sun.mail.smtp.SMTPTransport;

import ca.uwindsor.ims.model.ApprovalStudentBo;
import ca.uwindsor.ims.model.CommonDTO;
import ca.uwindsor.ims.model.CompanyBo;
import ca.uwindsor.ims.model.InternshipTypeBo;
import ca.uwindsor.ims.model.JobBo;
import ca.uwindsor.ims.model.Login;
import ca.uwindsor.ims.model.LoginBo;
import ca.uwindsor.ims.model.ReportBo;
import ca.uwindsor.ims.model.ReportlistBo;
import ca.uwindsor.ims.model.SkillBo;
import ca.uwindsor.ims.model.StudentCertificateBo;
import ca.uwindsor.ims.model.StudentEducationBo;
import ca.uwindsor.ims.model.StudentGpaBo;
import ca.uwindsor.ims.model.StudentInfoBo;
import ca.uwindsor.ims.model.StudentInternshipBo;
import ca.uwindsor.ims.model.StudentJobSkillbo;
import ca.uwindsor.ims.model.StudentSkillBo;
import ca.uwindsor.ims.model.StudentWorkBo;
import ca.uwindsor.ims.model.Student_Job_mapping;
import ca.uwindsor.ims.service.CommonService;
import ca.uwindsor.ims.service.CompanyService;
import ca.uwindsor.ims.service.InternshipService;
import ca.uwindsor.ims.service.JobService;
import ca.uwindsor.ims.service.RegLoginService;
import ca.uwindsor.ims.service.ReportService;
import ca.uwindsor.ims.service.SkillService;
import ca.uwindsor.ims.service.StudentService;

@Controller
@RequestMapping(value="main")
public class MainController {
	
	Logger log = Logger.getLogger(MainController.class);
	
	
	/*
	 * Injecting dependencies - Data Access Objects (Service Layer)
	 */
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private InternshipService internshipService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private RegLoginService regLoginService;
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private SkillService skillService;
	
	@Autowired
	private StudentService studentService;
	
	
	/**
	 * Delegate Methods 
	 * Getters and Setters  
	 */
	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public CompanyService getCompanyService() {
		return companyService;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	public InternshipService getInternshipService() {
		return internshipService;
	}

	public void setInternshipService(InternshipService internshipService) {
		this.internshipService = internshipService;
	}

	public JobService getJobService() {
		return jobService;
	}

	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}

	public RegLoginService getRegLoginService() {
		return regLoginService;
	}

	public void setRegLoginService(RegLoginService regLoginService) {
		this.regLoginService = regLoginService;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public SkillService getSkillService() {
		return skillService;
	}

	public void setSkillService(SkillService skillService) {
		this.skillService = skillService;
	}

	public StudentService getStudentService() {
		return studentService;
	}

	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}

	DateFormat dateandtime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
	
	
	public static final String MODEL_ATTRIBUTE_LIST = "lst";
	public static final String VIEW_ADMIN_MAIN = "internship/admin/admin_welcome";
	
	
	
	@RequestMapping(value = "/goHome")
	public String goHome(HttpServletRequest request) {
		log.info("-START--");
		HttpSession session = request.getSession();
		/*if(null!=session.getAttribute("admin")){
			return "redirect:donationForm.htm";
		}*/
		request.setAttribute("filename", "content");
		log.info("-END--");
		return "welcome";
	}
	
	@RequestMapping(value = "/loadlogin")
	public String loadlogin(HttpServletRequest request,Model model) {
		log.info("-START--");
		model.addAttribute("login", new Login());
		request.setAttribute("filename", "login");
		return "welcome";
	}
	
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request) {
		log.info("-START--");
//		request.setAttribute("filename", "content");
		return "admin/adminwelcome";
	}

	@RequestMapping(value = "/aboutUs")
	public String aboutUs(HttpServletRequest request) {
		log.info("-START--");
		request.setAttribute("filename", "aboutUs");
		return "welcome";
	}
	
	@RequestMapping(value = "/contact")
	public String contact(HttpServletRequest request) {
		log.info("-START--");
		request.setAttribute("filename", "contact");
		return "welcome";
	}
	
	@RequestMapping(value = "/gallery")
	public String gallery(HttpServletRequest request) {
		log.info("-START--");
		request.setAttribute("filename", "gallery");
		return "welcome";
	}
	@RequestMapping(value = "/logoutnow")
	public String logoutnow(HttpServletRequest request) {
		log.info("-START--");
		request.getSession().invalidate();
		request.setAttribute("filename", "content");
		log.info("-END--");
		return "welcome";
	}
	
	@RequestMapping(value = "/trustees")
	public String trustees(HttpServletRequest request) {
		log.info("-START--");
		log.info("-END--+--");
		request.setAttribute("filename", "trustees");
		return "welcome";
	}
	
	@RequestMapping(value = "/checklogin", method = RequestMethod.POST)
	public String onLoginCheck(@ModelAttribute("Login") Login login,HttpServletRequest request,Model model) throws Exception{
		log.info("-START--" + login.getLogin());
		String username = login.getLogin();
		String password = login.getXtxtKllFbbd3ES();
		System.out.println(username +password);
		if(username.equals("admin") && password.equals("admin"))
		{
		model.addAttribute("login", new Login());
		request.setAttribute("filename", "company");
		System.out.println(request.getAttribute("filename"));
		HttpSession session  = request.getSession(true);
		session.setAttribute("admin","admin");
		return "internship/admin/admin_welcome";
		}
		else
		{
			boolean flag = regLoginService.checkLogin(username, password);
			if(flag){
				HttpSession session  = request.getSession(true);
				LoginBo loginbo = this.studentService.getStudentList(username,password);
				if(loginbo != null)
				{
					session.setAttribute("studentDtl", loginbo);
					List<StudentInfoBo> info=studentService.getstudentlistfromid(loginbo.getStudent_id());
					session.setAttribute("student", info.get(0).getFname()+" "+ info.get(0).getLname());
					//session.setAttribute(arg0, loginbo.get)
				}
			request.setAttribute("filename", "main");
			System.out.println(request.getAttribute("filename"));
			return "internship/student/student_welcome";
			}
		}
		return null;
		}
		
	
	// Internship
	@RequestMapping(value="/savecompany")
	public String savecompany(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("AddCompany") @Valid CompanyBo companybo)throws Exception{
		log.info("-START--<<< ");
		System.out.println(companybo.getCity());
		 commonService.saveDataComon(companybo);
		request.setAttribute("filename", "company");
		System.out.println(request.getAttribute("filename"));
		return "internship/admin/admin_welcome";
		}
	
	@RequestMapping(value="/saveSkill")
	public String saveSkill(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("AddSkill") @Valid SkillBo skillbo)throws Exception{
		log.info("-START--<<< ");
		
		
		commonService.saveDataComon(skillbo);
		request.setAttribute("filename", "Skills");
		System.out.println(request.getAttribute("filename"));
		return "internship/admin/admin_welcome";
		}
	@RequestMapping(value = "/loadSkill")
	public String loadSkill(HttpServletRequest request) {
		log.info("-START--");
		request.setAttribute("filename", "Skills");
		return "internship/admin/admin_welcome";
	}
	@RequestMapping(value="/savejob")
	public String savejob(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("AddJob") @Valid JobBo jobbo)throws Exception{
		log.info("-START--<<< ");
		commonService.saveDataComon(jobbo);
		 loadjob(request);
		request.setAttribute("filename", "add_job");
		System.out.println(request.getAttribute("filename"));
		return "internship/admin/admin_welcome";
		}
	@RequestMapping(value = "/loadjob")
	public String loadjob(HttpServletRequest request) throws Exception {
		log.info("-START--");
		List<CompanyBo> lst = companyService.getcompanylist();
		System.out.println(lst.get(0).getCompany_name());
		request.setAttribute("lst", lst);
		List<SkillBo> skill=skillService.getskilllist();
		request.setAttribute("skill",skill);
		request.setAttribute("filename", "add_job");
		return "internship/admin/admin_welcome";
	}
	
	@RequestMapping(value="/loadstudentForm")
	public String loadstudentForm(HttpServletRequest request, Model model) throws Exception{
		System.out.println("in load student form");
		model.addAttribute("common", new CommonDTO());
		request.setAttribute("filename", "student_info");
		return "internship/admin/admin_welcome";
	}
	@RequestMapping(value="/saveDemo")
	public String saveDemo(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("common") @Valid CommonDTO commondto)throws Exception{
		
		StudentEducationBo e=commondto.getEdu();
		StudentInfoBo i=commondto.getInfo();
		StudentCertificateBo certificatebo=commondto.getCerti();
		StudentWorkBo work=commondto.getWork();
		Integer stu_id=i.getStudent_id();
		
		
		/**
		 * Registration Confirmation Email Trigger
		 */
		
        String email_id=(String) i.getStu_email();
		  final String username = "jay.raichandani05@gmail.com";
		    final String password = "Mteverestjay1";
		    
		    
		    Properties props = System.getProperties();
	        props.put("mail.smtps.host","smtp.gmail.com");
	        props.put("mail.smtps.auth","true");
	        props.put("mail.smtp.port", "25");
	        Session session = Session.getInstance(props, null);
	        Message msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(username));
	        
	        msg.setRecipients(Message.RecipientType.TO,
	        InternetAddress.parse(email_id, false));
	        String fname=i.getFname();
	        String[] email_id1=email_id.split("@");
	        String upToNCharacters = email_id.substring(0, Math.min(email_id.length(), 4));
	   
	        String stu_id1= String.valueOf(i.getStudent_id());
	        String stu_idPwd=stu_id1.substring(4,8);
	        msg.setSubject("Successfully Register");
	        msg.setText("Dear  " +i.getFname()+" "+ i.getLname()+""+
	        "\n\n You are succesfully register in Internship Management System!" +
	        "\n Your Username is  "+fname+stu_idPwd+""+"And Password is "+upToNCharacters+stu_idPwd+"");
	        msg.setSentDate(new Date());
	        SMTPTransport t =
	            (SMTPTransport)session.getTransport("smtps");

	        t.connect("smtp.gmail.com",username, password);
	        t.sendMessage(msg, msg.getAllRecipients());
	        System.out.println("Response: " + t.getLastServerResponse());
	        t.close();
		 
	        studentService.savestudentinfo(i);
	        studentService.savestudentedu(e,stu_id);
	        studentService.savestucertificate(certificatebo,stu_id);
	        studentService.saveworkexp(work,stu_id);
	        LoginBo loginbo=new LoginBo();
	        loginbo.setUsername(email_id1[0]);
	        loginbo.setPwd(upToNCharacters+stu_idPwd);
	        loginbo.setFlag("Y");
	        loginbo.setUser_type("Student");
	        loginbo.setStudent_id(stu_id);
	        commonService.saveDataComon(loginbo);
		request.setAttribute("filename", "student_info");
		return "internship/admin/admin_welcome";
		
	}
	
	@RequestMapping(value="/addcompany")
	public String addcompany(HttpServletRequest request, Model model) throws Exception{
		System.out.println("in load student form");
		request.setAttribute("filename", "company");
		return "internship/admin/admin_welcome";
	}

	@RequestMapping(value="/joblist")
	public String joblist(HttpServletRequest request, Model model) throws Exception{
		List<JobBo> lst = jobService.getjoblist();
		request.setAttribute("lst", lst);
		HttpSession session = request.getSession(false);
		LoginBo loginbo=(LoginBo)session.getAttribute("studentDtl");
		int student_id=loginbo.getStudent_id();
		List<JobBo> s= jobService.getstudentjoblist(student_id);
		request.setAttribute("s", s);
		
		request.setAttribute("filename", "job_tab");
		return "internship/student/student_welcome";
	}
	
	@RequestMapping(value="/jobInterest")
	public String jobInterest(HttpServletRequest request, Model model) throws Exception{
		String jobid=request.getParameter("job_id");
	
				HttpSession session = request.getSession(false);
				LoginBo loginbo=(LoginBo)session.getAttribute("studentDtl");
				int student_id=loginbo.getStudent_id();
				int i = Integer.parseInt(jobid);
				Student_Job_mapping s=new Student_Job_mapping();
				s.setJob_id(i);
				s.setStudent_id(student_id);
				commonService.saveDataComon(s);
				System.out.println(student_id+"ssss"+jobid);
				List<JobBo> lst = jobService.getjoblist();
				request.setAttribute("lst", lst);
				List<JobBo> s1=jobService.getstudentjoblist(student_id);
				request.setAttribute("s", s1);
	        	request.setAttribute("filename", "job_tab");
		        return "internship/student/student_welcome";
	}
	@RequestMapping(value="/saveinternship")
	public String saveinternship(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("AddInternship") @Valid InternshipTypeBo i)throws Exception{
		log.info("-START--<<< ");
		
		
		commonService.saveDataComon(i);
		request.setAttribute("filename", "add_internship");
		System.out.println(request.getAttribute("filename"));
		return "internship/admin/admin_welcome";
		}

	@RequestMapping(value="/loadinternship")
	public String loadinternship(HttpServletRequest request, Model model) throws Exception{
		request.setAttribute("filename", "add_internship");
		return "internship/admin/admin_welcome";
	}
	@RequestMapping(value="/student_report")
	public String student_report(HttpServletRequest request, Model model) throws Exception{
		
		List<ReportlistBo> year=reportService.getstudentyear();
		request.setAttribute("year", year);
		List<ReportlistBo> country=reportService.getstudentcountry();
		request.setAttribute("country", country);
		request.setAttribute("filename", "search_report");
		return "internship/admin/admin_welcome";
	}
	@RequestMapping(value="/internshp_type_report")
	public String internshp_type_report(HttpServletRequest request, Model model) throws Exception{
		List<InternshipTypeBo> i=internshipService.getinternshiplist();
		request.setAttribute("i", i);
		List<StudentInfoBo> li=studentService.getstudentinternshipwise();
		request.setAttribute("list_internshiiipStu", li);
		request.setAttribute("filename", "search_internship_type");
		return "internship/admin/admin_welcome";
	}
	@RequestMapping(value="/company_report")
	public String company_report(HttpServletRequest request, Model model) throws Exception{
		List<CompanyBo> lst = companyService.getcompanylist();
		System.out.println(lst.get(0).getCompany_name());
		request.setAttribute("employers_lst", lst);
		request.setAttribute("filename", "search_report_employers");
		return "internship/admin/admin_welcome";
	}
	
	@RequestMapping(value="/gpa_report")
	public String gpa_report(HttpServletRequest request, Model model) throws Exception{
		
		List<ReportlistBo> year=reportService.getstudentyear();
		request.setAttribute("year", year);
		List<ReportlistBo> country=reportService.getstudentcountry();
		request.setAttribute("country", country);
		List<StudentGpaBo> lst=studentService.getstudentavggpa();
		request.setAttribute("lst", lst);
		 List<ReportlistBo> university=reportService.getuniversity();
		 request.setAttribute("university", university);
		 List<ReportlistBo> university_location=reportService.getuniversity_location();
		 request.setAttribute("university_location", university_location);
		request.setAttribute("filename", "gpa._report");
		return "internship/admin/admin_welcome";
	}
	@RequestMapping(value="/loadinternship_status")
	public String loadinternship_status(HttpServletRequest request, Model model) throws Exception{
		List<StudentInfoBo> info=studentService.getallstudentdata();
		request.setAttribute(MODEL_ATTRIBUTE_LIST, info);
		request.setAttribute("filename", "student_internshiplist");
		return VIEW_ADMIN_MAIN;
	}
	@RequestMapping(value="/insert_student_status")
	public String insert_student_status(HttpServletRequest request, Model model) throws Exception{
		int studentid=Integer.parseInt(request.getParameter("student_id"));
		request.setAttribute("studentid", studentid);
		List<CompanyBo> lst = companyService.getcompanylist();
		System.out.println(lst.get(0).getCompany_name());
		request.setAttribute("lst", lst);
		List<JobBo> lst1 = jobService.getjoblist();
		request.setAttribute("lst1", lst1);
		List<InternshipTypeBo> i=internshipService.getinternshiplist();
		request.setAttribute("i", i);
		request.setAttribute("filename", "Student_internship_status");
		return "internship/admin/admin_welcome";
	}
	@RequestMapping(value="/getjobfromcompanyajax")
	public void getjobfromcompanyajax(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception{
		String company_id=(String)request.getParameter("company_id");
		
		List<JobBo> lst=jobService.getjoblistfromcompany(company_id);
		Map<String, String> ind = new LinkedHashMap<String, String>();
for(int i=0;i<lst.size();i++)
{
	String job_id=Integer.toString(lst.get(i).getJob_id());
	ind.put(job_id, lst.get(i).getJob_position());
}

String json=new Gson().toJson(ind);
System.out.println(json);
response.setContentType("application/json");
response.setCharacterEncoding("UTF-8");
response.getWriter().write(json);
	}
	@RequestMapping(value="/saveInternship_status")
	public String saveInternship_status(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("AddStudentInternship") @Valid StudentInternshipBo i)throws Exception{
		log.info("-START--<<< ");
		List<CompanyBo> lst = companyService.getcompanylist();
		System.out.println(lst.get(0).getCompany_name());
		request.setAttribute("lst", lst);
		List<JobBo> lst1 = jobService.getjoblist();
		request.setAttribute("lst1", lst1);
		List<InternshipTypeBo> is=internshipService.getinternshiplist();
		request.setAttribute("i", is);
		request.setAttribute("filename", "Student_internship_status");
		System.out.println(request.getAttribute("filename"));
		commonService.saveDataComon(i);
		internshipService.updatestudent_status(i.getStudent_id(),i.getInternship_type());
		return "internship/admin/admin_welcome";
		}
	@RequestMapping(value="/saveInternship_status_company")
	public String saveInternship_status_company(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("AddStudentInternshipCompany") @Valid StudentInternshipBo i)throws Exception{
		log.info("-START--<<< ");
		i.setInternship_id(1);
		List<ApprovalStudentBo> info=studentService.getstudentapprovedlist();
		request.setAttribute("info", info);
		request.setAttribute("filename","approval_student_list");
		commonService.saveDataComon(i);
		internshipService.updatestudent_status(i.getStudent_id(),i.getInternship_type());
		return "internship/admin/admin_welcome";
		}
	
	@RequestMapping(value="/searchreport")
	public String searchreport(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("SearchReport") @Valid ReportBo r)throws Exception{
		log.info("-START--<<< ");
		 List<StudentInfoBo> list=studentService.searchreport(r);
		 request.setAttribute("list", list);
		request.setAttribute("filename", "report_student_list");
		System.out.println(request.getAttribute("filename"));
		return "internship/admin/admin_welcome";
		}
	@RequestMapping(value="/searchemployers")
	public String searchemployers(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("Searchemployers") @Valid ReportBo r)throws Exception{
		log.info("-START--<<< ");
		 List<CompanyBo> list=companyService.searchreportcompany(r);
		 request.setAttribute("employers_lst", list);
		 System.out.println(list.size());
		request.setAttribute("filename", "search_report_employers");
		System.out.println(request.getAttribute("filename"));
		return "internship/admin/admin_welcome";
		}
	@RequestMapping(value="/searchinterntype")
	public String searchinterntype(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("Searchemployers") @Valid ReportBo r)throws Exception{
		log.info("-START--<<< ");
		 List<StudentInfoBo> list=studentService.searchinterntype_student(r);
		 request.setAttribute("list_internshiiipStu", list);
		 List<InternshipTypeBo> i=internshipService.getinternshiplist();
			request.setAttribute("i", i);
		 System.out.println(list.size());
		request.setAttribute("filename", "search_internship_type");
		System.out.println(request.getAttribute("filename"));
		return "internship/admin/admin_welcome";
		}
	@RequestMapping(value="/searchgpa")
	public String searchgpa(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("Searchgpa") @Valid ReportBo r)throws Exception{
		log.info("-START--<<< ");
		List<ReportlistBo> year=reportService.getstudentyear();
		request.setAttribute("year", year);
		List<ReportlistBo> country=reportService.getstudentcountry();
		request.setAttribute("country", country);
		 List<StudentGpaBo> lst=studentService.search_gpa(r);
		 request.setAttribute("lst", lst);		
		 List<ReportlistBo> university=reportService.getuniversity();
		 request.setAttribute("university", university);
		 List<ReportlistBo> university_location=reportService.getuniversity_location();
		 request.setAttribute("university_location", university_location);
		request.setAttribute("filename", "gpa._report");
		System.out.println(request.getAttribute("filename"));
		return "internship/admin/admin_welcome";
		}
	
	@RequestMapping(value="/student_skill")
	public String student_skill(HttpServletRequest request, Model model) throws Exception{
		List<SkillBo> skill_list=skillService.getskilllist();
		request.setAttribute("skill_list", skill_list);
		request.setAttribute("filename","Student_skill");
		return "internship/student/student_welcome";
	}
	@RequestMapping(value="/saveStudentskill")
	public String saveStudentskill(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("StudentSkill") @Valid StudentSkillBo r)throws Exception{
		log.info("-START--<<< ");
		String[] shah=request.getParameterValues("student_id");
		String s="";
		for(int i=0;i<shah.length;i++)
		{
			 s= s+shah[i]+","+"";
		}
		s = s.substring(0, s.length()-1);
		System.out.println(s);
		HttpSession session = request.getSession(false);
		
		LoginBo loginbo=(LoginBo)session.getAttribute("studentDtl");
		int student_id=loginbo.getStudent_id();
		System.out.println(student_id);
		r.setSkill_name(s);
		r.setStudent_id(student_id);
		List<SkillBo> skill_list=skillService.getskilllist();
		request.setAttribute("skill_list", skill_list);
		skillService.savestudent_skill(student_id,shah);
		request.setAttribute("filename", "Student_skill");
		System.out.println(request.getAttribute("filename"));
		return "internship/student/student_welcome";
		}
	
	@RequestMapping(value="/student_basic_info")
	public String student_basic_info(HttpServletRequest request, Model model) throws Exception{
		HttpSession session = request.getSession(false);

		LoginBo loginbo=(LoginBo)session.getAttribute("studentDtl");
		int student_id=loginbo.getStudent_id();
		System.out.println(student_id);
		List<StudentInfoBo> list=studentService.getstudentlistfromid(student_id);
		request.setAttribute("list", list);
		request.setAttribute("filename","student_profile_info");
		return "internship/student/student_welcome";
	}
	
	@RequestMapping(value="/student_edu")
	public String student_edu(HttpServletRequest request, Model model) throws Exception{
		HttpSession session = request.getSession(false);

		LoginBo loginbo=(LoginBo)session.getAttribute("studentDtl");
		int student_id=loginbo.getStudent_id();
		System.out.println(student_id);
		List<StudentEducationBo> list=studentService.getstudentedulistfromid(student_id);
		request.setAttribute("list", list);
		request.setAttribute("filename","student_edu_info");
		return "internship/student/student_welcome";
	}
	
	@RequestMapping(value="/edit_education_info")
	public String edit_education_info(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("EditEdu") @Valid StudentEducationBo r)throws Exception{
		log.info("-START--<<< ");
		HttpSession session = request.getSession(false);

		LoginBo loginbo=(LoginBo)session.getAttribute("studentDtl");
		int student_id=loginbo.getStudent_id();
		int id=r.getStu_edu_id();
		r.setMajor(request.getParameter("major"+id+""));
		r.setDegree_gpa(request.getParameter("degree_gpa"+id+""));
		r.setDegree_type(request.getParameter("degree_type"+id+""));
		r.setUniversity(request.getParameter("university"+id+""));
		r.setUniversity_location(request.getParameter("university_location"+id+""));
		r.setMajor(request.getParameter("major"+id+""));

		System.out.println(r.getMajor());
		r.setStudent_id(student_id);
		studentService.updateeducationinfo(r);
		List<StudentEducationBo> list=studentService.getstudentedulistfromid(student_id);
		request.setAttribute("list", list);
		request.setAttribute("filename", "student_edu_info");
		System.out.println(request.getAttribute("filename"));
		return "internship/student/student_welcome";
		}
	@RequestMapping(value="/student_certi")
	public String student_certi(HttpServletRequest request, Model model) throws Exception{
		HttpSession session = request.getSession(false);

		LoginBo loginbo=(LoginBo)session.getAttribute("studentDtl");
		String student_id=loginbo.getStudent_id()+"";
		System.out.println(student_id);
		List<StudentCertificateBo> list=studentService.getstudentcertilistfromid(student_id);
		request.setAttribute("list", list);
		request.setAttribute("filename","student_certi_info");
		return "internship/student/student_welcome";
	}
	@RequestMapping(value="/student_work")
	public String student_work(HttpServletRequest request, Model model) throws Exception{
		HttpSession session = request.getSession(false);

		LoginBo loginbo=(LoginBo)session.getAttribute("studentDtl");
		String student_id=loginbo.getStudent_id()+"";
		List<StudentWorkBo> list=studentService.getstudentworklistfromid(student_id);
		request.setAttribute("list", list);
		request.setAttribute("filename","student_work_info");
		return "internship/student/student_welcome";
	}
	
	@RequestMapping(value="/edit_certi_info")
	public String edit_certi_info(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("EditCerti") @Valid StudentCertificateBo r)throws Exception{
		log.info("-START--<<< ");
		HttpSession session = request.getSession(false);

		LoginBo loginbo=(LoginBo)session.getAttribute("studentDtl");
		String student_id=loginbo.getStudent_id()+"";
		int id=r.getCertificate_id();
		r.setCertificate_title(request.getParameter("certificate_title"+id+""));
		r.setCertificate_body(request.getParameter("certificate_body"+id+""));
		r.setStudent_id(student_id);
		studentService.updateCERTIcationinfo(r);
		List<StudentCertificateBo> list=studentService.getstudentcertilistfromid(student_id);
		request.setAttribute("list", list);
		request.setAttribute("filename", "student_certi_info");
		System.out.println(request.getAttribute("filename"));
		return "internship/student/student_welcome";
		}
	
	@RequestMapping(value="/edit_work_info")
	public String edit_work_info(HttpServletRequest request,RedirectAttributes redirect, @ModelAttribute("EditCerti") @Valid StudentWorkBo r)throws Exception{
		log.info("-START--<<< ");
		HttpSession session = request.getSession(false);

		LoginBo loginbo=(LoginBo)session.getAttribute("studentDtl");
		int student_id=loginbo.getStudent_id();
		String student_id1=student_id+"";
		int id=r.getStu_work_id();
		r.setCompany(request.getParameter("company"+id+""));
		r.setCompany_location(request.getParameter("company_location"+id+""));
		r.setStart_date(request.getParameter("start_date"+id+""));
		r.setPosition(request.getParameter("position"+id+""));
		r.setStudent_id(student_id);
		studentService.updateworkcationinfo(r);
		//List<StudentCertificateBo> list=employeeService.getstudentcertilistfromid(student_id);
		List<StudentWorkBo> list=studentService.getstudentworklistfromid(student_id1);

		request.setAttribute("list", list);
		request.setAttribute("filename", "student_work_info");
		System.out.println(request.getAttribute("filename"));
		return "internship/student/student_welcome";
		}
	
	@RequestMapping(value="/job_report")
	public String job_report(HttpServletRequest request, Model model) throws Exception{
		List<CompanyBo> lst = companyService.getcompanylist();
		System.out.println(lst.get(0).getCompany_name());
		request.setAttribute("lst", lst);
		
		request.setAttribute("filename","job_report");
		return "internship/admin/admin_welcome";
	}
	
	@RequestMapping(value="/getjobfromcompany")
	public String getjobfromcompany(HttpServletRequest request, Model model) throws Exception{
		
		String company_id=(String)request.getParameter("company_id");
		List<JobBo> lst=jobService.getjoblistfromcompany(company_id);

		
		request.setAttribute("lst", lst);
		request.setAttribute("filename","report_joblist");
		return "internship/admin/admin_welcome";
	}
	
	@RequestMapping(value="/getstudentfromjjob")
	public String getstudentfromjjob(HttpServletRequest request, Model model) throws Exception{
		
		String job_id=(String)request.getParameter("job_id");
		List<StudentJobSkillbo> lst=skillService.getstudentfromjjob(job_id);
		HttpSession session = request.getSession(false);
		session.setAttribute("job_id", job_id);
		request.setAttribute("lst", lst);
		request.setAttribute("job_id", job_id);
		request.setAttribute("filename","student_job_approval");
		return "internship/admin/admin_welcome";
	}
	@RequestMapping(value="/job_approval")
	public String job_approval(HttpServletRequest request, Model model) throws Exception{
		
		String job_id=(String)request.getParameter("job_id");
		List<StudentJobSkillbo> lst=skillService.getstudentfromjjob(job_id);
		request.setAttribute("lst", lst);
		String student_id=(String) request.getParameter("student_id");
		jobService.updatestudentjobflag(job_id,student_id);
		System.out.println(job_id+"  "+student_id);
		request.setAttribute("filename","student_job_approval");
		return "internship/admin/admin_welcome";
	}
	@RequestMapping(value="/approved_student")
	public String approved_student(HttpServletRequest request, Model model) throws Exception{
		
		List<ApprovalStudentBo> info=studentService.getstudentapprovedlist();
		request.setAttribute("info", info);
		request.setAttribute("filename","approval_student_list");
		return "internship/admin/admin_welcome";
	}
	
	@RequestMapping(value="/insert_company_student_status")
	public String insert_company_student_status(HttpServletRequest request, Model model) throws Exception{
		
		List<ApprovalStudentBo> info=studentService.getstudentapprovedlist();
		request.setAttribute("info", info);
		String job_id=(String)request.getParameter("job_id");
		String student_id=(String)request.getParameter("student_id");
		String company_id=(String)request.getParameter("company_id");
		String company_name=(String)request.getParameter("company_name");
		String job_position=(String)request.getParameter("job_position");
		
		request.setAttribute("job_id", job_id);
		request.setAttribute("company_id", company_id);
		request.setAttribute("student_id", student_id);
		request.setAttribute("company_name", company_name);
		request.setAttribute("job_position", job_position);

		request.setAttribute("filename","student_internship_status_company");
		return "internship/admin/admin_welcome";
	}
}

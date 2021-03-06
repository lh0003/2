package org.fkit.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.fkit.domain.User;
import org.fkit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 处理用户请求控制器
 * */
@Controller
public class UserController {
	
	/**
	 * 自动注入UserService
	 * */
	@Autowired
	@Qualifier("userService")
	private UserService userService;

	/**
	 * 处理/login请求
	 * */
	@RequestMapping(value="/login")
	 public ModelAndView login(
			 String loginname,String password,
			 ModelAndView mv,
			 HttpSession session,HttpServletRequest request){
		// 根据登录名和密码查找用户，判断用户登录
		request.getSession().setAttribute("numb",0);
		User user = userService.login(loginname, password);
		if(user != null){
			// 登录成功，将user对象设置到HttpSession作用范围域
			session.setAttribute("user", user);
			request.getSession().setAttribute("numb",user.getId());

			// 转发到main请求
			mv.setView(new RedirectView("./main"));
		}else{
			// 登录失败，设置失败提示信息，并跳转到登录页面
			mv.addObject("message", "登录名或密码错误，请重新输入!");
			mv.setViewName("loginForm");
		}
		return mv;
	}
	

	@RequestMapping(value="/save")
	public ModelAndView save(
	@ModelAttribute User user,
		ModelAndView mv,
		HttpSession session,String loginname,String password,String username,String phone,String address){
		User count = userService.save(loginname, password, username, phone, address);
		mv.addObject("message","注册成功！");
		mv.setViewName("loginForm");
		return mv;
	}
	@RequestMapping(value="/update")
	public ModelAndView update(
			@ModelAttribute User user,
		ModelAndView mv,
		HttpSession session){
		
	
		int count=userService.updateUser(user);
		mv.addObject("message","注册成功！");
		mv.setViewName("loginForm");
	
		return mv;
}
	@RequestMapping(value="/forget",method = RequestMethod.POST)
	public ModelAndView find(
		String loginname,String email,
		ModelAndView mv,
		HttpSession session,HttpServletRequest request,HttpServletResponse response)throws Exception{	
			
	    User user=userService.find(loginname,email);

		if(user!=null){
			
			StringBuffer url = new StringBuffer();
			StringBuilder builder = new StringBuilder();
			// 正文
			builder.append(
					"<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /></head><body>");
			url.append("<font color='red'>" + user + "</font>");
			builder.append("<br/><br/>");
			builder.append("<div>" + url + "</div>");
			builder.append("</body></html>");
			SimpleEmail sendemail = new SimpleEmail();
			sendemail.setHostName("smtp.163.com ");// 指定要使用的邮件服务器
			sendemail.setAuthentication("lh00003@163.com", "lh1003");// 使用163的邮件服务器需提供在163已注册的用户名、密码
			sendemail.setCharset("UTF-8");
			try {
				sendemail.setCharset("UTF-8");
				sendemail.addTo(email);
				sendemail.setFrom("lh00003@163.com");
				sendemail.setSubject("找回密码");
				sendemail.setMsg(builder.toString());
				sendemail.send();
				System.out.println(builder.toString());
			} catch (EmailException e) {
				e.printStackTrace();
			}
			response.getWriter().println("你的密码为已成功发送到邮箱");	
			mv.setViewName("login");
		}else{
			response.getWriter().println("获取密码失败");
		}
	    return null;
	}

//	
//	@RequestMapping(value="/forget")
//	public String forget(String loginname,String email,HttpServletRequest request){
//		try{
//			 Properties props = new Properties();  
//		        props.put("username1", "lyf1424944719@126.com");   
//		        props.put("password1", "liyanfang123");   
//		        props.put("mail.transport.protocol", "smtp" );  
//		        props.put("mail.smtp.host", "smtp.126.com ");  
//		        props.put("mail.smtp.port", "25" );
//		        Session mailSession = Session.getDefaultInstance(props);
//		        Message msg = new MimeMessage(mailSession);     
//		        msg.setFrom(new InternetAddress("lyf1424944719@126.com"));  
//		        msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); 
//		        msg.setSubject("找回密码");   
//		        msg.setContent("<h1>此邮件为官方邮件！请点击下面链接完成找回密码操作！</h1><h3><a href='http://localhost:8090/homework/getPassword.action?loginname="+loginname+"'>http://localhost:8080/SendMail/servlet/GetPasswordServlet</a></h3>","text/html;charset=UTF-8");
//		        //msg.setContent("<h1>此邮件为官方邮件！请点击下面的链接完成找回密码的操作！</h1><h3><a href='http://localhost:80' ></h3>","text/html;charset=UTF-8");
//		        msg.saveChanges();  
//		        
//		        Transport transport = mailSession.getTransport("smtp");  
//		        transport.connect(props.getProperty("mail.smtp.host"), props  
//		                .getProperty("username1"), props.getProperty("password1"));   
//		        transport.sendMessage(msg, msg.getAllRecipients());  
//		        transport.close();     
//		        request.setAttribute("information", "找回密码成功，请登录");
//			
//		}catch(Exception e){
//			
//		}
//		return "redirect:/success";
//	}
//	@RequestMapping(value="/getPassword")
//	
//		public ModelAndView getPassword(String loginname,HttpServletRequest request,HttpSession session,ModelAndView mv){
//			Random random=new Random();
//			String password=random.nextInt(9000)+1000+"";
//			session.setAttribute("randomPassword", password);
//			userService.updateUserPassword(loginname, password);
//			mv.setViewName("getUserPassword");
//			return mv;
//		}

	
	}


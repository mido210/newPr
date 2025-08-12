package com.example;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

// 로그인 로직
@WebServlet("/summit/login")
public class Login extends HttpServlet {
	private static List<Company> companyList = CompanySignUP.getCompanyList();
	private static List<Client> clientList = ClientSignUp.getClientList();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		// Thymeleaf를 사용해 companySignUp.html 렌더링
		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

		TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
		templateEngine.process("login", ctx, response.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		// 로그인 인증
		boolean authenticated = false;
		Object loginUser = null; // 고객이든 사업자든 담을 수 있는 객체

		// 사업자 검증
		for (Company com : companyList) {
			if (com.getId().equals(id) && com.getPassword().equals(password)) {
				authenticated = true;
				loginUser = com;
				break;
			}
		}
		// 고객 검증
		if (!authenticated) {
			for (Client cl : clientList) {
				if (cl.getId().equals(id) && cl.getPassword().equals(password)) {
					authenticated = true;
					loginUser = cl;
					break;
				}
			}
		}

		if (authenticated) {
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", loginUser);
			response.setContentType("text/html;charset=UTF-8");
			WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
			ctx.setVariable("user", loginUser);

			TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
			templateEngine.process("index", ctx, response.getWriter());
		} else {
			request.setAttribute("error", "아이디 또는 비밀번호가 틀렸습니다");
			response.setContentType("text/html;charset=UTF-8");
			WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
			ctx.setVariable("error", "아이디 또는 비밀번호가 틀렸습니다");

			TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
			templateEngine.process("login", ctx, response.getWriter());
		}
	}

}

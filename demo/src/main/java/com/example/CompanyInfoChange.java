package com.example;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

// 사업자 정보 변경 
@WebServlet("/summit/infoChange")
public class CompanyInfoChange extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");

		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		// 비밀번호 변경 폼 렌더링
		templateEngine.process("companyInfoChange", ctx, response.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("loginUser") == null) {
			WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
			ctx.setVariable("errorMsg", "로그인이 필요합니다.");
			templateEngine.process("login", ctx, response.getWriter());
			return;
		}

		Object loginUser = session.getAttribute("loginUser");
		if(!(loginUser instanceof Company)){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 타입");
		}

		String id = request.getParameter("id");
		String name = request.getParameter("companyName");
		String address = request.getParameter("address");
		String email = request.getParameter("email");

		Company company = (Company) loginUser;

		company.setId(id);
		company.setName(name);
		company.setAddress(address);
		company.setEmail(email);

		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		ctx.setVariable("company", company);
		templateEngine.process("companyMypage", ctx, response.getWriter());
	}
}

package com.example;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebFilter("/company/delete")
public class CompanyDelete extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = getServletContext();
		ArrayList<Company> companys = (ArrayList<Company>) context.getAttribute("companys");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");

		HttpSession session = request.getSession(false);

		// 세션 및 로그인 체크
		if (session == null || session.getAttribute("loginUser") == null) {
			WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
			ctx.setVariable("errorMsg", "로그인이 필요합니다.");
			templateEngine.process("login", ctx, response.getWriter());
			return;
		}

		Object loginUser = session.getAttribute("loginUser");

		// 타입 체크
		if (!(loginUser instanceof Company)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 타입");
			return; // 반드시 return 필요
		}
		// 로그인 객체의 변수 생성
		Company company = (Company) loginUser;
		String companyId = company.getId();

		// companys 리스트에서 해당 company 삭제
		if (companys != null) {
			boolean removed = false;
			for (int i = 0; i < companys.size(); i++) {
				if (companys.get(i).getId().equals(companyId)) {
					companys.remove(i);
					removed = true;
					break;
				}
			}

			if (removed) {
				// 세션 무효화 (탈퇴했으니 자동 로그아웃)
				session.invalidate();

				response.sendRedirect(request.getContextPath() + "/index.html");
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "해당 회원을 찾을 수 없습니다.");
			}
		} else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "회원 리스트가 존재하지 않습니다.");
		}
	}
}

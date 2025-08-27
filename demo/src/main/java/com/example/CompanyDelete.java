package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/company/delete")
public class CompanyDelete extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        ServletContext context = getServletContext();

        // 회원 리스트 가져오기
        Object obj = context.getAttribute("companys");
        if (obj == null || !(obj instanceof ArrayList<?>)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "회원 리스트가 존재하지 않습니다.");
            return;
        }

        @SuppressWarnings("unchecked")
        ArrayList<Company> companys = (ArrayList<Company>) obj;

        // 템플릿 엔진 확인
        TemplateEngine templateEngine = (TemplateEngine) context.getAttribute("templateEngine");
        if (templateEngine == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "템플릿 엔진이 초기화되지 않았습니다.");
            return;
        }

        // 세션 확인
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            WebContext ctx = new WebContext(request, response, context, request.getLocale());
            ctx.setVariable("errorMsg", "로그인이 필요합니다.");
            templateEngine.process("login", ctx, response.getWriter());
            return;
        }

        Object loginUser = session.getAttribute("loginUser");
        if (!(loginUser instanceof Company)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 타입");
            return;
        }

        Company company = (Company) loginUser;
        String companyId = company.getId();
        if (companyId == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "회원 ID가 존재하지 않습니다.");
            return;
        }

        // 회사 삭제
        boolean removed = false;
        for (int i = 0; i < companys.size(); i++) {
            if (Objects.equals(companyId, companys.get(i).getId())) {
                companys.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            // 세션 무효화
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/index.html");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "해당 회원을 찾을 수 없습니다.");
        }
    }
}

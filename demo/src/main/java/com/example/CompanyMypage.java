package com.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/companyMyPage") // URL 경로
public class CompanyMypage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 세션에서 로그인 사용자 가져오기
        Object loginUser = req.getSession().getAttribute("loginUser");

        // 로그인 안 했거나 사업자가 아닌 경우 → 로그인 페이지 렌더링
        if (!(loginUser instanceof Company)) {
            resp.setContentType("text/html;charset=UTF-8");
            WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
            ctx.setVariable("errorMessage", "로그인이 필요합니다.");

            TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
            templateEngine.process("login", ctx, resp.getWriter());
            return;
        }

        // 로그인한 사용자가 Company인 경우
        Company company = (Company) loginUser;

        resp.setContentType("text/html;charset=UTF-8");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("company", company);

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        templateEngine.process("companyMyPage", ctx, resp.getWriter());
    }
}

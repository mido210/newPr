package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/account/find-id")
public class FindIdServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        resp.setContentType("text/html;charset=UTF-8");

        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        // GET에서는 아직 검색 전
        ctx.setVariable("searched", false);

        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        engine.process("findId", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String email = req.getParameter("email");
        List<Result> results = new ArrayList<>();

        // 1) 고객 리스트에서 탐색
        for (Client c : ClientSignUp.getClientList()) {
            if (email != null && email.equalsIgnoreCase(c.getEmail())) {
                results.add(new Result("CLIENT", c.getId()));
            }
        }

        // 2) 사업자 리스트에서 탐색
        for (Company co : CompanySignUP.getCompanyList()) {
            if (email != null && email.equalsIgnoreCase(co.getEmail())) {
                results.add(new Result("COMPANY", co.getId()));
            }
        }

        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("searched", true);
        ctx.setVariable("results", results);

        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        engine.process("findId", ctx, resp.getWriter());
    }

    // 화면에 넘길  DTO
    public static class Result {
        public final String role;
        public final String id;
        public Result(String role, String id) {
            this.role = role;
            this.id = id;
        }
        public String getRole() { return role; }
        public String getId() { return id; }
    }
}

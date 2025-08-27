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
public class FindIdServlet extends HttpServlet 
{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException 
    {

        resp.setContentType("text/html;charset=UTF-8");

        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("searched", false); // GET에서는 아직 검색 전
        
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        engine.process("findId", ctx, resp.getWriter());


            /*
            setVariable() : 템플릿(HTML)에서 사용할 변수(name=value 쌍)를 등록하는 메서드.
            getLocale()   : 요청자의 언어 및 지역(Locale) 정보 가져오기
            getLocale()   : 템플릿 + 변수 --> 최종 HTML로 생성
            process()     : 브라우저에 보낼 문자 스트림(출력 통로) 꺼내기
                            쉽게 말해, 브라우저에게 보낼 문자를 적을 종이와 펜을 꺼내는 것
            */

    }




    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException 
    {

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
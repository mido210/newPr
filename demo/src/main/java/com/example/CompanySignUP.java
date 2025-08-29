package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/summit/company")
public class CompanySignUP extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static List<Company> companyList = new ArrayList<>();

    private static final Pattern BUSINESS_NUMBER_PATTERN = Pattern.compile("^\\d{3}-\\d{3}-\\d{3}$");
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-z0-9]{3,19}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-z0-9]{3,19}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         response.setContentType("text/html;charset=UTF-8");
        // Thymeleaf를 사용해 companySignUp.html 렌더링
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        templateEngine.process("companySignUp", ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String companyName = request.getParameter("companyName");
        String businessNumber = request.getParameter("businessNumber");
        String address = request.getParameter("address");

        // 유효성 검사
        if (id == null || !ID_PATTERN.matcher(id).matches()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "아이디 형식이 올바르지 않습니다.");
            return;
        }

        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다.");
            return;
        }

        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "이메일 형식이 올바르지 않습니다.");
            return;
        }

        if (companyName == null || companyName.trim().isEmpty() ||
            businessNumber == null || businessNumber.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 정보가 누락되었습니다.");
            return;
        }

        Matcher matcher = BUSINESS_NUMBER_PATTERN.matcher(businessNumber);
        if (!matcher.matches()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "사업자 번호 형식이 올바르지 않습니다. (예: 123-456-789)");
            return;
        }

        // Company 객체 생성 후 리스트에 추가
        Company newCompany = new Company(id, password, companyName, businessNumber, address, email, Role.COMPANY);
        companyList.add(newCompany);

        System.out.println("현재 등록된 회사 리스트: " + companyList);

        // 회원가입 완료 후 index.html로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/index.html");
    }

        // 전역 리스트로 보내주는 역할, 없으면 못 찾음
    public static List<Company> getCompanyList() {
        return companyList;
    }
}

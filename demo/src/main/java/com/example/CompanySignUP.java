package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/summit/company")
public class CompanySignUP extends HttpServlet {
	private static List<Company> companyList = new ArrayList<>();


    // 사업자 번호 정규식: 3자리-2자리-5자리
    private static final Pattern BUSINESS_NUMBER_PATTERN = Pattern.compile("^\\d{3}-\\d{3}-\\d{3}$");
    
    // 아이디 정규식: 4~20자, 영문 소문자/숫자, 첫 글자 영문
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-z0-9]{3,19}$");

    // 비밀번호 정규식: 8~20자, 최소 1개 대/소문자, 숫자, 특수문자 포함
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-z0-9]{3,19}$");

    // 이메일 정규식
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");


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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "사업자 번호 형식이 올바르지 않습니다. (예: 123-45-67890)");
            return;
        }

        // 모든 검증 통과 후 객체 생성 및 저장
        Company newCompany = new Company(id, password, companyName, businessNumber, address, email);
        companyList.add(newCompany);

        request.setAttribute("registeredCompany", newCompany);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/company-result.html");
        dispatcher.forward(request, response);
    }
}
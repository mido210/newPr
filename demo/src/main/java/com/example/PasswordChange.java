package com.example;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/summit/changePassword")
public class PasswordChange extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");

        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        // 비밀번호 변경 폼 렌더링
        templateEngine.process("passwordChange", ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");

        HttpSession session = req.getSession(false);
        if (session == null) {
            WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
            ctx.setVariable("errorMsg", "로그인이 필요합니다.");
            templateEngine.process("login", ctx, resp.getWriter());
            return;
        }

        Object loginUser = session.getAttribute("loginUser");
        if (loginUser == null) {
            WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
            ctx.setVariable("errorMsg", "로그인이 필요합니다.");
            templateEngine.process("login", ctx, resp.getWriter());
            return;
        }

        String currentPasswordInput = req.getParameter("password");
        String newPassword = req.getParameter("newPassword");

        String errorMsg = null;

        // 타입에 따라 분기 처리
        if (loginUser instanceof Client) {
            Client customer = (Client) loginUser;
            if (currentPasswordInput == null || !currentPasswordInput.equals(customer.getPassword())) {
                errorMsg = "현재 비밀번호가 일치하지 않습니다.";
            } else if (!isValidPassword(newPassword)) {
                errorMsg = "새 비밀번호는 8~20자이며, 영문, 숫자, 특수문자를 포함해야 합니다.";
            }

            if (errorMsg == null) {
                customer.setPassword(newPassword);
                // TODO: DB 업데이트 처리

                // 변경 성공 시 마이페이지 렌더링
                WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
                
                ctx.setVariable("successMsg", "비밀번호가 성공적으로 변경되었습니다.");
                templateEngine.process("myPage", ctx, resp.getWriter());
                return;
            }
        } else if (loginUser instanceof Company) {
            Company company = (Company) loginUser;
            if (currentPasswordInput == null || !currentPasswordInput.equals(company.getPassword())) {
                errorMsg = "현재 비밀번호가 일치하지 않습니다.";
            } else if (!isValidPassword(newPassword)) {
                errorMsg = "새 비밀번호는 8~20자이며, 영문, 숫자, 특수문자를 포함해야 합니다.";
            }

            if (errorMsg == null) {
                company.setPassword(newPassword);
                // TODO: DB 업데이트 처리

                // 변경 성공 시 마이페이지 렌더링
                WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
                ctx.setVariable("company", company); // <-- 추가
                ctx.setVariable("successMsg", "비밀번호가 성공적으로 변경되었습니다.");
                templateEngine.process("companyMyPage", ctx, resp.getWriter());
                return;
            }
        } else {
            // 알 수 없는 타입
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
            return;
        }

        // 오류가 있을 때 다시 비밀번호 변경 폼 렌더링
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("errorMsg", errorMsg);
        templateEngine.process("passwordChange", ctx, resp.getWriter());
    }

    private boolean isValidPassword(String password) {
        if (password == null) return false;
        String regex = "^[a-z0-9]{3,19}$";
        // "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$"
        return Pattern.matches(regex, password);
    }
}

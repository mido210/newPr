package com.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 세션이 있으면 가져오고, 없으면 null 반환
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화(로그아웃 처리)
        }

        // 로그아웃 후 리다이렉트 (예: 로그인 페이지 또는 홈)
        response.sendRedirect(request.getContextPath() + "/index.html");
    }
}

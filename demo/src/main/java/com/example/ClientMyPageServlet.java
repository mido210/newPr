package com.example;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/client/mypage")
public class ClientMyPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            // 비로그인 → 로그인 페이지로
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

        Object loginUserObj = session.getAttribute("loginUser");

        // loginUser가 Client 타입인지 확인
        if (!(loginUserObj instanceof Client)) {
            resp.sendRedirect(req.getContextPath() + "/index");
            return;
        }

        Client loginUser = (Client) loginUserObj;

        // role이 CLIENT인지 확인
        if (loginUser.getRole() != Role.CLIENT) {
            resp.sendRedirect(req.getContextPath() + "/index");
            return;
        }

        // 페이지 렌더링
        resp.setContentType("text/html;charset=UTF-8");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");

        engine.process("clientMyPage", ctx, resp.getWriter());
    }
}

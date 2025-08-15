// 고객 마이페이지의 실제 이름과 이메일 변경 (POST)
// 수정 폼 표시 (GET)

package com.example;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/client/edit")
public class ClientEditServlet extends HttpServlet {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Object user = (session == null) ? null : session.getAttribute("loginUser");
        if (!(user instanceof Client)) {
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

        resp.setContentType("text/html;charset=UTF-8");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        engine.process("clientEdit", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        Object user = (session == null) ? null : session.getAttribute("loginUser");
        if (!(user instanceof Client)) {
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

        Client loginClient = (Client) user;

        String newName  = req.getParameter("name");
        String newEmail = req.getParameter("email");

        // 간단 검증
        if (newName == null || newName.trim().isEmpty()) {
            sendError(req, resp, "이름을 입력해주세요.");
            return;
        }
        if (newEmail == null || !EMAIL_PATTERN.matcher(newEmail).matches()) {
            sendError(req, resp, "올바른 이메일 형식이 아닙니다.");
            return;
        }

        // 1) 세션 객체 갱신
        loginClient.setName(newName.trim());
        loginClient.setEmail(newEmail.trim());

        // 2) 메모리 리스트(ClientSignUp) 갱신
        List<Client> list = ClientSignUp.getClientList();
        for (Client c : list) {
            if (c.getId().equals(loginClient.getId())) {
                c.setName(loginClient.getName());
                c.setEmail(loginClient.getEmail());
                break;
            }
        }

        // 완료 후 마이페이지로
        resp.sendRedirect(req.getContextPath() + "/client/mypage");
    }

    private void sendError(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("errorMsg", msg);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        engine.process("clientEdit", ctx, resp.getWriter());
    }
}

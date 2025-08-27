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
        // 1. 세션에서 로그인 정보 확인
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            // 비로그인 → 로그인 페이지로
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

        // 2. 로그인한 사용자가 고객(client)인지 아닌지 확인하기
        Object loginUserObj = session.getAttribute("loginUser");

        // loginUser가 Client 타입인지 확인
        if (!(loginUserObj instanceof Client)) {
            resp.sendRedirect(req.getContextPath() + "/index");
            return;
        }
        // getAttribute() : 저장된 값을 key 값으로 꺼내오는 기능
        // instanceof : 객체가 특정 클래스의 인스턴스인지 확인하는 연산자
        // sendRedirect() : 브라우저에게 지정된 URL로 새로운 요청을(리다이렉트 요청을) 보내는 메소드
        // req.getContextPath() : 지정한 url 경로로 이동

        // 3. object 타입으로 들어온 사용자의 타입을 client로 형변환하기
        // loginUser를 Object 타입에서 Client 타입으로 형변환(캐스팅)
        Client loginUser = (Client) loginUserObj;

        // role이 CLIENT인지 확인
        if (loginUser.getRole() != Role.CLIENT) {
            resp.sendRedirect(req.getContextPath() + "/index");
            return;
        }

        // 4. 페이지 렌더링(사용자에게 마이페이지 보여주기 / 뷰 렌더링)
        resp.setContentType("text/html;charset=UTF-8");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");

        engine.process("clientMyPage", ctx, resp.getWriter());
    }
}

//esp.getWriter()

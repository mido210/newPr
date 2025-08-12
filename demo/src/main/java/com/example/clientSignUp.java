package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/summit/client")
public class ClientSignUp extends HttpServlet {

    private static List<Client> clientList = new ArrayList<>();

    private static final Pattern ID_PATTERN = Pattern.compile("^[a-z0-9]{4,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");

      @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         response.setContentType("text/html;charset=UTF-8");
        // Thymeleaf를 사용해 companySignUp.html 렌더링
        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        templateEngine.process("client", ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        if (id == null || !ID_PATTERN.matcher(id).matches()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "아이디 형식이 올바르지 않습니다.");
            return;
        }

        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "비밀번호는 최소 8자, 숫자/문자 포함이어야 합니다.");
            return;
        }

        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "이메일 형식이 올바르지 않습니다.");
            return;
        }

        // 아이디 중복 확인
        for (Client c : clientList) {
            if (c.getId().equals(id)) {
                response.sendError(HttpServletResponse.SC_CONFLICT, "이미 존재하는 아이디입니다.");
                return;
            }
        }

        Client newClient = new Client(id, password, name, email, Role.CLIENT);
        clientList.add(newClient);

        request.setAttribute("registeredClient", newClient);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");
        dispatcher.forward(request, response);
    }

    public static List<Client> getClientList() {
        return clientList;
      }
}

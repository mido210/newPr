package com.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        // 1. 템플릿 리졸버 생성 - WEB-INF/views/ 경로 지정
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);

        // 2. 템플릿 엔진에 리졸버 연결
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 3. 타임리프 렌더링용 WebContext 생성
        WebContext context = new WebContext(request, response, getServletContext(), request.getLocale());

        // 4. 필요시 데이터 넣기
        context.setVariable("message", "Hello Thymeleaf with Servlet!");

        response.setContentType("text/html;charset=UTF-8");

        // 5. 템플릿 렌더링 - /WEB-INF/views/index.html
        templateEngine.process("index", context, response.getWriter());
    }
}

// 상품 조회 서블릿
package com.example;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/client/productList")
public class ProductListServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
            // ■1. 세션 확인
        HttpSession session = req.getSession(false);
        Object loginUser = (session == null) ? null : session.getAttribute("loginUser");
      

             // ■2. 로그인 여부 + 고객(Client) 여부 확인     
        if(!(loginUser instanceof Client))
        {
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

            // ■3. 상품 리스트 가져오기
        @SuppressWarnings("unchecked")
        List<Product> products = (List<Product>) getServletContext().getAttribute("productList");


            // ■4. 타임리프 설정 및 응답
        resp.setContentType("text/html;charset=UTF-8");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("products", products);

            // ■5. 타임리프 템플릿 엔진 가져와서 이걸로 productList.html 렌더링하기
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        templateEngine.process("productList", ctx, resp.getWriter());
    }

}

/*

■1. 세션 확인

    1) HttpSession session = req.getSession(false);

        HttpSession으로 session 변수를 만들고 
        현재 요청(req)에서 세션을 가져와 session에 저장해라.
        대신, 세션이 없으면 새로 만들지 마라(false).

        즉, 이미 세션이 있으면 그걸 쓰고 없으면 null을 저장해라.


    2) Object loginUser = (session == null) ? null : session.getAttribute("loginUser");

        loginUser 변수를 Object 타입으로 만들어 Client, Company 둘다 받을 수 있게 만들어라.

        만약 session이 null이라면 loginUser에 null을 저장하고
        아니라면 loginUser의 키 값에 해당되는 값을 loginUser에 저장해라.



■2. 로그인 여부 + 고객(Client) 여부 확인 

    1) 조건식

        1. if(!(loginUser instanceof Client))
          
            instanceof로 loginuser의 타입을 확인해라.
            만약 Client 타입이 아니라면

            즉, 고객이 아니라면
        

        2. resp.sendRedirect(req.getContextPath() + "/summit/login");

            sendRedirect()로 브라우저에 다음과 같은 정보를 보내라.
            getContextPath()로 지정된 현재의 경로는 /summit/login이라고.

            즉, 고객 아니면 강제로 로그인 페이지로 이동시켜라.



■3. 상품 리스트 가져오기

    1) List<Product> products = (List<Product>) getServletContext().getAttribute("productList");

        상품 등록 서블릿에서 만든 list를 가져오고 produsts에 저장한다.
        그 products에 '상품 등록 서블릿'에서 등록한 상품 내용들을 전부 products에 담아라. 


■4. 타임리프 설정 및 응답

    1) resp.setContentType("text/html;charset=UTF-8");

        resp로 다음 내용을 서블릿 응답으로 보내라.
        우리가 보낼 문서는 html이고 해당 문서는 한글이다.   
           
           
    2) WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());

        타임리프에서 WebContext를 가져오고 cts에 저장해라. 
        ctx에 서블릿 응답, 요청, 웹의 모든 정보, 사용자의 언어 정보를 담아라.


    3) ctx.setVariable("products", products);

        ctx에 html에서 사용할 변수를 추가해라.
        html에서 ${products}로 접근할 수 있게 해라.



■5. 타임리프 템플릿 엔진 가져와서 이걸로 productList.html 렌더링하기

    1) TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
            
        템플릿 처리기를 templateEngine에 담아라.
        공유 저장소에 저장된 templateEngine을 가져와서 templateEngine에 담아라.    
    
    
    2) templateEngine.process("productList", ctx, resp.getWriter());

        /WEB-INF/views/productList.html 파일을 불러와서
        cts의 내용들을 적용하고
        최종 html 코드를 생성해라.
        그리고 그 html 코드를 resp로 응답해라.


        


resp.setContentType()             : 브라우저에게 "내가 지금 보내는 응답이 어떤 형식인지" 알려주는 기능
 

getServletContext()               : 웹 애플리케이션 전체에서 공유되는 저장소(Context)를 의미합니다.

                                    setAttribute("productList", someList) 로 저장해둔 것을
                                    getAttribute("productList") 로 꺼내오는 거예요.


getServletContext().getAttribute() :  웹 전체 공유 저장소에서 값 꺼내기   


setVariable("products", products)  : 템플릿(HTML)에서 사용할 변수를 등록하는 메소드
                                    
                                     이 명령어를 쓰면 html에서 ${products}로 접근 가능



WebContext
 - 타임리프에서 사용할 데이터 컨테이너
 - 템블릿(HTML)에서 사용할 변수들을 담고 있는 객체
 - html에 데이터 전달용 상자


TemplateEngine
 - 타임리프의 핵심 엔진
 - 템플릿 처리기(HTML 생성기)


 */

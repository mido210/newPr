// 장바구니 페이지 보여주기 + 상품 추가 파일
// 상품 개수 조작 X

package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/client/cart")
public class CartServlet extends HttpServlet 
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
            // ■1. 로그인 확인
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null)
        {
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

            // ■2. 로그인 유저가 Client인지 확인
        Object loginUser = session.getAttribute("loginUser");
        if (!(loginUser instanceof Client)) 
        {
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

            // ■3. 장바구니 가져오기 (없으면 빈 리스트 생성)
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) 
        {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

            // ■4. 선택된 상품 총액 계산
        int selectedTotal = 0;
        for (CartItem item : cart) 
        {
            if (item.isSelected())  { selectedTotal += item.getTotalPrice(); }
        }

            // ■5. 디버깅 로그 출력
        System.out.println("==== [CartServlet Debug] ====");
        System.out.println("장바구니 아이템 개수: " + cart.size());
        for (int i = 0; i < cart.size(); i++) 
        {
            CartItem ci = cart.get(i);
            System.out.println("[" + i + "] 상품명=" + ci.getProduct().getProductName() +
                               ", 수량=" + ci.getCount() +
                               ", 단가=" + ci.getProduct().getPrice() +
                               ", 선택됨=" + ci.isSelected() +
                               ", 총액=" + ci.getTotalPrice());
        }
        System.out.println("선택된 총액 = " + selectedTotal);
        System.out.println("============================");

            // ■6. 타임리프 렌더링
        resp.setContentType("text/html;charset=UTF-8");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("cart", cart);
        ctx.setVariable("selectedTotal", selectedTotal);

        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        engine.process("cart", ctx, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
        req.setCharacterEncoding("UTF-8");

            // ■7. 세션 확인
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) 
        {
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

            // ■8. 장바구니 가져오기
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) 
        {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

            // ■9. 요청 파라미터(상품 정보) 받기
        String productName = req.getParameter("productName");
        int price = Integer.parseInt(req.getParameter("price"));
        int count = Integer.parseInt(req.getParameter("count"));
        String profilePath = req.getParameter("profilePath");

            // ■10. 상품 추가
        Product p = new Product(productName, count, price, profilePath);
        CartItem item = new CartItem(p, count);
        cart.add(item);

            // ■11. 디버깅 로그 출력
        System.out.println("==== [CartServlet Debug] POST ====");
        System.out.println("추가된 상품명=" + productName +
                           ", 수량=" + count +
                           ", 단가=" + price +
                           ", 이미지=" + profilePath);
        System.out.println("현재 장바구니 크기 = " + cart.size());
        System.out.println("============================");

            // ■12. 다시 장바구니 화면으로 리다이렉트
        resp.sendRedirect(req.getContextPath() + "/client/cart");
    }
}


/*

■3과 ■6의 형식은 같아야 한다.
■6-5가 ■7 아래에 있어야만 한다.



 < doGet >



■1. 세션 확인하기
    
     1) HttpSession session = req.getSession(false);

        - HttpSession으로 session 변수를 만들고 여기에 세션 받을걸 
        - 로그인 여부를 확인~~~~~~

     2) if (session == null || session.getAttribute("loginUser") == null)

        - 만약 session 값이 null이거나 loginUser 값이 null이라면
        - 로그인 한 적 없거나 ~~~~~~~

     3) resp.sendRedirect(req.getContextPath() + "/summit/login");

        - sendRedirect()로 브라우저에게 요청(req)해라, getContextPath()를 이용해 /summit/login으로 이동하라고



■2. 로그인 확인

      1) Object loginUser = session.getAttribute("loginUser");

        - Object로 loginUser 변수를 만들고 여기에 loginUser값을 받아라
        - Object는 java 최상위 객체라 Client, Company 다 받는다.
        - getAttribute()로 해당 session 값의 타입을 확인한다.

       2) if (!(loginUser instanceof Client)) 
      
        - 만약 loginUser의 타입을 instanceof로 확인했는데 Client가 아니라면

       3) resp.sendRedirect(req.getContextPath() + "/summit/login");
 
        - 다음과 같이 응답을 보내라. getContextPath()를 이용해 /summit/login로 이동시키라고



■3. 장바구니 생성 및 조회 (없으면 빈 리스트 생성)

      1) @SuppressWarnings("unchecked")
         - @SuppressWarnings()
            * 이건 애너테이션이다.
            * 자바 컴파일러에게 '특정 경고는 무시해라'라고 명령하는 거다.

         - unchecked
            * 제네릭 형변환을 할 때 '형 안정성 보장 안 된다'는 경고가 뜨는데 이거 무시한다는 거다. 

         - 결론
            * 컴파일러야, 이 코드 내가 책임질 테니 제네릭 형변환(타입 체크) 경고는 무시해라


      2) List<Product> cart = (List<Product>) session.getAttribute("cart");
        - List<Product> cart
          * 해당 리스트 안에는 Product 객체만 들어갈 수 있게 한다.
          * 그 리스트의 변수는 cart다.

        -  (List<Product>) 
          * 반환된 Object를 List<Product> 타입으로 강제 형변환(casting)한다.
          * 이건 "Product 객체가 담긴 List일 거야"라고 알려주는 것.

        - session.getAttribute("cart")
          * 세션에 "cart"라는 이름(key)로 저장된 객체를 꺼내라
          * 반환 타입은 Object

	    - 결론
          * 세션에서 cart라는 이름으로 저장된 데이터를 꺼내 Prduct 리스트로 쓰고 
            그걸 cart 변수에 담아라


             : <Product> 리스트 안에 Product 타입 객체만 넣을 수 있다는 "제네릭" 표시입니다.


      3) if (cart == null) 

         - 만약 cart가 null 값이라면
         - 해당 세션에 장바구니가 아직 없다면


      4) cart = new ArrayList<>();

         - 새로운 빈 리스트(장바구니)를 만들어라.11


      5) session.setAttribute("cart", cart);
        
         - session.setAttribute()는 '세션에 데이터 저장' 메서드다.
         - "cart" → key(문자열). 세션에 저장될 이름표
         -  cart   → value(실제 객체, List<Product>)
       


■4. 선택된 상품 총액 계산
        int selectedTotal = 0;
        for (CartItem item : cart) 
        {
            if (item.isSelected())  { selectedTotal += item.getTotalPrice(); }
        }



        1) for문

            - 장바구니에 있는 항목들 하나 하나 for문으로 다 꺼내라. 

        2) if 문
            
            - 만약 사용자가 선택한 항목인 item.isSelected()라면  
              총액(selectedTotal)에 더해라. 
            - item.getTotalPrice()는 가격 * 개수를 반환

        3) 기타

            - 아래 2개의 메서드가 CartItem.java에 없으면 
              CartServlet에서 호출시 오류 발생
              * isSelected()
              * getTotalPrice()


■5. 디버깅 로그 출력







■6. 타임리프 렌더링

      지금까지 만든 내용들을 html에 보내는 기능

      1) resp.setContentType("text/html;charset=UTF-8");

          - setContentType()를 이용해 브라우저에게 다음과 같이 전달해라.
          - 이 문서는 html이고 UTF-8로 읽어야 한다(한글 깨지지 않게)
 
       2) WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());

          - 타임리프에서 WebContext라는 템플릿 엔진을 가져와 ctx 변수를 만든다.
          - WebContext는 타임리프에서 HTML을 랜더링할 때 필요한 데이터 상자다.
          - ctx에 응답, 요청, 해당 웹 페이지의 모든 정보, 사용자 언어 정보를 넣어라.

       3) ctx.setVariable("cart", cart);

          - html 템플릿에서 쓸 변수를 등록하는 기능
          - "cart" -> 템플릿에서 접근할 이름
          - cart    -> 실제 자바 객체(장바구니 리스트)

          - html에선 ${cart} 또는 <tr th:each="p : ${cart}">로 접근 가능
          - java 데이터를 html로 연결

       4) TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        
          - 타임리프에서 TemplateEngine를 가져와 engine을 만들어라.
          - TemplateEngine은 렌더링 엔진.
          - engine에 아래의 데이터들을 넣어라.
             * getServletContext()  -> 웹 전체에서 공유되는 저장소
             * .getAttribute("templateEngine") → 여기에 templateEngine이라는 이름으로 저장된 객체 꺼내기.
             * (TemplateEngine) → TemplateEngine 타입으로 형변환.
   
          - 결론
            : 타임리프 HTML 변환기를 가져와서 engine이라는 이름으로 쓰겠다.

       5) engine.process("cart", ctx, resp.getWriter());
 
          - "cart"라는 이름의 템플릿(cart.html)을 불러온다.
          - 그 템플릿 안에 ctx의 변수(cart 리스트 등)를 적용한다.
          - 결과 HTML을 resp.getWriter()를 통해 브라우저에 출력한다.
          - cart.html 파일을 실제 화면용 HTML 코드로 변환해서 사용자에게 보여줘"


          


 < doPost >

■9. 요청 파라미터(상품정보) 받기

        String productName = req.getParameter("productName");
        int price = Integer.parseInt(req.getParameter("price"));
        int count = Integer.parseInt(req.getParameter("count"));
        String profilePath = req.getParameter("profilePath");


   - req.getParameter("...")
       ==> HTML form에서 넘어온 값 꺼내기.
   - Integer.parseInt(...) 
       ==> 문자열을 숫자로 변환.
   - 상품 이름, 가격, 개수, 이미지 경로를 클라이언트 요청에서 꺼내와라.



■12. 다시 장바구니 화면으로 리다이렉트


        resp.sendRedirect(req.getContextPath() + "/client/cart");
    
     - 다시 doGet() 실행되도록 리다이렉트.
     - 즉, 방금 담은 상품이 보이도록 장바구니 페이지 새로고침.

 */
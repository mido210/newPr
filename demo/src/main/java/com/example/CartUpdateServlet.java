/*  장바구니에 들어있는 상품 개수 조작

 이거 없으면 html만 조작되서 실제로는 안 바뀌는데 사용자 눈에는 개수가 바뀐 것 처럼 보임*/
package com.example;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/client/cart/update")
public class CartUpdateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) 
        {
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) 
        {
            resp.sendRedirect(req.getContextPath() + "/client/cart");
            return;
        }

        // 📌 파라미터 받기
        int index = Integer.parseInt(req.getParameter("index"));   // 몇 번째 상품인지
        int newCount = Integer.parseInt(req.getParameter("count")); // 새로운 수량

        if (index >= 0 && index < cart.size() && newCount > 0) 
        {
            cart.get(index).setCount(newCount); // 해당 상품 개수 업데이트
        }

        resp.sendRedirect(req.getContextPath() + "/client/cart");
    }
}

/*
1. int index = Integer.parseInt(req.getParameter("index"));  

    몇 번째 상품인가?

  - req.getParameter("index")는 HTTP 요청(form 제출 등)에서 "index"란 이름으로 넘어온 값을 꺼내라.
  - "index"는 String으로 들어온거라 바로 못쓰니 Integer.parseInt()를 이용해 정수로 변환
  - index 변수는 장바구니 리스트에서 몇 번째 상품인지 (list index)를 나타냄


  - index는 cart.html의 다음 코드에서 넘겨준 값이다.
    <input type="hidden" name="index"h:value="${stat.index}">

  - 따라서 HTML의 name="index"와 
    서블릿의 req.getParameter("index")가 
    반드시 같아야 값이 정상적으로 넘어옵니다.
  - 

2. int newCount = Integer.parseInt(req.getParameter("count")); 

     새로운 수량

  - 위와 같은 방식으로 "count"로 넘어온 값을 나타낸다.

  - "count"도 HTML에서 <input name="count">



3. if문

 index가 유효 범위인지 확인하고 
 newCount가 0 이상일 떄만 실제 개수를 변경해라.
 

 */
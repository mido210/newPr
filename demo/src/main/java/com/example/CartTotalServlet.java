// 선택된 상품 총액 계산
package com.example;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/client/cart/total")
public class CartTotalServlet extends HttpServlet 
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
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

        // 선택된 상품의 총액 계산
        int total = 0;
        
        for (CartItem item : cart) 
        {
            if (item.isSelected()) 
            {
                total += item.getTotalPrice();
            }
        }

        // 응답에 총액만 출력 (Ajax로 호출 가능)
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().write("총액: " + total + "원");
    }
}


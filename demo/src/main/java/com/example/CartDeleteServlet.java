// 장바구니에 들은 상품 삭제 + 총액 계산

package com.example;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/client/cart/delete")
public class CartDeleteServlet extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
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

        // 삭제할 인덱스
        int index = Integer.parseInt(req.getParameter("index"));
        if (index >= 0 && index < cart.size()) 
        {
            cart.remove(index);
        }

        resp.sendRedirect(req.getContextPath() + "/client/cart");
    }
}
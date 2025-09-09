// 선택 여부 반영
package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/client/cart/select")
public class CartSelectServlet extends HttpServlet
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

        // ✅ 선택 여부 반영
        int index = Integer.parseInt(req.getParameter("index"));
        boolean selected = "true".equals(req.getParameter("selected"));

        if (index >= 0 && index < cart.size()) 
        {
            cart.get(index).setSelected(selected);
        }

        // 다시 장바구니 화면으로
        resp.sendRedirect(req.getContextPath() + "/client/cart");
    }
}

// 고객 회원 탈퇴

package com.example;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/client/delete")
public class ClientDeleteServlet extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {

        HttpSession session = req.getSession(false);
        Object user = (session == null) ? null : session.getAttribute("loginUser");
        
        if (!(user instanceof Client)) 
        {
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

        Client loginClient = (Client) user;
        String myId = loginClient.getId();

        // in-memory 리스트에서 제거
        List<Client> list = ClientSignUp.getClientList();
        Iterator<Client> it = list.iterator();
        while (it.hasNext()) 
        {
            Client c = it.next();
            if (c.getId().equals(myId)) 
            {
                it.remove();
                break;
            }
        }

        // 세션 무효화
        session.invalidate();

        // 🔁 정적 홈으로 이동 (타임리프 템플릿이 아님)
        resp.sendRedirect(req.getContextPath() + "/index.html");
    }
}





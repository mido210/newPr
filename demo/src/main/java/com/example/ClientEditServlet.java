// 고객 마이페이지의 실제 이름과 이메일 변경 (POST)
// 수정 폼 표시 (GET)

package com.example;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

@WebServlet("/client/edit")
public class ClientEditServlet extends HttpServlet 
{
            // ■1.
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
            // ■２。
        HttpSession session = req.getSession(false);
        Object user = (session == null) ? null : session.getAttribute("loginUser");
        if (!(user instanceof Client)) {
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

        resp.setContentType("text/html;charset=UTF-8");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        engine.process("clientEdit", ctx, resp.getWriter());
    }




    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        Object user = (session == null) ? null : session.getAttribute("loginUser");
        if (!(user instanceof Client)) 
        {
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }

        Client loginClient = (Client) user;

        String newName  = req.getParameter("name");
        String newEmail = req.getParameter("email");

        // 간단 검증
        if (newName == null || newName.trim().isEmpty()) 
        {
            sendError(req, resp, "이름을 입력해주세요.");
            return;
        }
        if (newEmail == null || !EMAIL_PATTERN.matcher(newEmail).matches()) 
        {
            sendError(req, resp, "올바른 이메일 형식이 아닙니다.");
            return;
        }

        // 1) 이름 및 이메일 변경 (세션 객체 갱신)
        loginClient.setName(newName.trim());
        loginClient.setEmail(newEmail.trim());

        // 2) 메모리 리스트(ClientSignUp) 갱신
        List<Client> list = ClientSignUp.getClientList();
        for (Client c : list) 
        {
            if (c.getId().equals(loginClient.getId())) 
            {
                c.setName(loginClient.getName());
                c.setEmail(loginClient.getEmail());
                break;
            }
        }

        // 완료 후 마이페이지로
        resp.sendRedirect(req.getContextPath() + "/client/mypage");
    }

    private void sendError(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws IOException 
    {
        resp.setContentType("text/html;charset=UTF-8");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("errorMsg", msg);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        engine.process("clientEdit", ctx, resp.getWriter());
    }
}


/*
  ■1. 정규 표현식 (Regular Expression) 
     1) 코드 의미

        private       → 클래스 내부 전용

        static        → 객체 생성 없이 공유됨

        final         → 값 변경 불가 (상수화)

        Pattern       → 정규 표현식 클래스

        EMAIL_PATTERN → 변수 이름, 원한다면 다른 이름으로 지어도 됨 (단, 의미 전달이 잘 되도록 짓는 게 좋음)


     2) 코드 추가 설명(static)
        : 클래스 단위에서 공유됩니다.

          👉 “static 없는 경우”
            * 각 학생이 자기 공책을 갖고 있어서, 자기만 필기할 수 있음.
            * A학생 공책에 쓴 내용은 B학생 공책과 아무 관계 없음.

          👉 “static 있는 경우”
            * 교실 칠판 하나를 두고 모든 학생이 거기에 같이 씀.
            * 누가 쓰든, 지우든 칠판은 하나뿐이라 다 같이 공유됨.


     3) 코드 추가 설명(fianl)
       변수에 fianl을 쓰면 해당 값이 절대 안바뀌고
       클래스에 final을 쓰면 상속이 안되며
       매서드에 final을 쓰면 오버라이딩이 안된다.

       주로 보안상의 이유로 fianl을 쓴다.

       이번 경우에는 이메일 정규식이 바뀌면 안되서 고정 시킨 거다.



--------------------------------------------------------------------------
  ■2. doGet() 매서드
      : 고객 정보 수정 화면(clientEdit.html)을 띄워주는 부분



        HttpSession session = req.getSession(false);
         * HttpSession
           > java EE에 내장되있는 클래스
           > 브라우저(사용자)마다 개별 저장 공간을 만들어서
             로그인 정보, 장바구니 같은 데이터를 유지한다.
           > 사용자가 여러 페이지를 이동해도 로그인 상태를 유지시키는 시켜준다.
          
         * req.getSession(false)
           > req = 브라우저 요청 정보 / 객체
           > getSession(boolean create) 매서드
            = 현재 요청에 연결된 세션을 돌려줌
             * true -> 세션이 없으면 새로 만듦
             * false -> 세션이 없으면 null 반환
           > 이미 존재하는 세션을 가져오되, 없으면 null 줘 

             로그인한 사용자가 맞는지 확인하기 위해 세션을 불러오는 코드다.




        Object user = (session == null) ? null : session.getAttribute("loginUser");
          > java 최상위 클래스인 Object 타입으로 user 변수를 선언해
            client 타입이든 Company 타입이든 다 받을 수 있게 한다.

          > session이 null이면 user에 null을 담고
            session이 있으면 loginUser라는 key 값으로 
            저장된 값을 user 변수에 담는다.




      
        if (!(user instanceof Client)) {
            resp.sendRedirect(req.getContextPath() + "/summit/login");
            return;
        }
   
          > instanceof는 객체가 특정 인스턴스인지 확인하는 기능이다.
           이걸로 user가 Client 타입인지 확인하고 아니라면
          > 서블릿 응답을 현재 웹 어플리케이션의 루트 경로로 해라.
          
    


        resp.setContentType("text/html;charset=UTF-8");
          > 브라우저에 보낼 문서는 HTML 문서다
          > 문자 인코딩은 UTF-8로 해라
            ( 한글 안 깨지게)

        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
          > WebContext
            : 타임리프에서 제공하는 클래스
            : 타임리프 템플릿을 렌더링(브라우저에 출력)할 때 필요한 정보를 담는 그릇
          > 응답, 요청, 웹 전체 설정, 사용자 언어를 ctx에 담는다.
          
        
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
          > ThymeleafInitializer.java에서 만든 templateEngine을 가져온다.
          > 이걸로 html을 렌더링(브라우저에 출력)할 수 있다.
       
          > TemplateEngine은 타임리프의 핵심 클래스다.
            html 파일을 읽어서 브라우저에 출력하는 역할을 한다.
            (템플릿 엔진 역할)
          > 어플리케이션 시작될때 TemplateEngine을 가져온다.



        engine.process("clientEdit", ctx, resp.getWriter());
          * clientEdit       → /WEB-INF/views/clientEdit.html 파일을 찾아서 렌더링.
          * ctx              → HTML에 끼워 넣을 데이터.
          * resp.getWriter() → 완성된 HTML을 브라우저에 전송할 출력 스트림.



---------------------------------------------------------------------
  ■ 3. doPost() 매서드
      : 고객이 정보 수정 폼에서 입력한 내용을 제출(저장 버튼 클릭) 하면 실행돼요
      : 입력값 검증, 세션 데이터와 메모리 리스트 갱신, 완료 후 마이페이지로 리다이렉트



        req.setCharacterEncoding("UTF-8");
         > 브라우저에서 보낸 글자를 UTF-8로 해석해라.
         > 글자 안 깨지게


        Client loginClient = (Client) user;
          > 세션에 들어있던 loginUser를 Object 타입에서 
           Client 타입으로 형변환(캐스팅)
   
          > user는 Object 타입 (모든 객체의 최상위 클래스).

          > session.getAttribute("loginUser")가 반환하는 값이 원래 
           Client 객체인데, 세션에서 꺼낼 때는 항상 Object 타입으로 나옴.

          > 따라서 (Client)로 형변환(casting) 해서 
            Client 전용 메서드 (setName(), getEmail() 등)를 
            쓸 수 있게 만드는 것.


        String newName  = req.getParameter("name");
        String newEmail = req.getParameter("email");
          > req.getParameter("name") 
            : 폼에서 name 속성이 "name"인 입력 필드의 값을 읽어옴
          > req.getParameter("email") 
            : 폼에서 name 속성이 "email"인 입력 필드의 값을 읽어옴
          > 사용자가 폼에 입력한 새 이름과 새 이메일을 읽어와서 변수에 저장




        // 간단 검증
        if (newName == null || newName.trim().isEmpty()) 
        {
            sendError(req, resp, "이름을 입력해주세요.");
            return;
        }
        if (newEmail == null || !EMAIL_PATTERN.matcher(newEmail).matches()) 
        {
            sendError(req, resp, "올바른 이메일 형식이 아닙니다.");
            return;
        }

            * 이름 = null ❌, 빈칸 ❌
            * 이메일 = null ❌, 올바른 이메일 형식 ❌



        // 1) 이름 및 이메일 변경 (세션 객체 갱신)
        loginClient.setName(newName.trim());
            > 사용자가 입력한 이름을 공백 제거한 후, 
              지금 로그인한 객체(loginClient)의 이름 필드에 저장해라.

            * loginClient는 세션에서 꺼낸 Client 객체
            * 즉, 지금 로그인 중인 고객이다.

            * setName(String name)은 client.java에서 만든 setter 메서드다.
            * this.name=name;으로 name 필드를 바꿔준다.

            * newName = req.getParameter("name") → 사용자가 입력한 새로운 이름.
            * .trim() = String 클래스의 메서드 (자바 표준 API).


        loginClient.setEmail(newEmail.trim());
            > 로그인한 객체의 이메일 필드를 새 값으로 갱신.

            * setEmail(String email) = Client.java 안에 정의된 Setter 메서드.
            * newEmail.trim() = 입력한 이메일에서 앞뒤 공백 제거.



        resp.setContentType("text/html;charset=UTF-8");
        WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("errorMsg", msg);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        engine.process("clientEdit", ctx, resp.getWriter());
    

         > html 문서를 보낼테니 UTF-8로 읽어라
         > html에 데이터를 전달할 ctx 그릇과 내용물 제작
         > HTML 템플릿에서 ${errorMsg}라고 쓰면 여기서 설정한 msg 값이 출력됨.
         > Thymeleaf 템플릿 엔진을 꺼내와서 HTML을 렌더링할 준비를 한다
         > clientEdit.html을 렌더링해서 오류 메시지를 포함한 HTML을 브라우저로 보낸다











---------------------------------------------------------------------


  ■ n. 기타
     1) doGet, doPost 메서드를 오버라이딩 하는 이유
       : 애초에 HttpServlet 클래스는 서블릿의 가장 기초적인 뼈대다.
       : 해당 클래스에있는 doGet(), doPost() 매서드는 분명히 있지만, 기본적으로 아무 일도 안함
       : 그래서 일을 시키기 위해서는 오버라이딩을 해야 한다.
         get 요청 받으면 이렇게 하고 post 요청 받으면 저렇게 해라 이런 식으로 말이다.



      2) doGet()과 doPost()의 구체적인 역할
        : doGet()
          * 고객이 정보 수정 페이지(폼) 에 접근했을 때 실행돼요.
          * 동작 순서
              1. 세션 확인 (로그인한 유저가 Client인지 확인)
              2. 확인이 끝나면 clientEdit.html을 불러와서 사용자에게 보여줌

        : doPost()
          * 고객이 정보 수정 폼에서 입력한 내용을 제출(저장 버튼 클릭) 하면 실행돼요
          * 동작 순서
              1. 세션에서 로그인한 Client 확인
              2. req.getParameter("name") / req.getParameter("email") 로 입력값 받음
              3. 유효성 검사 (이름이 비었는지, 이메일 형식이 맞는지)
              4. 문제가 없으면 세션 데이터와 메모리 리스트를 갱신
              5. 갱신이 끝나면 /client/mypage로 다시 보내서, 수정된 결과가 반영된 화면을 보여줌

        : 구체적인 동작 과정
          * 브라우저 → GET /client/edit 요청
             → doGet() 실행 → clientEdit.html 폼 페이지가 브라우저에 출력됨

          * 사용자가 이름/이메일 입력 후 저장 버튼 클릭 
             → POST /client/edit 요청 → doPost() 실행
              1. 입력값 읽어오기 (req.getParameter)
              2. 입력값 검증
              3. 세션 정보 수정
              4. 메모리 리스트(ClientSignUp) 갱신
              5. 성공 → /client/mypage로 리다이렉트

          * 브라우저 → /client/mypage GET 요청
            → ClientMyPageServlet 실행 → 수정된 값이 반영된 마이페이지 표시됨



      3) sendError() 메서드의 기능
         : 에러 메시지를 띄워 잘못된 이메일 형식으로 입력했다고 알리는 메서드


*/
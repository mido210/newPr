import java.util.ArrayList;
import java.util.Scanner;

//사용자 정보 저장소
public class User  
{
    private String name;
    private String id;
    private String password;    

    public User(String name, String id, String password) 
    {
        this.name = name;
        this.id = id;
        this.password = password;
    }

    public String getName()     { return name; }
    public String getId()       { return id; }
    public String getPassword() { return password; }

    public void setName(String name)         { this.name = name; }       
    public void setId(String id)             { this.id = id; }           
    public void setPassword(String password) { this.password = password; }
}



// 회원 정보
public class Membership   
{
    private String membershipId;
    private User user;

    public Membership(String membershipId, User user) 
    {
        this.membershipId = membershipId;
        this.user = user;
    }

    public String getMembershipId() { return membershipId; }
    public User getUser()           { return user; }

    public void setMembershipId(String membershipId) { this.membershipId = membershipId; }
    public void setUser(User user)                   { this.user = user; }
}


// 사용자 목록 관리
public class UserList  
{
    private ArrayList<User> userList = new ArrayList<>();

    // 같은 id가 있는 지 확인
    public boolean Id_Check(String id) 
    {
        for (User u : userList) 
        {
            if (u.getId().equals(id)) { return true; }
        }

        return false;
    }

    // 비밀번호 강도 검사
    public boolean Password_Check(String pw) 
    {
        if (pw.length() < 8) return false;

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : pw.toCharArray()) 
        {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        return hasLetter && hasDigit;
    }

    // 회원 가입 처리
    public boolean join(User user) 
    {
        if (Id_Check(user.getId())) 
        {
            System.out.println("❌ 중복된 아이디입니다.");
            return false;
        }

        if (!Password_Check(user.getPassword())) 
        {
            System.out.println("❌ 비밀번호가 너무 쉽습니다.");
            return false;
        }

        userList.add(user);
        System.out.println("✅ 가입 성공: " + user.getName());
        return true;
    }
}


// 회원 가입 처리 클래스
public class JoinMembership  
{  
    private Membership membership;

    public JoinMembership(Membership membership)  { this.membership = membership;}
    public Membership getMembership() { return membership; }
    public void setMembership(Membership membership) { this.membership = membership; }
   
    // 회원 가입 로직
    public void join(User user) { System.out.println(user.getName() + " has joined the membership with ID: " + membership.getMembershipId()); }
}
 

public class Main 
{
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        UserList userList = new UserList(); // 사용자 목록 관리자 생성

        System.out.print("이름 입력: ");
        String name = sc.nextLine();

        System.out.print("아이디 입력: ");
        String id = sc.nextLine();

        System.out.print("비밀번호 입력: ");
        String pw = sc.nextLine();

        User user = new User(name, id, pw);

        boolean success = userList.join(user);

        if (success) 
        {
            // 가입 성공 시 Membership 객체와 JoinMembership 로직까지 실행
            Membership membership = new Membership("M001", user);
            JoinMembership joinMembership = new JoinMembership(membership);

            joinMembership.join(user);

            System.out.println("회원 상세 정보:");
            System.out.println("Membership ID: " + joinMembership.getMembership().getMembershipId());
            System.out.println("User ID: " + joinMembership.getMembership().getUser().getId());
            System.out.println("User Name: " + joinMembership.getMembership().getUser().getName());
            System.out.println("User Password: " + joinMembership.getMembership().getUser().getPassword());
        }

        sc.close();
    }
}












package com.example;


public class Company {
	private String id; // 아이디 
	private String password; // 비밀번호 
	private String name;           // 업체 이름
    private String businessNumber; // 사업자 번호
    private String address;        // 업체 주소
	private String email;

	// 기본 생성자 
	public Company(){

	}
	
	// 생성자 
	public Company(String id, String password, String name, String businessNumber, String address, String email){
		this.id= id;
		this.password =password;
		this.name = name;
		this.businessNumber= businessNumber;
		this.address = address;
		this.email = email;
	}
	
	// getter, setter 예시 
	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}
	
	public String getPassword(){
		return password;
	}

	public void SetPassword(String password){
		this.password = password;
	}
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

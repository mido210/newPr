// 상품 정보 저장용 클래스
package com.example;

public class Product 
{
    private String productName;
    private int count;
    private int price;
    private String profilePath;

    public Product() {}

		// 기존 생성자 (count 기본값 1)
    public Product(String productName, int price, String profilePath) 
	{
        this(productName, 1, price, profilePath);
    }

    	// 표준 생성자 (count → price)
    public Product(String productName, int count, int price, String profilePath) 
	{
        this.productName = productName;
        this.count = count;
        this.price = price;
        this.profilePath = profilePath;
    }


    	// Getter & Setter
	public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getProfilePath() { return profilePath; }
    public void setProfilePath(String profilePath) { this.profilePath = profilePath; }
}
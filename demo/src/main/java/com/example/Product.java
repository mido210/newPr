package com.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
// @AllArgsConstructor // 모든 파라미터의 생성자 
@NoArgsConstructor// 기본생성자
@WebServlet("/submit/product")
public class Product extends HttpServlet {
	private String productName;
	private int count;
	private int price;
	private String profilePath;

	// // 기본 생성자
	// public Product() {

	// }

	public Product(String productName, int price, String profilePath){
		this(productName, 1, price, profilePath);// count= 1
	}

	// count 값도 변경 가능도록 수정한 생성자 
	public Product(String productName, int count, int price, String profilePath) {
		this.productName = productName;
		this.count = count;
		this.price = price;
		this.profilePath = profilePath;
	}

	// public String getProductName(){
	// 	return productName;
	// }

	// public void setProductName(String productName){
	// 	this.productName = productName;
	// }

	// public int getCount(){
	// 	return count;
	// }

	// public void setCount(int count){
	// 	this.count = count;
	// }


}

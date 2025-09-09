/*
1) 해당 파일 기능 

     * Product.java(상품 정보 저장용 클래스)    
     * 장바구니 관련 데이터(결제 여부, 개수) 저장
     * 해당 파일은 그냥 DTO다.


2) 이렇게 만든 이유

    정석적으로는 Product.java 파일에 저장된 상품들(Product 객체들) 안에
    electedForOrder(결제 여부), getTotalPrice()(총액 계산) 같은 필드를 
    추가하는 식으로 Product.java 파일을 수정해야 한다.

    근데 Product.java을 내가 만든게 아니고 해당 파일을 바탕으로 
    재호가 장바구니나 결제 기능을 만들 건데 그러면 안 될 것 같아서
    그냥 해당 파일을 하나 더 만듬

    
3) 변경 사항
 
    Product는 그대로 두고, 새로운 CartItem 클래스를 만들어 
    Product + 장바구니 관련 데이터(결제 여부, 개수)를 함께 관리.
 */


// 이 클래스는 com.exmple 패키지에 속해 있다.
// 그러니 다른 클래스에서 import com.example.CartItem;으로 불러올 수 있다.
package com.example;

public class CartItem 
{
    private Product product;   // 상품 자체 정보
    private int count;         // 개수
    private boolean selected;  // 결제 여부

        // 1. 생성자
    public CartItem(Product product, int count) 
    {
        this.product = product;
        this.count = count;
        this.selected = false;
    }

        // 2. Getter, Setter 메서드들
    public Product getProduct()               { return product; }   
    public int getCount()                     { return count; }
    public void setCount(int count)           { this.count = count; }
    public boolean isSelected()               { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

        // 3. 총액 계산
    public int getTotalPrice() { return product.getPrice() * count; }
}

/*



1. 생성자

    - 장바구니에 상품 넣을 때 상품 객체, 개수를 받아서 CartItem을 생성
    - 처음엔 selected를 무조건 false로 초기화(기본 상태는 '선택 안 됨')
    - 이 후 CartSelectServlet을 통해 
      setSelected(true)로 바꿔야 실제 결제에 포함됩니다.


2. Getter, Setter 메서드들

    - 다른 클래스에서 CartItem 값을 수정 할 수 있게 한다.


        getProduct()                  → 상품 자체 반환

        getCount()                    → 현재 개수 반환

        setCount(int count)           → 개수 변경 (예: CartUpdateServlet)

        isSelected()                  → 결제 여부 확인

        setSelected(boolean selected) → 결제 여부 변경 (예: CartSelectServlet)


3. 총액 계산
    
    - getTotalPrice()라는 메서드를 만든다.
    - 해당 메서드는 총액을 계산하는 메서드다.
    - 한 항목의 가격(product.getPrice()) × 개수(count) = 총액
    - CartServlet이나 cart.html에서 이 값을 가져다 씀

 */
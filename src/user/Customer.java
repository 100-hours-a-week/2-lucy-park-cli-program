package user;

import cart.Cart;
import cart.CartItem;
import order.Order;
import order.OrderItem;
import order.OrderType;
import restaurant.Customization;
import restaurant.Menu;
import restaurant.MenuItem;
import restaurant.Restaurant;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static order.OrderType.배달;

public class Customer extends User{

    // 필드 --


    // 생성자
    public Customer(String name) {
        super(name);
    }

    // 메소드
    public Restaurant selectRestaurant(List<Restaurant> restaurants, String rName) {
        for(Restaurant restaurant : restaurants) {
            if(rName.equals(restaurant.getrName())) {
                System.out.printf("%s 식당이 선택되었습니다.%n", restaurant.getrName());
                return restaurant;
            }
        }
        return null;
    }

    public Cart selectMenu(Restaurant restaurant, Customer customer) {
        Menu confirmedMenu = null;
        int contimedQuantity = 0;
        Scanner sc = new Scanner(System.in);

        while(confirmedMenu == null) {
            System.out.println("━━━━━━━━━━━━━━━⊱메뉴⊰━━━━━━━━━━━━━━━");

            List<Menu> menuList = restaurant.getMenuList();
            for (Menu menu : menuList) {
                System.out.printf("\uD80C\uDFF2๋࣭࣪˖ %s  %d%n", menu.getItemName(), menu.getItemPrice());  // 특수 이모티콘입니다!
            }
            System.out.println("━━━━━━━━━━━━━━━━⊱⋆⊰━━━━━━━━━━━━━━━━");

            String selectedMenu = sc.nextLine().trim();

            if (selectedMenu.equals("뒤로 가기")) {
                System.out.println("뒤로 가기를 선택하셨습니다. 식당 조회 화면으로 되돌아갑니다.");
                return null;
            }

            for (Menu menu : menuList) {
                if (menu.getItemName().equals(selectedMenu)) {
                    confirmedMenu = menu;
                    System.out.printf("%s 메뉴가 선택되었습니다.%n", selectedMenu);

                    int quantity = 0;
                    System.out.println("수량을 선택해주세요.");
                    while (true) {
                        try {
                            quantity = sc.nextInt();
                            sc.nextLine();

                            if (quantity == 0) {
                                System.out.println("1개 이상 주문 가능합니다.");
                                continue;
                            }
                            contimedQuantity = quantity;
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("숫자를 입력해주세요.");
                            sc.nextLine();
                        }
                    }

                    System.out.printf("%s 메뉴가 %d개 선택되었습니다.%n", menu.getItemName(), quantity);
                }
            }
            if (confirmedMenu == null) {
                System.out.println("존재하지 않는 메뉴입니다.");
            }
        }

        CartItem menuCartItem = null;
        menuCartItem = new CartItem(confirmedMenu, contimedQuantity);

        CartItem customizationCartItem = null;

        customizationCartItem = selectCustomazation(confirmedMenu, customizationCartItem, sc);

        System.out.println("선택된 메뉴는 다음과 같습니다.");
        System.out.printf("%s %d%n", menuCartItem.getMenuItem().getItemName(), menuCartItem.getQuantity());
        if (isCustomizationNotNUll(customizationCartItem)) {
            System.out.printf("%s %d%n", customizationCartItem.getMenuItem().getItemName(), customizationCartItem.getQuantity());
        }

        int totalPrice = isCustomizationNotNUll(customizationCartItem)
                ?
                menuCartItem.getSubTotal() +
                customizationCartItem.getSubTotal()
                :
                menuCartItem.getSubTotal();

        Cart cart = null;
        while(cart == null) {
            System.out.printf("총 가격은 %d원입니다. 카트에 담으시겠습니까?: 네  아니오 %n", totalPrice);

            String reply = sc.nextLine().trim();

            if (reply.equals("네")) {
                System.out.println("장바구니에 추가되었습니다.");
                List<CartItem> cartItemList = new ArrayList<>();
                cartItemList.add(menuCartItem);
                if (isCustomizationNotNUll(customizationCartItem)) {
                    cartItemList.add(customizationCartItem);
                }
                cart = new Cart(cartItemList);
                break;
            } else if (reply.equals("뒤로 가기") || reply.equals("아니오")) {
                System.out.println("처음 화면으로 돌아갑니다.");
                cart = new Cart(null);
                break;
            } else {
                System.out.println("조건에 맞게 응답해주세요.");
            }
        }
        return cart;
    }

    public CartItem selectCustomazation(Menu confirmedMenu, CartItem customizationCartItem,Scanner sc) {
        List<Customization> customizations = confirmedMenu.getCustomizations();

        if(customizations.isEmpty()) {
            System.out.println("사이드 메뉴가 없는 메뉴입니다.");
            return customizationCartItem;
        }
        else {

            System.out.println("━━━━━━━━━━━━━⊱사이드메뉴⊰━━━━━━━━━━━━━");
            for (Customization customization : customizations) {
                System.out.printf(":::::   %s  %d%n", customization.getItemName(), customization.getItemPrice());
            }
            System.out.println("━━━━━━━━━━━━━━━━━⊱⊰━━━━━━━━━━━━━━━━");

            customizationCartItem = new CartItem();
            MenuItem selectedCustomization = null;

            String side = "";
            System.out.println("사이드 메뉴를 선택해주세요.");
            while(true) {
                side = sc.nextLine().trim();

                for (Customization customization : customizations) {
                    if (customization.getItemName().equals(side)) {
                        selectedCustomization = customization;
                        System.out.printf("%s 사이드 메뉴가 선택되었습니다.%n", side);
                        break;
                    }
                }
                if (selectedCustomization != null) {
                    break;
                }

                System.out.println("해당 사이드 메뉴가 없습니다. 다시 선택해주세요.");
            }

            int sideQuantity = 0;
            System.out.println("수량을 선택해주세요.");
            while (true) {
                try {
                    sideQuantity = sc.nextInt();
                    sc.nextLine();

                    if (sideQuantity == 0) {
                        System.out.println("1개 이상 주문 가능합니다.");
                        sc.nextLine();
                        continue;
                    }

                    break;
                } catch (InputMismatchException e) {
                    System.out.println("숫자를 입력해주세요.");
                    sc.nextLine();
                }
            }
            System.out.printf("%s 메뉴가 %d개 선택되었습니다.%n", side, sideQuantity);
            customizationCartItem.setMenuItem(selectedCustomization);
            customizationCartItem.setQuantity(sideQuantity);
            return customizationCartItem;
        }
    }

    // 리턴 타입 고려 필요
    public void setDeliveryOrder(Restaurant restaurant, Cart cart, OrderType orderType) {
        // 최소 주문 금액 확인(배달)
        int minOrderAmount = restaurant.getrMinOrderAmount();
        System.out.printf("최소 주문 금액: %d%n", minOrderAmount);

        int cartTotalPrice = cart.getTotalPrice();
        System.out.printf("현재 장바구니 금액: %d%n", cartTotalPrice);

        if (minOrderAmount > cartTotalPrice) {
            System.out.println("최소 주문 금액에 맞게 주문해주세요.");
            //return null;
        } else {
            System.out.println("주문되었습니다.");

            // Customer, VIPCustomer 여부에 따라 오버라이딩된 메소드 사용됨
            int deliveryFee = getDeliveryFee(restaurant);

            if (deliveryFee == 0) {
                System.out.println("VIP 혜택으로 배달비가 0원 처리되었습니다.");
            } else {
                System.out.printf("귀하의 배달비는 %d원입니다.%n", deliveryFee);
            }

            // 주문
            setOrder(restaurant, cart, orderType);
        }
    }

    public boolean isCustomizationNotNUll(CartItem customizationCartItem) {
        if (customizationCartItem != null) {
            return true;
        }
        return false;
    }

    public void setTakeoutOrder(Restaurant restaurant, Cart cart, OrderType orderType) {
        setOrder(restaurant, cart, orderType);
    }

    public void setOrder(Restaurant restaurant, Cart cart, OrderType orderType) {
        /// 주문 아이템에 카트 깊은 복사 -> 같은 클래스여야 가능한가?
        List<CartItem> cartItemList = cart.getCartItemList();
        List<OrderItem> orderItemList = new ArrayList<>();

        for(CartItem cartItem : cartItemList) {
            ///  깊은 복사로 업데이트 필요: 클래스가 다르기 때문에 불가능한가?
            OrderItem orderItem = new OrderItem(cartItem.getMenuItem(), cartItem.getQuantity());
            orderItemList.add(orderItem);
        }

        int deliverFee = restaurant.getrDeliveryFee();

        Order order = new Order(this, restaurant, orderItemList,
                                orderType == 배달
                                    ? cart.getTotalPrice() + deliverFee
                                    : cart.getTotalPrice(),
                                orderType);

        System.out.println("━━━━━━━━━━━⊱주문 내역 확인⊰━━━━━━━━━━━");
        System.out.printf(":::::: 주문 번호   %d%n", order.getOrderId());
        System.out.printf(":::::: 주문 고객   %s%n", order.getCustomer().getName());
        System.out.printf(":::::: 식당       %s%n", order.getRestaurant().getrName());
        System.out.printf(":::::: 수령 방식   %s%n", order.getOrderType());
        System.out.printf(":::::: 주문 금액   %s%n", order.getTotalOrderPrice());
        System.out.printf(":::::: 주문 시각   %s%n", order.getOrderTimeStamp()); // LocalDateTime 처리
        System.out.printf(":::::: 진행 상황   %s%n", order.getOrderStatus());

        int orderTime =
                orderType == 배달
                ? order.getRestaurant().getrDeliveryTime()
                : order.getRestaurant().getrTakeoutTime();
        int hour = orderTime / 60;
        int minutes = orderTime % 60;
        System.out.printf(":::::: 소요 시간  %s %d분%n", hour > 0 ? " " + hour + "시간" : "", minutes);

        System.out.println("이용해 주셔서 감사합니다.");
        System.out.println("EatsNow!");
        System.out.println("━━━━━━━━━━━━━━━━⊱⋆⊰━━━━━━━━━━━━━━━━");
    }

    public int getDeliveryFee(Restaurant restaurant) {
        return restaurant.getrDeliveryFee();
    }
}

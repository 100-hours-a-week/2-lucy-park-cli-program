import cart.Cart;
import cart.CartItem;
import restaurant.Customization;
import restaurant.Menu;
import restaurant.Restaurant;
import user.Customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static order.OrderType.배달;
import static order.OrderType.포장;

public class Main {
    public static void main(String[] args) {
        List<Restaurant> restaurantList = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        // 식당 초기화
        Restaurant r1 = new Restaurant("맥도날드", "서울시 용산구", "패스트푸트",
                                        4.8, 3000, 12000, 50);

        List<Menu> r1MenuList = new ArrayList<>();
        List<Customization> customizations = Arrays.asList(
                new Customization("후렌치 후라이 스몰", 2300, "스낵"),
                new Customization("후렌치 후라이 미디움", 2800, "스낵"),
                new Customization("후렌치 후라이 라지", 3300, "스낵")
        );

        Menu r1Menu = new Menu("빅맥", 6300, "버거", customizations);
        r1MenuList.add(r1Menu);
        r1.addMenus(r1MenuList);

        restaurantList.add(r1);

        Customer customer = new Customer("박지은");

        // 서비스
        System.out.println("환영합니다. EatsNow!");

        Restaurant restaurant = null;
        Cart cart = null;
        while(cart == null) {
            System.out.println("식당 조회");

            for (Restaurant res : restaurantList) {
                System.out.println("━━━━━━━━━━━━━━━━⊱⋆⊰━━━━━━━━━━━━━━━━");
                System.out.printf(".•☀ %s%n ☀•.", res.getrName());
                System.out.printf(".•☀ 평점 %s%n", res.getrRate());
                System.out.printf(".•☀ %s분 소요%n", res.getrDeliveryTime());
                System.out.printf(".•☀ 배달팁 %s%n", res.getrDeliveryFee());
                System.out.printf(".•☀ 최소 주문 %s%n", res.getrMinOrderAmount());
            }
            System.out.println("━━━━━━━━━━━━━━━━⊱⋆⊰━━━━━━━━━━━━━━━━");

            System.out.println("식당을 선택해주세요.");
            while(true) {
                String selectedRestaurantName = sc.nextLine().trim();
                restaurant = customer.selectRestaurant(restaurantList, selectedRestaurantName);

                if(restaurant == null) {
                    System.out.println("해당 이름의 식당이 없습니다. 다시 선택해주세요.");
                    continue;
                }
                break;
            }

            String reply = null;
            System.out.println("메뉴를 선택해주세요. (혹은 '뒤로 가기'를 입력하세요.)");
            while (cart == null) {
                //if (reply.equals("뒤로 가기")) {
                    /// // 처음으로
                //}
                cart = customer.selectMenu(restaurant, customer);

                if (cart == null) {
                    System.out.println("메뉴를 다시 선택해주세요. (혹은 '뒤로 가기'를 입력하세요.)");
                    continue;
                }

                break;
            }
        }

        if(cart != null) {
            System.out.println("장바구니를 확인합니다.");
            System.out.println("━━━━━━━━━━━━━━⊱장바구니⊰━━━━━━━━━━━━━━");
            List<CartItem> cartItemList = cart.getCartItemList();
            for(CartItem cartItem : cartItemList) {
                System.out.printf("%s %d%n", cartItem.getMenuItem().getItemName(), cartItem.getQuantity());
            }
            System.out.println("━━━━━━━━━━━━━━━━⊱⋆⊰━━━━━━━━━━━━━━━━");
            while(true) {
                System.out.println("주문하시겠습니까? 네  아니오");
                String orderReply = sc.nextLine();

                if (orderReply.equals("네")) {               ////////  에러 처리 필요
                    while(true) {
                        System.out.println("수령 방식을 선택해주세요: 배달   포장");    ///// 에러 처리 필요
                        String orderType = sc.nextLine();

                        // 배달 주문
                        if (orderType.equals("배달")) {
                            customer.setDeliveryOrder(restaurant, cart, 배달);
                            break;
                        } else if (orderType.equals("포장")) {
                            customer.setTakeoutOrder(restaurant, cart, 포장);
                            break;
                        } else {
                            System.out.println("잘못된 응답입니다. 다시 선택해주세요.");
                            sc.nextLine();
                        }
                    }

                }
                break;
            }
        }
    }
}
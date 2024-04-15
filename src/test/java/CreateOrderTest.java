import java.util.List;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import orders.OrderClient;
import orders.Orders;
import orders.OrderClientImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private OrderClient orderClient;
    private List<String> colour;

    public CreateOrderTest(List<String> colour) {
        this.colour = colour;
    }


    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("GREY", "BLACK")},
                {List.of()},
        };
    }

    @Test
    @DisplayName("Create order")
    @Description("Проверка возможности создания заказа c выбором цвета")
    public void CreateOrderTest() {
        orderClient = new OrderClientImpl();

        Orders orders = Orders.create("Naruto", "Uchiha", "Konoha, 142", "4", "+7 800 355 35 35", 5, "2020-06-06", "Хоть бы ревьюер, с первого раза зачел все", colour);
        ValidatableResponse response = orderClient.createOrders(orders);
        response.assertThat().statusCode(201);
    }


}

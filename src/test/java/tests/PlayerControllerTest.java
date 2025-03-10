package tests;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.example.api.PlayerController;
import org.example.models.Player;
import org.testng.annotations.Test;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Epic("API Testing")
@Feature("Player Controller")
public class PlayerControllerTest extends BaseTest{
    private Long createdPlayerId;

    @Test(priority = 1)
    @Story("Create Player")
    @Description("Позитивний тест: Створення нового гравця з валідними даними")
    public void testCreatePlayerPositive() {
        // Підготовка тестових даних
        Player player = Player.createValidPlayer();

        // Виконання запиту
        Response response = PlayerController.createPlayer(config.getSupervisorLogin(), player);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 200,
                "Код відповіді має бути 200");
        Player createdPlayer = response.as(Player.class);

        // Зберігаємо ID для подальших тестів
        createdPlayerId = createdPlayer.getId();

        // Валідація даних
        assertEquals(createdPlayer.getLogin(), player.getLogin(),
                "Логін створеного гравця має співпадати");
        assertEquals(createdPlayer.getAge(), player.getAge(),
                "Вік створеного гравця має співпадати");
        assertEquals(createdPlayer.getGender(), player.getGender(),
                "Стать створеного гравця має співпадати");
        assertEquals(createdPlayer.getRole(), player.getRole(),
                "Роль створеного гравця має співпадати");
        assertEquals(createdPlayer.getScreenName(), player.getScreenName(),
                "Ім'я на екрані створеного гравця має співпадати");
    }

    @Test(priority = 2)
    @Story("Create Player")
    @Description("Негативний тест: Створення гравця з надто молодим віком (менше 16)")
    public void testCreatePlayerNegativeAgeTooYoung() {

        Player player = Player.createInvalidAgeTooYoung();

        Response response = PlayerController.createPlayer(config.getSupervisorLogin(), player);

        assertEquals(response.getStatusCode(), 400,
                "Код відповіді має бути 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("age"),
                "Повідомлення про помилку має містити інформацію про вік");
    }

    @Test(priority = 3)
    @Story("Create Player")
    @Description("Негативний тест: Створення гравця з надто старим віком (більше 60)")
    public void testCreatePlayerNegativeAgeTooOld() {

        Player player = Player.createInvalidTooOld();

        // Виконання запиту
        Response response = PlayerController.createPlayer(config.getSupervisorLogin(), player);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 400, "Код відповіді має бути 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("age"), "Повідомлення про помилку має містити інформацію про вік");
    }

    @Test(priority = 4)
    @Story("Create Player")
    @Description("Негативний тест: Створення гравця з невалідною роллю")
    public void testCreatePlayerNegativeInvalidRole() {
        // Підготовка тестових даних
        Player player = Player.createInvalidRole();

        // Виконання запиту
        Response response = PlayerController.createPlayer(config.getSupervisorLogin(), player);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 400, "Код відповіді має бути 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("role"), "Повідомлення про помилку має містити інформацію про роль");
    }

    @Test(priority = 5)
    @Story("Create Player")
    @Description("Негативний тест: Створення гравця з невалідним паролем")
    public void testCreatePlayerNegativeInvalidPassword() {
        // Підготовка тестових даних
        Player player = Player.createInvalidPassword();

        // Виконання запиту
        Response response = PlayerController.createPlayer(config.getSupervisorLogin(), player);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 400, "Код відповіді має бути 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("password"), "Повідомлення про помилку має містити інформацію про пароль");
    }

    @Test(priority = 6)
    @Story("Create Player")
    @Description("Негативний тест: Створення гравця з невалідною статтю")
    public void testCreatePlayerNegativeInvalidGender() {
        // Підготовка тестових даних
        Player player = Player.createInvalidGender();

        // Виконання запиту
        Response response = PlayerController.createPlayer(config.getSupervisorLogin(), player);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 400, "Код відповіді має бути 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("gender"), "Повідомлення про помилку має містити інформацію про стать");
    }

    @Test(priority = 7)
    @Story("Create Player")
    @Description("Негативний тест: Створення гравця звичайним користувачем (не supervisor і не admin)")
    public void testCreatePlayerNegativeInvalidEditor() {
        // Спочатку створюємо звичайного користувача
        Player regularUser = Player.createValidPlayer();
        Response createResponse = PlayerController.createPlayer(config.getSupervisorLogin(), regularUser);
        assertEquals(createResponse.getStatusCode(), 200, "Код відповіді має бути 200");
        Player createdUser = createResponse.as(Player.class);

        // Тепер намагаємось створити нового гравця від імені звичайного користувача
        Player newPlayer = Player.createValidPlayer();
        Response response = PlayerController.createPlayer(createdUser.getLogin(), newPlayer);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 403, "Код відповіді має бути 403 (Forbidden)");
    }

    @Test(priority = 8)
    @Story("Get Player")
    @Description("Позитивний тест: Отримання гравця за ID")
    public void testGetPlayerPositive() {
        // Перевіряємо, що маємо ID користувача з попереднього тесту
        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        Response response = PlayerController.getPlayer(createdPlayerId);

        assertEquals(response.getStatusCode(), 200, "Код відповіді має бути 200");
        Player player = response.as(Player.class);
        assertEquals(player.getId(), createdPlayerId, "ID отриманого гравця має співпадати з запитаним");
    }

    @Test(priority = 9)
    @Story("Get Player")
    @Description("Негативний тест: Отримання неіснуючого гравця")
    public void testGetPlayerNegativeNonExistent() {
        // Виконання запиту з неіснуючим ID
        Long nonExistentId = 999999L;
        Response response = PlayerController.getPlayer(nonExistentId);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 404, "Код відповіді має бути 404 (Not Found)");
    }

    @Test(priority = 10)
    @Story("Update Player")
    @Description("Позитивний тест: Оновлення даних гравця")
    public void testUpdatePlayerPositive() {
        // Перевіряємо, що маємо ID користувача з попереднього тесту
        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        // Отримуємо поточні дані гравця
        Response getResponse = PlayerController.getPlayer(createdPlayerId);
        assertEquals(getResponse.getStatusCode(), 200, "Код відповіді має бути 200");
        Player currentPlayer = getResponse.as(Player.class);

        // Підготовка даних для оновлення
        Player updatedPlayer = new Player();
        updatedPlayer.setAge(30); // Змінюємо вік
        updatedPlayer.setScreenName("UpdatedScreenName" + System.currentTimeMillis()); // Змінюємо ім'я на екрані

        // Виконання запиту
        Response response = PlayerController.updatePlayer(config.getSupervisorLogin(), createdPlayerId, updatedPlayer);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 200, "Код відповіді має бути 200");
        Player resultPlayer = response.as(Player.class);

        // Перевірка оновлених даних
        assertEquals(resultPlayer.getId(), createdPlayerId, "ID гравця не повинен змінитися");
        assertEquals(resultPlayer.getAge(), updatedPlayer.getAge(), "Вік гравця має бути оновлений");
        assertEquals(resultPlayer.getScreenName(), updatedPlayer.getScreenName(), "Ім'я на екрані має бути оновлене");
        assertEquals(resultPlayer.getLogin(), currentPlayer.getLogin(), "Логін гравця не повинен змінитися");
        assertEquals(resultPlayer.getGender(), currentPlayer.getGender(), "Стать гравця не повинна змінитися");
        assertEquals(resultPlayer.getRole(), currentPlayer.getRole(), "Роль гравця не повинна змінитися");
    }

    @Test(priority = 11)
    @Story("Update Player")
    @Description("Негативний тест: Оновлення гравця з невалідним віком")
    public void testUpdatePlayerNegativeInvalidAge() {
        // Перевіряємо, що маємо ID користувача з попереднього тесту
        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        // Підготовка даних для оновлення з невалідним віком
        Player updatedPlayer = new Player();
        updatedPlayer.setAge(10); // Вік менше 16

        // Виконання запиту
        Response response = PlayerController.updatePlayer(config.getSupervisorLogin(), createdPlayerId, updatedPlayer);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 400, "Код відповіді має бути 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("age"), "Повідомлення про помилку має містити інформацію про вік");
    }

    @Test(priority = 12)
    @Story("Update Player")
    @Description("Негативний тест: Оновлення гравця з невалідним паролем")
    public void testUpdatePlayerNegativeInvalidPassword() {
        // Перевіряємо, що маємо ID користувача з попереднього тесту
        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        // Підготовка даних для оновлення з невалідним паролем
        Player updatedPlayer = new Player();
        updatedPlayer.setPassword("short"); // Пароль менше 7 символів

        // Виконання запиту
        Response response = PlayerController.updatePlayer(config.getSupervisorLogin(), createdPlayerId, updatedPlayer);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 400, "Код відповіді має бути 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("password"), "Повідомлення про помилку має містити інформацію про пароль");
    }

    @Test(priority = 13)
    @Story("Update Player")
    @Description("Негативний тест: Оновлення неіснуючого гравця")
    public void testUpdatePlayerNegativeNonExistent() {
        // Підготовка даних для оновлення
        Player updatedPlayer = new Player();
        updatedPlayer.setAge(30);

        // Виконання запиту з неіснуючим ID
        Long nonExistentId = 999999L;
        Response response = PlayerController.updatePlayer(config.getSupervisorLogin(), nonExistentId, updatedPlayer);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 404, "Код відповіді має бути 404 (Not Found)");
    }

    @Test(priority = 14)
    @Story("Update Player")
    @Description("Негативний тест: Оновлення гравця звичайним користувачем (не supervisor і не admin)")
    public void testUpdatePlayerNegativeInvalidEditor() {
        // Перевіряємо, що маємо ID користувача з попереднього тесту
        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        // Спочатку створюємо звичайного користувача
        Player regularUser = Player.createValidPlayer();
        Response createResponse = PlayerController.createPlayer(config.getSupervisorLogin(), regularUser);
        assertEquals(createResponse.getStatusCode(), 200, "Код відповіді має бути 200");
        Player createdUser = createResponse.as(Player.class);

        // Підготовка даних для оновлення
        Player updatedPlayer = new Player();
        updatedPlayer.setAge(30);

        // Виконання запиту від імені звичайного користувача
        Response response = PlayerController.updatePlayer(createdUser.getLogin(), createdPlayerId, updatedPlayer);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 403, "Код відповіді має бути 403 (Forbidden)");
    }

    @Test(priority = 15)
    @Story("Delete Player")
    @Description("Позитивний тест: Видалення гравця")
    public void testDeletePlayerPositive() {
        // Створюємо нового гравця для видалення
        Player player = Player.createValidPlayer();
        Response createResponse = PlayerController.createPlayer(config.getSupervisorLogin(), player);
        assertEquals(createResponse.getStatusCode(), 200, "Код відповіді має бути 200");
        Player createdPlayer = createResponse.as(Player.class);

        // Виконання запиту на видалення
        Response response = PlayerController.deletePlayer(config.getSupervisorLogin(), createdPlayer.getId());

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 200, "Код відповіді має бути 200");

        // Перевіряємо, що гравця дійсно видалено
        Response getResponse = PlayerController.getPlayer(createdPlayer.getId());
        assertEquals(getResponse.getStatusCode(), 404, "Код відповіді має бути 404 (Not Found)");
    }

    @Test(priority = 16)
    @Story("Delete Player")
    @Description("Негативний тест: Видалення неіснуючого гравця")
    public void testDeletePlayerNegativeNonExistent() {
        // Виконання запиту з неіснуючим ID
        Long nonExistentId = 999999L;
        Response response = PlayerController.deletePlayer(config.getSupervisorLogin(), nonExistentId);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 404, "Код відповіді має бути 404 (Not Found)");
    }

    @Test(priority = 17)
    @Story("Delete Player")
    @Description("Негативний тест: Видалення гравця звичайним користувачем (не supervisor і не admin)")
    public void testDeletePlayerNegativeInvalidEditor() {
        // Перевіряємо, що маємо ID користувача з попереднього тесту
        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        // Спочатку створюємо звичайного користувача
        Player regularUser = Player.createValidPlayer();
        Response createResponse = PlayerController.createPlayer(config.getSupervisorLogin(), regularUser);
        assertEquals(createResponse.getStatusCode(), 200, "Код відповіді має бути 200");
        Player createdUser = createResponse.as(Player.class);

        // Виконання запиту на видалення від імені звичайного користувача
        Response response = PlayerController.deletePlayer(createdUser.getLogin(), createdPlayerId);

        // Перевірка результатів
        assertEquals(response.getStatusCode(), 403, "Код відповіді має бути 403 (Forbidden)");
    }

    @Test(priority = 18)
    @Story("Delete Player")
    @Description("Критичний тест: Спроба видалення користувача supervisor")
    public void testDeleteSupervisorNegative() {
        // Отримуємо інформацію про supervisor
        Response getResponse = PlayerController.getPlayer(1L); // Припускаємо, що supervisor має ID=1

        // Якщо отримали дані про supervisor
        if (getResponse.getStatusCode() == 200) {
            Player supervisor = getResponse.as(Player.class);

            // Перевірка, що це дійсно supervisor
            if ("supervisor".equals(supervisor.getRole())) {
                // Спроба видалити supervisor
                Response response = PlayerController.deletePlayer(config.getAdminLogin(), supervisor.getId());

                // Перевірка результатів
                assertEquals(response.getStatusCode(), 403, "Код відповіді має бути 403 - supervisor не може бути видалений");
            }
        }
    }

    @Test(priority = 19)
    @Story("Create Player")
    @Description("Критичний тест: Створення двох гравців з однаковим логіном")
    public void testCreatePlayerDuplicateLogin() {
        // Підготовка тестових даних для першого гравця
        Player player1 = Player.createValidPlayer();
        String duplicateLogin = "duplicate" + System.currentTimeMillis();
        player1.setLogin(duplicateLogin);

        // Створення першого гравця
        Response response1 = PlayerController.createPlayer(config.getSupervisorLogin(), player1);
        assertEquals(response1.getStatusCode(), 200, "Код відповіді має бути 200");

        // Підготовка тестових даних для другого гравця з тим самим логіном
        Player player2 = Player.createValidPlayer();
        player2.setLogin(duplicateLogin);

        // Спроба створити другого гравця з тим самим логіном
        Response response2 = PlayerController.createPlayer(config.getSupervisorLogin(), player2);

        // Перевірка результатів
        assertEquals(response2.getStatusCode(), 400, "Код відповіді має бути 400 - дублікат логіну не дозволяється");
        String errorMsg = response2.getBody().asString();
        assertTrue(errorMsg.contains("login") || errorMsg.contains("duplicate"),
                "Повідомлення про помилку має містити інформацію про дублікат логіну");
    }

    @Test(priority = 20)
    @Story("Create Player")
    @Description("Критичний тест: Створення двох гравців з однаковим screenName")
    public void testCreatePlayerDuplicateScreenName() {
        // Підготовка тестових даних для першого гравця
        Player player1 = Player.createValidPlayer();
        String duplicateScreenName = "DuplicateScreen" + System.currentTimeMillis();
        player1.setScreenName(duplicateScreenName);

        // Створення першого гравця
        Response response1 = PlayerController.createPlayer(config.getSupervisorLogin(), player1);
        assertEquals(response1.getStatusCode(), 200, "Код відповіді має бути 200");

        // Підготовка тестових даних для другого гравця з тим самим screenName
        Player player2 = Player.createValidPlayer();
        player2.setScreenName(duplicateScreenName);

        // Спроба створити другого гравця з тим самим screenName
        Response response2 = PlayerController.createPlayer(config.getSupervisorLogin(), player2);

        // Перевірка результатів
        assertEquals(response2.getStatusCode(), 400,
                "Код відповіді має бути 400 - дублікат screenName не дозволяється");
        String errorMsg = response2.getBody().asString();
        assertTrue(errorMsg.contains("screenName") || errorMsg.contains("duplicate"),
                "Повідомлення про помилку має містити інформацію про дублікат screenName");
    }
}

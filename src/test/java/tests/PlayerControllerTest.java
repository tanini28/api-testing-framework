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
    @Description("Positive test: Creating a new player with valid data")
    public void testCreatePlayerPositive() {

        Player player = Player.createValidPlayer();

        Response response = PlayerController.createPlayer
                (config.getSupervisorLogin(), player);

        assertEquals(response.getStatusCode(), 200,
                "The response code should be 200");
        Player createdPlayer = response.as(Player.class);

        createdPlayerId = createdPlayer.getId();

        assertEquals(createdPlayer.getLogin(), player.getLogin(),
                "The created player's login must match");
        assertEquals(createdPlayer.getAge(), player.getAge(),
                "The age of the created player must match");
        assertEquals(createdPlayer.getGender(), player.getGender(),
                "The gender of the created player must match");
        assertEquals(createdPlayer.getRole(), player.getRole(),
                "The role of the created player must match");
        assertEquals(createdPlayer.getScreenName(), player.getScreenName(),
                "The screen name of the created player must match");
    }

    @Test(priority = 2)
    @Story("Create Player")
    @Description("Negative test: Creating a player with a too young age (less than 16)")
    public void testCreatePlayerNegativeAgeTooYoung() {

        Player player = Player.createInvalidAgeTooYoung();

        Response response = PlayerController.createPlayer
                (config.getSupervisorLogin(), player);

        assertEquals(response.getStatusCode(), 405,
                "The response code should be 405");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("age"),
                "The error message should include age information");
    }

    @Test(priority = 3)
    @Story("Create Player")
    @Description("Negative test: Creating a player with an age that is too old (over 60)")
    public void testCreatePlayerNegativeAgeTooOld() {

        Player player = Player.createInvalidTooOld();

        Response response = PlayerController.createPlayer
                (config.getSupervisorLogin(), player);

        assertEquals(response.getStatusCode(), 400,
                "The response code should be 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("age"),
                "The error message should include age information");
    }

    @Test(priority = 4)
    @Story("Create Player")
    @Description("Negative test: Creating a player with an invalid role")
    public void testCreatePlayerNegativeInvalidRole() {
        // Підготовка тестових даних
        Player player = Player.createInvalidRole();

        Response response = PlayerController.createPlayer
                (config.getSupervisorLogin(), player);

        assertEquals(response.getStatusCode(), 400,
                "The response code should be 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("role"),
                "The error message should contain information about the role");
    }

    @Test(priority = 5)
    @Story("Create Player")
    @Description("Negative test: Creating a player with an invalid password")
    public void testCreatePlayerNegativeInvalidPassword() {

        Player player = Player.createInvalidPassword();

        Response response = PlayerController.createPlayer
                (config.getSupervisorLogin(), player);

        assertEquals(response.getStatusCode(), 400,
                "Код відповіді має бути 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("password"),
                "Повідомлення про помилку має містити інформацію про пароль");
    }

    @Test(priority = 6)
    @Story("Create Player")
    @Description("Negative test: Creating a player with an invalid gender")
    public void testCreatePlayerNegativeInvalidGender() {

        Player player = Player.createInvalidGender();

        Response response = PlayerController.createPlayer
                (config.getSupervisorLogin(), player);

        assertEquals(response.getStatusCode(), 400,
                "Код відповіді має бути 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("gender"),
                "The error message should include gender information");
    }

    @Test(priority = 7)
    @Story("Create Player")
    @Description("Negative test: Creating a player as a regular user " +
            "(not a supervisor or admin))")
    public void testCreatePlayerNegativeInvalidEditor() {

        Player regularUser = Player.createValidPlayer();

        Response createResponse = PlayerController.createPlayer
                (config.getSupervisorLogin(), regularUser);

        assertEquals(createResponse.getStatusCode(), 200,
                "The response code should be 200");
        Player createdUser = createResponse.as(Player.class);

        Player newPlayer = Player.createValidPlayer();
        Response response = PlayerController.createPlayer(createdUser.getLogin(), newPlayer);

        assertEquals(response.getStatusCode(), 403,
                "The response code should be 403 (Forbidden)");
    }

    @Test(priority = 8)
    @Story("Get Player")
    @Description("Positive test: Getting player by ID")
    public void testGetPlayerPositive() {

        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        Response response = PlayerController.getPlayer(createdPlayerId);

        assertEquals(response.getStatusCode(), 200,
                "The response code should be 200");
        Player player = response.as(Player.class);
        assertEquals(player.getId(), createdPlayerId,
                "The received player ID must match the requested one.");
    }

    @Test(priority = 9)
    @Story("Get Player")
    @Description("Negative test: Getting a non-existent player")
    public void testGetPlayerNegativeNonExistent() {

        Long nonExistentId = 999999L;
        Response response = PlayerController.getPlayer(nonExistentId);


        assertEquals(response.getStatusCode(), 404,
                "The response code should be 404 (Not Found)");
    }

    @Test(priority = 10)
    @Story("Update Player")
    @Description("Positive test: Player data update")
    public void testUpdatePlayerPositive() {

        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        Response getResponse = PlayerController.getPlayer(createdPlayerId);
        assertEquals(getResponse.getStatusCode(), 200,
                "The response code should be 200");
        Player currentPlayer = getResponse.as(Player.class);

        Player updatedPlayer = new Player();
        updatedPlayer.setAge(30);
        updatedPlayer.setScreenName("UpdatedScreenName" + System.currentTimeMillis());

        Response response = PlayerController.updatePlayer
                (config.getSupervisorLogin(), createdPlayerId, updatedPlayer);

        assertEquals(response.getStatusCode(), 200,
                "The response code should be 200");
        Player resultPlayer = response.as(Player.class);

        assertEquals(resultPlayer.getId(), createdPlayerId,
                "Player ID should not change");
        assertEquals(resultPlayer.getAge(), updatedPlayer.getAge(),
                "Player age needs to be updated");
        assertEquals(resultPlayer.getScreenName(), updatedPlayer.getScreenName(),
                "The screen name needs to be updated.");
        assertEquals(resultPlayer.getLogin(), currentPlayer.getLogin(),
                "The player's login must not change.");
        assertEquals(resultPlayer.getGender(), currentPlayer.getGender(),
                "The player's gender should not change.");
        assertEquals(resultPlayer.getRole(), currentPlayer.getRole(),
                "The player's role should not change.");
    }

    @Test(priority = 11)
    @Story("Update Player")
    @Description("Negative test: Updating a player with an invalid age")
    public void testUpdatePlayerNegativeInvalidAge() {

        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        Player updatedPlayer = new Player();
        updatedPlayer.setAge(10);

        Response response = PlayerController.updatePlayer
                (config.getSupervisorLogin(), createdPlayerId, updatedPlayer);

        assertEquals(response.getStatusCode(), 400,
                "The response code should be 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("age"),
                "The error message should include age information");
    }

    @Test(priority = 12)
    @Story("Update Player")
    @Description("Negative test: Updating a player with an invalid password")
    public void testUpdatePlayerNegativeInvalidPassword() {

        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        Player updatedPlayer = new Player();
        updatedPlayer.setPassword("short");

        Response response = PlayerController.updatePlayer
                (config.getSupervisorLogin(), createdPlayerId, updatedPlayer);

        assertEquals(response.getStatusCode(), 400,
                "The response code should be 400");
        String errorMsg = response.getBody().asString();
        assertTrue(errorMsg.contains("password"),
                "The error message should contain password information");
    }

    @Test(priority = 13)
    @Story("Update Player")
    @Description("Negative test: Updating a non-existent player")
    public void testUpdatePlayerNegativeNonExistent() {

        Player updatedPlayer = new Player();
        updatedPlayer.setAge(30);

        Long nonExistentId = 999999L;
        Response response = PlayerController.updatePlayer
                (config.getSupervisorLogin(), nonExistentId, updatedPlayer);

        assertEquals(response.getStatusCode(), 404,
                "The response code should be 404 (Not Found)");
    }

    @Test(priority = 14)
    @Story("Update Player")
    @Description("Negative test: Updating a player by a regular user " +
            "(not a supervisor or admin)")
    public void testUpdatePlayerNegativeInvalidEditor() {

        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        Player regularUser = Player.createValidPlayer();
        Response createResponse = PlayerController.createPlayer
                (config.getSupervisorLogin(), regularUser);
        assertEquals(createResponse.getStatusCode(), 200,
                "The response code should be 200");
        Player createdUser = createResponse.as(Player.class);

        Player updatedPlayer = new Player();
        updatedPlayer.setAge(30);

        Response response = PlayerController.updatePlayer
                (createdUser.getLogin(), createdPlayerId, updatedPlayer);

        assertEquals(response.getStatusCode(), 403,
                "The response code should be 403 (Forbidden)");
    }

    @Test(priority = 15)
    @Story("Delete Player")
    @Description("Positive test: Player removal")
    public void testDeletePlayerPositive() {

        Player player = Player.createValidPlayer();
        Response createResponse = PlayerController.createPlayer
                (config.getSupervisorLogin(), player);
        assertEquals(createResponse.getStatusCode(), 200,
                "The response code should be 200");
        Player createdPlayer = createResponse.as(Player.class);

        Response response = PlayerController.deletePlayer
                (config.getSupervisorLogin(), createdPlayer.getId());

        assertEquals(response.getStatusCode(), 200,
                "The response code should be 200");

        Response getResponse = PlayerController.getPlayer(createdPlayer.getId());
        assertEquals(getResponse.getStatusCode(), 404,
                "The response code should be 404 (Not Found)");
    }

    @Test(priority = 16)
    @Story("Delete Player")
    @Description("Negative test: Removing a non-existent player")
    public void testDeletePlayerNegativeNonExistent() {

        Long nonExistentId = 999999L;
        Response response = PlayerController.deletePlayer
                (config.getSupervisorLogin(), nonExistentId);

        assertEquals(response.getStatusCode(), 404,
                "The response code should be 404 (Not Found)");
    }

    @Test(priority = 17)
    @Story("Delete Player")
    @Description("Negative test: Player deletion by a regular user " +
            "(not a supervisor or admin)")
    public void testDeletePlayerNegativeInvalidEditor() {

        if (createdPlayerId == null) {
            testCreatePlayerPositive();
        }

        Player regularUser = Player.createValidPlayer();
        Response createResponse = PlayerController.createPlayer
                (config.getSupervisorLogin(), regularUser);
        assertEquals(createResponse.getStatusCode(), 200,
                "The response code should be 200");
        Player createdUser = createResponse.as(Player.class);

        Response response = PlayerController.deletePlayer
                (createdUser.getLogin(), createdPlayerId);

        assertEquals(response.getStatusCode(), 403,
                "The response code should be 403 (Forbidden)");
    }

    @Test(priority = 18)
    @Story("Delete Player")
    @Description("Critical test: Attempting to delete the supervisor user")
    public void testDeleteSupervisorNegative() {

        Response getResponse = PlayerController.getPlayer(1L);

        if (getResponse.getStatusCode() == 200) {
            Player supervisor = getResponse.as(Player.class);

            if ("supervisor".equals(supervisor.getRole())) {

                Response response = PlayerController.deletePlayer
                        (config.getAdminLogin(), supervisor.getId());

                assertEquals(response.getStatusCode(), 403,
                        "The response code should be 403 - " +
                                "supervisor cannot be deleted");
            }
        }
    }

    @Test(priority = 19)
    @Story("Create Player")
    @Description("Critical test: Creating two players with the same login")
    public void testCreatePlayerDuplicateLogin() {

        Player player1 = Player.createValidPlayer();
        String duplicateLogin = "duplicate" + System.currentTimeMillis();
        player1.setLogin(duplicateLogin);

        Response response1 = PlayerController.createPlayer
                (config.getSupervisorLogin(), player1);
        assertEquals(response1.getStatusCode(), 200,
                "The response code should be 200");

        Player player2 = Player.createValidPlayer();
        player2.setLogin(duplicateLogin);

        Response response2 = PlayerController.createPlayer
                (config.getSupervisorLogin(), player2);

        assertEquals(response2.getStatusCode(), 400,
                "The response code should be 400 - duplicate login is not allowed");
        String errorMsg = response2.getBody().asString();
        assertTrue(errorMsg.contains("login") || errorMsg.contains("duplicate"),
                "The error message should contain information about the duplicate login.");
    }

    @Test(priority = 20)
    @Story("Create Player")
    @Description("Critical test: Creating two players with the same screenName")
    public void testCreatePlayerDuplicateScreenName() {

        Player player1 = Player.createValidPlayer();
        String duplicateScreenName = "DuplicateScreen" + System.currentTimeMillis();
        player1.setScreenName(duplicateScreenName);

        Response response1 = PlayerController.createPlayer
                (config.getSupervisorLogin(), player1);
        assertEquals(response1.getStatusCode(), 200,
                "The response code should be 200");

        Player player2 = Player.createValidPlayer();
        player2.setScreenName(duplicateScreenName);

        Response response2 = PlayerController.createPlayer
                (config.getSupervisorLogin(), player2);

        assertEquals(response2.getStatusCode(), 400,
                "Response code should be 400 - duplicate screenName not allowed");
        String errorMsg = response2.getBody().asString();
        assertTrue(errorMsg.contains("screenName") || errorMsg.contains("duplicate"),
                "The error message should include information about the duplicate screenName");
    }
}

package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.api.client.RestClient;
import org.example.models.Player;
import org.example.utils.LogUtils;


public class PlayerController {

    private static final Logger logger = LogManager.getLogger(PlayerController.class);

    private static final String BASE_PATH = "/player";
    private static final String CREATE_PLAYER_ENDPOINT = BASE_PATH + "/create/{editor}";
    private static final String DELETE_PLAYER_ENDPOINT = BASE_PATH + "/delete/{editor}";
    private static final String GET_PLAYER_ENDPOINT = BASE_PATH + "/get";
    private static final String UPDATE_PLAYER_ENDPOINT = BASE_PATH + "/update/{editor}/{id}";

    private final RestClient restClient;

    public PlayerController(RestClient restClient) {
        this.restClient = restClient;
    }

    @Step("Creating a new player with editor {editor}")
    public Response createPlayer(String editor, String supervisorPassword, Player player) {
        LogUtils.logInfo(logger, "Creating a new player: "
                + player.getLogin() + " with editor: " + editor);
        return restClient.post(CREATE_PLAYER_ENDPOINT, player,
                "editor", editor);
    }

    @Step("Getting player by ID: {playerId}")
    public Response getPlayer(Long playerId) {
        LogUtils.logInfo(logger, "Getting player with ID: " + playerId);
        return restClient.get(GET_PLAYER_ENDPOINT + "?playerId=" + playerId);
    }

    @Step("Updating player with ID: {id} by editor {editor}")
    public Response updatePlayer(String editor, String adminPassword, Long id, Player player) {
        LogUtils.logInfo(logger, "Updating player with ID: " + id
                + " by editor: " + editor);
        return restClient.put(UPDATE_PLAYER_ENDPOINT, player, "editor",
                editor, "id", id);
    }

    @Step("Deleting player with ID: {playerId} by editor {editor}")
    public Response deletePlayer(String editor, Long playerId) {
        LogUtils.logInfo(logger, "Deleting player with ID: "
                + playerId + " by editor: " + editor);
        return restClient.delete(DELETE_PLAYER_ENDPOINT
                + "?playerId=" + playerId, "editor", editor);
    }
}

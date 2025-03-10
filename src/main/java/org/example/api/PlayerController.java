package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.models.Player;
import org.example.utils.ApiUtils;
import org.example.utils.LogUtils;

public class PlayerController {

    private static final Logger logger = LogManager.getLogger(PlayerController.class);

    private static final String CREATE_PLAYER_ENDPOINT = "/player/create/{editor}";
    private static final String DELETE_PLAYER_ENDPOINT = "/player/delete/{editor}";
    private static final String GET_PLAYER_ENDPOINT = "/player/get";
    private static final String UPDATE_PLAYER_ENDPOINT = "/player/update/{editor}/{id}";

    @Step("Створення нового гравця з редактором {editor}")
    public static Response createPlayer(String editor, Player player) {
        LogUtils.logInfo(logger, "Створення нового гравця: " + player.getLogin() + " редактором: " + editor);
        return ApiUtils.post(CREATE_PLAYER_ENDPOINT, player, editor);
    }

    @Step("Отримання гравця за ID: {playerId}")
    public static Response getPlayer(Long playerId) {
        LogUtils.logInfo(logger, "Отримання гравця з ID: " + playerId);
        return ApiUtils.get(GET_PLAYER_ENDPOINT + "?playerId=" + playerId);
    }

    @Step("Оновлення гравця з ID: {id} редактором {editor}")
    public static Response updatePlayer(String editor, Long id, Player player) {
        LogUtils.logInfo(logger, "Оновлення гравця з ID: " + id + " редактором: " + editor);
        return ApiUtils.put(UPDATE_PLAYER_ENDPOINT, player, editor, id);
    }

    @Step("Видалення гравця з ID: {playerId} редактором {editor}")
    public static Response deletePlayer(String editor, Long playerId) {
        LogUtils.logInfo(logger, "Видалення гравця з ID: " + playerId + " редактором: " + editor);
        return ApiUtils.delete(DELETE_PLAYER_ENDPOINT + "?playerId=" + playerId, null, editor);
    }
}

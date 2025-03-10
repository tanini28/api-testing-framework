package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {
    private Long id;
    private Integer age;
    private String gender;
    private String login;
    private String password;
    private String role;
    private String screenName;

    public static Player createValidPlayer() {
        return Player.builder()
                .age(25)
                .gender("male")
                .login("testuser" + System.currentTimeMillis())
                .password("Password123")
                .role("user")
                .screenName("TestUser" + System.currentTimeMillis())
                .build();
    }

    public static Player createValidAdmin() {
        return Player.builder()
                .age(30)
                .gender("male")
                .login("testadmin" + System.currentTimeMillis())
                .password("Password123")
                .role("admin")
                .screenName("TestAdmin" + System.currentTimeMillis())
                .build();
    }

    public static Player createInvalidAgeTooYoung() {
        Player player = createValidPlayer();
        player.setAge(15);
        return player;
    }

    public static Player createInvalidRole(){
        Player player = createValidPlayer();
        player.setRole("supervisor");
        return player;
    }

    public static Player createInvalidPassword(){
        Player player = createValidPlayer();
        player.setPassword("short");
        return player;
    }

    public static Player createInvalidGender(){
        Player player = createValidPlayer();
        player.setGender("unknown");
        return player;
    }

    public static Player createInvalidTooOld() {
        Player player = createValidPlayer();
        player.setAge(70);
        return player;
    }
}

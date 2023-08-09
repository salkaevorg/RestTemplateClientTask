import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Consumer {
    private static final String URL = "http://94.198.50.185:7081/api/users";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.getForEntity(URL, String.class);
        String sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        StringBuilder code = new StringBuilder();
        System.out.println("User list: " + response.getBody());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionId);
        User user = new User(3L, "James", "Brown", (byte) 25);
        HttpEntity<User> userRequest = new HttpEntity<>(user, headers);
        ResponseEntity<String> postResponse =
                restTemplate.postForEntity(URL, userRequest, String.class);
        code.append(postResponse.getBody());
        System.out.println("Saved user: " + postResponse.getBody());

        user.setName("Thomas");
        user.setLastName("Shelby");
        userRequest = new HttpEntity<>(user, headers);
        ResponseEntity<String> putResponse =
                restTemplate.exchange(URL, HttpMethod.PUT, userRequest, String.class);
        code.append(putResponse.getBody());
        System.out.println("Updated user: " + putResponse.getBody());

        ResponseEntity<String> deleteResponse =
                restTemplate.exchange(String.format(URL + "/%s",
                        user.getId()), HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        code.append(deleteResponse.getBody());
        System.out.println("Deleted user: " + deleteResponse.getBody());

        if (code.length() == 18) {
            System.out.println("Final code: " + code);
        } else System.out.println("Code does not consist of 18 symbols");
    }
}

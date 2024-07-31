package kr.co.direa.direachatbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ExecutionException;

@Controller
public class ChatController {

    @Autowired
    private GptService gptService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/chat")
    @ResponseBody
    public String chat(@RequestParam("message") String message) {
        try {
            String response = gptService.getGptResponse(message).get();
            return response;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
}
package com.jason.trade.web.portal.controller;

import com.jason.trade.order.mq.OrderMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
public class RabbitTestController {

    @Autowired
    private OrderMessageSender orderMessageSender;

    /**
     * Handles a request to test delayed message sending.
     *
     * @return A response indicating the status of the message send operation.
     */
    @ResponseBody
    @RequestMapping("/delayTest")
    public String delayTest() {
        String message = "Message sent at: " + LocalDateTime.now() + " - Content: Delayed queue test";
        orderMessageSender.sendPayStatusCheckDelayMessage(message);
        return "Message sent successfully.";
    }
}

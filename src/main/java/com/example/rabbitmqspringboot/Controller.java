package com.example.rabbitmqspringboot;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

@RestController
@RequestMapping("/api/v1")
public class Controller {

    private final RabbitTemplate rabbitTemplate;

    public Controller(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @GetMapping("/test/{name}")
    public String api(@PathVariable(value = "name") String name) {

        Person person = new Person(1, name);
        rabbitTemplate.convertAndSend("Mobile", person);
        rabbitTemplate.convertAndSend("Direct-Exchange", "mobile", person);
        rabbitTemplate.convertAndSend("Fanout-Exchange", "", person);
        rabbitTemplate.convertAndSend("Topic-Exchange", "tv.mobile.ac", person);
        return "Success";
    }

    @GetMapping("/{name}")
    public String apiForHeader(@PathVariable(value = "name") String name) throws IOException {

        Person person = new Person(1, name);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(bos);
        objectOutput.writeObject(person);
        objectOutput.flush();
        objectOutput.close();

        byte[] byteMessage = bos.toByteArray();
        bos.close();

        Message message = MessageBuilder
                .withBody(byteMessage)
                .setHeader("item1", "mobile")
                .setHeader("item2", "ac")
                .build();

        rabbitTemplate.send("Header-Exchange", "", message);

        return "Success";
    }
}

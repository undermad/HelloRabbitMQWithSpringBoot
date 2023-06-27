package com.example.rabbitmqspringboot;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

@Service
public class RabbitMQConsumer {


    @RabbitListener(queues = "Mobile")
    public void getMessage(Person person) {
        System.out.println(person.getName());
    }

    @RabbitListener(queues = "AC")
    public void getHeaderMessage(byte[] message) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(message);
        ObjectInput objectInput = new ObjectInputStream(bis);
        Person person = (Person) objectInput.readObject();
        objectInput.close();
        bis.close();
        System.out.println(person.getName());

    }
}

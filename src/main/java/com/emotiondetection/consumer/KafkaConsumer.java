package com.emotiondetection.consumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emotiondetection.constant.ApplicationConstant;
import com.emotiondetection.data.Student;

/**
 * Created by asus on 5/30/2021.
 */
@RestController
@RequestMapping("/consume")
public class KafkaConsumer {

    @Autowired
    private ConcurrentKafkaListenerContainerFactory<String, Student> factory;

    @GetMapping("/message")
    public List<Student> receiveMessage() {
        List<Student> students = new ArrayList<>();
        ConsumerFactory<String, Student> consumerFactory = (ConsumerFactory<String, Student>) factory.getConsumerFactory();
        Consumer<String, Student> consumer = consumerFactory.createConsumer();
        try {
            consumer.subscribe(Arrays.asList(ApplicationConstant.TOPIC_NAME));
            ConsumerRecords<String, Student> consumerRecords = consumer.poll(Duration.ofSeconds(100l));
            if(consumerRecords.count()==0){
                System.out.println("NOOOOOOOOO MSGGGGCOn");
            }
//            ConsumerRecords<String, Student> consumerRecords = consumer.poll(10000);
            Iterable<ConsumerRecord<String, Student>> records = consumerRecords.records(ApplicationConstant.TOPIC_NAME);
            Iterator<ConsumerRecord<String, Student>> iterator = records.iterator();

            while (iterator.hasNext()) {
                System.out.println(iterator.next().value());
                students.add(iterator.next().value());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }
}
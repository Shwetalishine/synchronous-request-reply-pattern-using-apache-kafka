package com.kafka.kafkasync;




import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
    public class KafkaTestController {
        @Value("${kafka.request.topic}")
        private String requestTopic;




    @Autowired
    private ReplyingKafkaTemplate<String, Student, Result> replyingKafkaTemplate;
    @PostMapping("/get-result")
    public ResponseEntity<Result> getObject(@RequestBody Student student)
            throws InterruptedException, ExecutionException,Exception {
        System.out.println("student"+student);
        ProducerRecord<String, Student> record = new ProducerRecord<>(requestTopic, null, student.getRegistrationNumber(), student);
        System.out.println("record is "+record);
        RequestReplyFuture<String, Student, Result> future = replyingKafkaTemplate.sendAndReceive(record);
        System.out.println("future is "+future);

        ConsumerRecord<String, Result> response = future.get();
        System.out.println("response is "+response);
        return new ResponseEntity<>(response.value(), HttpStatus.OK);
    }
}

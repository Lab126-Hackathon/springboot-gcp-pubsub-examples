package com.lab126.hackathon.gcp.pubsub.publisher;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class PublisherController {

    @RequestMapping(value = "/api/v1/{topicId}/message", method = RequestMethod.POST)
    public ResponseEntity<String> publishMessage(@PathVariable final String topicId,
            @RequestBody final HashMap<?, ?> message) throws Exception {

        System.out.println("Post body message received:" + message.toString());

        List<ApiFuture<String>> futures = new ArrayList<>();
        
        // This is purely used for emulation code
        //String hostport = System.getenv("PUBSUB_EMULATOR_HOST");

        // use the default project id
        final String PROJECT_ID = ServiceOptions.getDefaultProjectId();
        Publisher publisher = null;
        // Code used for emulator
        //ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8085").usePlaintext().build();
        try {
            // Code used for emulator
            //TransportChannelProvider channelProvider = FixedTransportChannelProvider
            //        .create(GrpcTransportChannel.create(channel));
            // Code used for emulator
            //CredentialsProvider credentialsProvider = NoCredentialsProvider.create();

            // Set the channel and credentials provider when creating a `TopicAdminClient`.
            // Similarly for SubscriptionAdminClient
            // Code used for emulator
            //TopicAdminClient topicClient = TopicAdminClient.create(TopicAdminSettings.newBuilder()
            //        .setTransportChannelProvider(channelProvider).setCredentialsProvider(credentialsProvider).build());

            //ProjectTopicName topicName = ProjectTopicName.of("cmb-canada-globalhackaton", topicId);
            ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, topicId);
            // Set the channel and credentials provider when creating a `Publisher`.
            // Similarly for Subscriber
            // Code used for emulator
            //Publisher publisher = Publisher.newBuilder(topicName).setChannelProvider(channelProvider)
            //        .setCredentialsProvider(credentialsProvider).build();
            publisher = Publisher.newBuilder(topicName).build();

            // convert message to bytes
            ByteString data = ByteString.copyFromUtf8(message.toString());
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

            // Schedule a message to be published. Messages are automatically batched.
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            futures.add(future);
        } finally {

            // Wait on any pending requests
            List<String> messageIds = ApiFutures.allAsList(futures).get();

            for (String messageId : messageIds) {
                System.out.println(messageId);
            }
            // Code for emulator
            //channel.shutdown();
            publisher.shutdown();
        }
        // [END use_pubsub_emulator]

        return new ResponseEntity<String>("{message submitted}", HttpStatus.OK);
    }
}

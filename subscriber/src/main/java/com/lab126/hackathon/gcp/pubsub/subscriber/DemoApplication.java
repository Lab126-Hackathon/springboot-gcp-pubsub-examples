package com.lab126.hackathon.gcp.pubsub.subscriber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.Subscription;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.ServiceOptions;

@SpringBootApplication
public class DemoApplication {

	static class MessageReceiverExample implements MessageReceiver {

		@Override
		public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
		  System.out.println(
			  "Message Id: " + message.getMessageId() + " Data: " + message.getData().toStringUtf8());
		  // Ack only after all work for the message is complete.
		  consumer.ack();
		}
	}

	public static void main(String[] args) throws Exception {

		SpringApplication.run(DemoApplication.class, args);

		final String PROJECT_ID = ServiceOptions.getDefaultProjectId();

  		// Set the channel and credentials provider when creating a `TopicAdminClient`.
  		// Similarly for SubscriptionAdminClient
  		SubscriptionAdminClient subscriptionAdminClient =
      		SubscriptionAdminClient.create(SubscriptionAdminSettings.newBuilder().build());
					  
		//subscription.getName()
		ProjectSubscriptionName subscriptionName =ProjectSubscriptionName.of(PROJECT_ID, "testSubscription");  
		//ProjectTopicName topicName = ProjectTopicName.of("cmb-canada-globalhackaton", "testTopic");

		// Set the channel and credentials provider when creating a `Publisher`.  
		// Similarly for Subscriber
		Subscriber subscriber = null;
	
			MessageReceiver receiver =
				new MessageReceiver() {
			  	@Override
			  	public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
					System.out.println("Received message: " + message.getData().toStringUtf8());
					consumer.ack();
			  	}
				};

			try {
		  	subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
		  	subscriber.addListener(
			  	new Subscriber.Listener() {
					@Override
					public void failed(Subscriber.State from, Throwable failure) {
				  	// Handle failure. This is called when the Subscriber encountered a fatal error and is
				  	// shutting down.
				  	System.err.println(failure);
					}
			  	},
			  	MoreExecutors.directExecutor());
		  	subscriber.startAsync().awaitRunning();
	
		  	// In this example, we will pull messages for one minute (60,000ms) then stop.
		  	// In a real application, this sleep-then-stop is not necessary.
		  	// Simply call stopAsync().awaitTerminated() when the server is shutting down, etc.
			  //Thread.sleep(60000);
			subscriber.awaitTerminated();
			} finally {
		  	if (subscriber != null) {
				subscriber.stopAsync().awaitTerminated();
			  }
		}

	}
}

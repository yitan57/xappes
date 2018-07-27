package com.technologies.yanny.xappes.Database;
import android.content.Context;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class DynamoDB {

    // Declare a DynamoDBMapper object
    com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper dynamoDBMapper;

    public com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper myDynamoDB(Context context) {
        // AWSMobileClient enables AWS user credentials to access your table
        AWSMobileClient.getInstance().initialize(context).execute();

        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();


        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);

        return com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(configuration)
                .build();
    }
}
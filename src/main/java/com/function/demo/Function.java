package com.function.demo;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.ServiceBusQueueOutput;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

    @FunctionName("sendMessage")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
        @ServiceBusQueueOutput(name = "outputMessage", queueName = "demo-monex-queue", connection = "AzureWebJobsServiceBus") OutputBinding<String> outputMessage,
        final ExecutionContext context) {

        context.getLogger().info("Java HTTP trigger processed a request.");
        final String message = request.getBody().orElse(null);

        if (message == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            outputMessage.setValue(message);
            return request.createResponseBuilder(HttpStatus.OK).body("Message processed: " + message).build();
        }
    }
}

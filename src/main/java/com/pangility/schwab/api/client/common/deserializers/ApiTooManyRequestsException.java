package com.pangility.schwab.api.client.common.deserializers;

/**
 * Exception thrown when a 429 - TOO MANY REQUESTS is returned from the API
 * To be used to slow down and throttle API calls.
 */
public class ApiTooManyRequestsException extends Exception { }

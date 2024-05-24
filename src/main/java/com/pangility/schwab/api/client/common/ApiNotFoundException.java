package com.pangility.schwab.api.client.common;

/**
 * Exception thrown when a 404 - NOT FOUND is returned from the API
 * To be used to gracefully handle not found exceptions from the API.
 */
public class ApiNotFoundException extends Exception { }

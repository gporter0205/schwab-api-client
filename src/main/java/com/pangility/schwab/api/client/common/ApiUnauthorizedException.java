package com.pangility.schwab.api.client.common;

/**
 * Exception thrown when a 401 - UNAUTHORIZED is returned from the API
 * To be used to gracefully handle UNAUTHORIZED codes from the API and
 * attempt to generate a new Access Token and retry before throwing an error.
 */
public class ApiUnauthorizedException extends Exception { }

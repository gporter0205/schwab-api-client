/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pangility.schwab.api.client.marketdata;

public class MarketNotFoundException extends Exception {
    public MarketNotFoundException(String msg) {
        super(msg);
    }
}

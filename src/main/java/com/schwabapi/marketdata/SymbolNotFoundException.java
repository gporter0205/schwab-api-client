/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schwabapi.marketdata;

public class SymbolNotFoundException extends Exception {
    public SymbolNotFoundException(String msg) {
        super(msg);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author vikrant
 */
class FriendInformation {
    public int id;
    public String username;
    public String name;
    
    @Override
    public String toString() {
        return ( name + " ( " + username + " )" );    }
}

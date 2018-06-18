package com.soon.socket.msg;

/**
 * ClientMsg:1 - 4999
 * PlayerMsg:5000 - 5999
 * RelationMsg:6000 - 6499
 * RouteMsg:6500 - 6999
 * BattleMsg:7000 - 7499
 * CrossMsg:7500 - 7999
 * 
 * @author songlin
 */
public final class MessageID implements ClientMsg,RelationMsg,PlayerMsg,BattleMsg,CrossMsg,RouteMsg {

}

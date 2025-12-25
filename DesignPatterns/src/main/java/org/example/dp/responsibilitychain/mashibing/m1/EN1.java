package org.example.dp.responsibilitychain.mashibing.m1;

public class EN1 {
    public static void main(String[] args) {
        Msg msg = new Msg();
        msg.setMsg("大家好:),<script>,欢迎访问 mashibing.com ,大家都是996 ");
        //处理msg
        String r = msg.getMsg();
        r = r.replace("<", "[");
        r = r.replace(">", "]");
        msg.setMsg(r);
        r = r.replaceAll("996", "955");
        msg.setMsg(r);
        System.out.println(msg);
    }
}

class Msg {
    String name;
    String msg;
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    @Override
    public String toString() {
        return "Msg{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
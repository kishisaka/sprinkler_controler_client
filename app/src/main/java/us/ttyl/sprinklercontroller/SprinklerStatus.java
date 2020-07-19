package us.ttyl.sprinklercontroller;

public class SprinklerStatus {
    int status1;
    int status2;
    int status3;
    int status4;
    int status5;
    int status6;
    int status7;
    int status8;

    public String getStatus(int status) {
        if (status == 1) {
            return "off";
        } else {
            return "on";
        }
    }
}

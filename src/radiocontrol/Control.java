/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package radiocontrol;

import Socket.Client;
import java.io.IOException;
import java.math.BigDecimal;

import java.math.RoundingMode;

public class Control {

    final int MAX_Volume = 20;
    final int MIN_Volume = 0;
    final int MAX_FM = 110;
    final int MIN_FM = 90;
    int volume;
    double fm;
    boolean sleep;
    boolean mute;
    boolean power;

    static BigDecimal bd;
    Client clientCtrl;

    public Control() throws Exception {
        this.volume = (MAX_Volume + MIN_Volume) / 2;
        this.fm = (MAX_FM + MIN_FM) / 2;
        this.sleep = true;
        this.mute = false;
        this.power = true;
        clientCtrl = new Client(1111);
    }

    public int getVolume() {
        return volume;
    }

    public void volumeUp() throws IOException {
        if (!sleep && MAX_Volume > this.volume  && MIN_Volume <= this.volume) {
            this.volume++;
            if (mute) {
                setMute();
            }
        }

        clientCtrl.sendCommand("v " + getVolume(),2828);
    }
    
    public void volumeDown() throws IOException {
        if (!sleep && MAX_Volume >= this.volume && MIN_Volume < this.volume) {
            this.volume--;
            if (mute) {
                setMute();
            }
        }

        clientCtrl.sendCommand("v " + getVolume(),2828);
    }



    public double getFm() {
        bd = new BigDecimal(fm).setScale(2, RoundingMode.HALF_UP);
        fm = bd.doubleValue();
        return fm;
    }

    public void fmUp() throws IOException {
        if (!sleep &&MAX_FM > this.fm && this.fm >= MIN_FM) {
            this.fm += 0.1;
        }
        clientCtrl.sendCommand("fm " + getFm(),2828);
    }

    public void fmDown() throws IOException {
        if (!sleep &&MAX_FM >= this.fm && this.fm > MIN_FM) {
            this.fm -= 0.1;
        }
        clientCtrl.sendCommand("fm " + getFm(),2828);
    }

    public boolean isSleep() {
        return sleep;
    }

    public void setSleep() throws IOException {

        this.sleep = !this.sleep;
        clientCtrl.sendCommand("s " + this.sleep,2828);
        System.out.println(sleep);
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute() throws IOException {

        this.mute = !this.mute;
        clientCtrl.sendCommand("m " + this.mute,2828);
    }

    public boolean isPower() {
        return power;
    }

    public void setPower() throws IOException {
        clientCtrl.sendCommand("p",2828);
        power = false;
        System.exit(0);
    }

    void togglePower() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void fmUP() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

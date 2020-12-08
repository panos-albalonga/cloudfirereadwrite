package com.albalonga.cloudfirereadwrite;

import android.widget.ImageView;
import android.widget.TextView;

public class Player {
    private String name, pos;
    private int seat;
    private ImageView imgV;
    private TextView  txtV;

    public Player(String name, int seat, ImageView imgV) {
        this.name = name;
        this.seat = seat;
        this.imgV = imgV;
    }
    public Player(String name, int seat) {
        this.name = name;
        this.seat = seat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public ImageView getImgV() {
        return imgV;
    }

    public void setImgV(ImageView imgV) {
        this.imgV = imgV;
    }

    public TextView getTxtV() {
        return txtV;
    }

    public void setTxtV(TextView txtV) {
        this.txtV = txtV;
    }
}

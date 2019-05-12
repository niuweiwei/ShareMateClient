package cn.edu.hebtu.software.sharemateclient.Entity;

import android.graphics.Bitmap;


import java.io.Serializable;


public class Goods implements Serializable{
    private int cakeId;
    private String cakeName;
    private int cakeSize;
    private int cakePrice;
    private String cakeImageUrl;
    private String cakeDetail;
    private int cakeTypeId;
    private String date;
    private int count;
    private Bitmap bitmap;
    public Goods() {
    }

    public Goods(String cakeName, int cakePrice) {
        this.cakeName = cakeName;
        this.cakePrice = cakePrice;
    }

    public Goods(int cakeId, String cakeName, int cakeSize, int cakePrice,
                 String cakeImageUrl, int cakeTypeId, String date, int count) {
        this.cakeId = cakeId;
        this.cakeName = cakeName;
        this.cakeSize = cakeSize;
        this.cakePrice = cakePrice;
        this.cakeImageUrl = cakeImageUrl;
        this.cakeTypeId = cakeTypeId;
        this.date = date;
        this.count = count;
    }

    public String getCakeDetail() {
        return cakeDetail;
    }

    public void setCakeDetail(String cakeDetail) {
        this.cakeDetail = cakeDetail;
    }

    public int getCakeId() {
        return cakeId;
    }

    public void setCakeId(int cakeId) {
        this.cakeId = cakeId;
    }

    public String getCakeName() {
        return cakeName;
    }

    public void setCakeName(String cakeName) {
        this.cakeName = cakeName;
    }

    public int getCakeSize() {
        return cakeSize;
    }

    public void setCakeSize(int cakeSize) {
        this.cakeSize = cakeSize;
    }

    public int getCakePrice() {
        return cakePrice;
    }

    public void setCakePrice(int cakePrice) {
        this.cakePrice = cakePrice;
    }

    public String getCakeImageUrl() {
        return cakeImageUrl;
    }

    public void setCakeImageUrl(String cakeImageUrl) {
        this.cakeImageUrl = cakeImageUrl;
    }

    public int getCakeTypeId() {
        return cakeTypeId;
    }

    public void setCakeTypeId(int cakeTypeId) {
        this.cakeTypeId = cakeTypeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "cakeId=" + cakeId +
                ", cakeName='" + cakeName + '\'' +
                ", cakeSize=" + cakeSize +
                ", cakePrice=" + cakePrice +
                ", cakeImageUrl='" + cakeImageUrl + '\'' +
                ", cakeDetail='" + cakeDetail + '\'' +
                ", cakeTypeId=" + cakeTypeId +
                ", date='" + date + '\'' +
                ", count=" + count +
                ", bitmap=" + bitmap +
                '}';
    }
}

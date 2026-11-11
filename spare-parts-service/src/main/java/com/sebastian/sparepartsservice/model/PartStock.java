package com.sebastian.sparepartsservice.model;


public class PartStock {
    private final int campusId;
    private final int partId;
    private int totalQuantity;
    private int reservedQuantity;

    public PartStock(int campusId, int partId, int totalQuantity){
        this.campusId = campusId;
        this.partId = partId;
        this.totalQuantity = totalQuantity;
        this.reservedQuantity = 0;
    }

    public int getReservedQuantity(){
        return reservedQuantity;
    }

    public int getTotalQuantity(){
        return totalQuantity;
    }

    public int getCampusId(){
        return campusId;
    }

    public int getPartId(){
        return partId;
    }

    public int getAvailableQuantity(){
        return totalQuantity - reservedQuantity;
    }


    public synchronized void reserve(int quantity){
        if(quantity <= 0){
            throw new IllegalArgumentException("预留数量必须大于0");
        }
        if(getAvailableQuantity() < quantity){
            throw new IllegalStateException("可用库存不足");
        }
        reservedQuantity += quantity;
    }

    public synchronized void issue(int quantity){
        if(quantity <= 0){
            throw new IllegalArgumentException("领用数量必须大于0");
        }
        if(quantity > reservedQuantity){
            throw new IllegalStateException("领用数量不能超过预留数量");
        }
        totalQuantity -= quantity;
        reservedQuantity -= quantity;
    }

    public synchronized void release(int quantity){
        if(quantity <= 0){
            throw new IllegalArgumentException("取消数量必须大于0");
        }
        if(quantity > reservedQuantity){
            throw new IllegalStateException("取消数量不能超过预留数量");
        }
        reservedQuantity -= quantity;
    }

}

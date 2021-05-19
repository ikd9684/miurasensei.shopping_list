package model;

public class Goods {

    private String uuid;
    private String name;
    private Integer number;
    private String memo;
    private String registeredDatetime;
    private String purchasedDatetime;
    private String updateDatetime;

    // FIXME JavaDoc コメント書きましょう
    public String getUuid() {

        return uuid;
    }

    public void setUuid(String uuid) {

        this.uuid = uuid;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Integer getNumber() {

        return number;
    }

    public void setNumber(Integer number) {

        this.number = number;
    }

    public String getMemo() {

        return memo;
    }

    public void setMemo(String memo) {

        this.memo = memo;
    }

    public String getRegisteredDatetime() {

        return registeredDatetime;
    }

    public String getPurchasedDatetime() {

        return purchasedDatetime;
    }

    public String getUpdateDatetime() {

        return updateDatetime;
    }

    public Goods() {

    }

    public void setRegisteredDatetime(String registeredDatetime) {

        this.registeredDatetime = registeredDatetime;
    }

    public void setPurchasedDatetime(String purchasedDatetime) {

        this.purchasedDatetime = purchasedDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {

        this.updateDatetime = updateDatetime;
    }

    public Goods(String uuid, String name, Integer number, String memo, String registeredDatetime,
            String purchasedDatetime, String updateDatetime) {

        this.uuid = uuid;
        this.name = name;
        this.number = number;
        this.memo = memo;
        this.registeredDatetime = registeredDatetime;
        this.purchasedDatetime = purchasedDatetime;
        this.updateDatetime = updateDatetime;
    }

}

package com.dtech.orm;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 14/10/2015.
 */
public class MtlPelanggan extends SugarRecord<MtlPelanggan> {
    private String code;
    private String name;
    private String address;
    private String lastXPosition;
    private String lastYPosition;
    private boolean active;

    public MtlPelanggan(){}

    public MtlPelanggan(String code, String name, String address){
        this(code, name, address, false);
    }

    public MtlPelanggan(String code, String name, String address, boolean checkBackwardData){
        super();
        setCode(code);
        setName(name);
        setAddress(address);
        setActive(true);

        if (checkBackwardData)
            validateBackwardData();
    }

    private void validateBackwardData() {
        Iterator<Customer> oldData = Customer.findAll(Customer.class);
        while(oldData.hasNext()) {
            Customer currentOldData = oldData.next();
            // this is means that this data already exist in MtlPelanggan
            boolean existInLite = custExist("code = ?", currentOldData.getCode());
            if (!existInLite){
                MtlPelanggan mtlPelanggan = new MtlPelanggan(currentOldData.getCode(),
                        currentOldData.getName(), currentOldData.getAddress());
                mtlPelanggan.setLastXPosition(currentOldData.getLastXPosition());
                mtlPelanggan.setLastYPosition(currentOldData.getLastXPosition());
                mtlPelanggan.save();
            }
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static boolean custExist(String where, String params) {
        if (!where.contains("?"))
            return false;
        List<MtlPelanggan> customer = MtlPelanggan.find(MtlPelanggan.class, where, params);
        return customer.size() > 0;
    }

    public String getLastXPosition() {
        return lastXPosition;
    }

    public void setLastXPosition(String lastXPosition) {
        this.lastXPosition = lastXPosition;
    }

    public String getLastYPosition() {
        return lastYPosition;
    }

    public void setLastYPosition(String lastYPosition) {
        this.lastYPosition = lastYPosition;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLastVisit() {
        MtlPelanggaran lastRecord = MtlPelanggaran.getLastFoulRecord(this);
        SimpleDateFormat xxx = new SimpleDateFormat(DefaultOps.DEFAULT_DATETIME_FORMAT);
        if (lastRecord == null) {
            return xxx.format(Calendar.getInstance().getTime());
        }
        return lastRecord.getFoulDate();
    }
}

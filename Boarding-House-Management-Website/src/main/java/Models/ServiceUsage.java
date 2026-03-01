/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author huudanh
 */
public class ServiceUsage {

    private int usageId;
    private int contractId;
    private int serviceId;
    private BigDecimal quantity;
    private LocalDate usageDate;
    private boolean billed;

    public ServiceUsage() {
    }

    public ServiceUsage(int usageId, int contractId, int serviceId, BigDecimal quantity, LocalDate usageDate, boolean billed) {
        this.usageId = usageId;
        this.contractId = contractId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.usageDate = usageDate;
        this.billed = billed;
    }

    public int getUsageId() {
        return usageId;
    }

    public void setUsageId(int usageId) {
        this.usageId = usageId;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDate getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDate usageDate) {
        this.usageDate = usageDate;
    }

    public boolean isBilled() {
        return billed;
    }

    public void setBilled(boolean billed) {
        this.billed = billed;
    }
    
}

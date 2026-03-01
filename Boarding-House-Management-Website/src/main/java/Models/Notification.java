/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author huuda
 */
public class Notification {

    private int createdBy;
    private String title;
    private String content;
    private Integer targetContractId; // null = broadcast
    private boolean isDeleted;

    public Notification() {
    }

    public Notification(int createdBy, String title, String content, Integer targetContractId, boolean isDeleted) {
        this.createdBy = createdBy;
        this.title = title;
        this.content = content;
        this.targetContractId = targetContractId;
        this.isDeleted = isDeleted;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTargetContractId() {
        return targetContractId;
    }

    public void setTargetContractId(Integer targetContractId) {
        this.targetContractId = targetContractId;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
}

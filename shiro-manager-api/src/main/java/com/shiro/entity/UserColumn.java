package com.shiro.entity;

/**
 * Created by 白超 on 2018/7/24.
 */
public class UserColumn {

    private String label;

    private String columnKey;

    private boolean canEdit;

    private Integer minWidth;

    private String align = "center";

    public UserColumn(String label, String columnKey) {
        this.label = label;
        this.columnKey = columnKey;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Integer getMinWidth() {
        return minWidth == null ? 200 : minWidth;
    }

    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }
}
